package `in`.koreatech.koin.data.stomp

import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import org.hildan.krossbow.stomp.LostReceiptException
import org.hildan.krossbow.stomp.MissingHeartBeatException
import org.hildan.krossbow.stomp.SessionDisconnectedException
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.WebSocketClosedUnexpectedly
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.websocket.WebSocketException
import org.hildan.krossbow.websocket.reconnection.ExponentialBackOff
import org.hildan.krossbow.websocket.reconnection.RetryDelayStrategy
import timber.log.Timber

class KoinStomp @Inject constructor(
    private val baseUrl: String,
    private val tokenProvider: suspend () -> String,
    private val stompClient: StompClient
) {
    private val mutex = Mutex()
    private var stompSession: StompSession? = null
    private var isIntentionallyDisconnected = false

    private val delayStrategy: RetryDelayStrategy = ExponentialBackOff()

    private val _connectionState = MutableStateFlow<KoinStompConnectionState>(KoinStompConnectionState.Disconnected)
    val connectionState: StateFlow<KoinStompConnectionState> = _connectionState.asStateFlow()

    suspend fun connect(): StompSession {
        return mutex.withLock {
            isIntentionallyDisconnected = false
            stompSession ?: openSession()
        }
    }

    private suspend fun reconnect(): StompSession? {
        return mutex.withLock {
            if (isIntentionallyDisconnected) return@withLock null
            stompSession ?: openSession()
        }
    }

    private suspend fun openSession(): StompSession {
        _connectionState.value = KoinStompConnectionState.Connecting
        Timber.d("Connecting to STOMP...")
        val authToken = tokenProvider()
        return stompClient.connect(
            url = "${baseUrl.replaceFirst("https", "wss")}/ws-stomp",
            customStompConnectHeaders = mapOf("Authorization" to authToken)
        ).also {
            stompSession = it
            _connectionState.value = KoinStompConnectionState.Connected
            Timber.d("STOMP connected.")
        }
    }

    private suspend fun getSession(): StompSession {
        val session = mutex.withLock { stompSession }
        return session ?: connect()
    }

    suspend fun disconnect() {
        mutex.withLock {
            isIntentionallyDisconnected = true
            stompSession?.disconnect()
            stompSession = null
            _connectionState.value = KoinStompConnectionState.Disconnected
        }
    }

    /**
     * Retries [reconnect] with backoff until it succeeds or [isIntentionallyDisconnected] becomes true.
     * A [reconnect] failure must never escape to the caller here — [subscribe]'s retryWhen predicate
     * treats any exception thrown from it as terminal and stops retrying entirely.
     */
    private suspend fun awaitReconnected(cause: Throwable): Boolean {
        var attempt = 0
        while (!isIntentionallyDisconnected) {
            val delayDuration = delayStrategy.computeDelay(attempt).coerceAtMost(MAX_RECONNECT_DELAY)
            Timber.d("${cause::class.simpleName}, reconnecting (attempt ${attempt + 1}) in $delayDuration...")
            _connectionState.value = KoinStompConnectionState.Reconnecting(attempt + 1)
            delay(delayDuration)
            if (isIntentionallyDisconnected) return false
            mutex.withLock { stompSession = null }
            try {
                if (reconnect() == null) return false
                Timber.d("Reconnected.")
                return true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                attempt++
            }
        }
        return false
    }

    fun <T : Any> subscribe(
        destination: String,
        deserializer: DeserializationStrategy<T>
    ): Flow<T> {
        return flow {
            getSession().withJsonConversions().subscribe(destination, deserializer).collect { emit(it) }
        }.retryWhen { cause, _ ->
            if (isIntentionallyDisconnected || !cause.isConnectionLost()) {
                false
            } else {
                awaitReconnected(cause)
            }
        }
    }

    suspend fun <T : Any> convertAndSend(
        headers: String,
        body: T? = null,
        serializer: SerializationStrategy<T>
    ) {
        while (true) {
            try {
                getSession().withJsonConversions().convertAndSend(StompSendHeaders(headers), body, serializer)
                return
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (isIntentionallyDisconnected || !e.isConnectionLost() || !awaitReconnected(e)) {
                    Timber.e("Websocket send failed: ${e.message}")
                    throw e
                }
            }
        }
    }

    suspend inline fun <reified T : Any> convertAndSend(
        headers: String,
        body: T
    ) {
        convertAndSend(headers, body, serializer())
    }

    private fun Throwable.isConnectionLost(): Boolean =
        this is WebSocketException ||
            this is WebSocketClosedUnexpectedly ||
            this is MissingHeartBeatException ||
            this is SessionDisconnectedException ||
            this is LostReceiptException

    companion object {
        private val MAX_RECONNECT_DELAY = 30.seconds
    }
}
