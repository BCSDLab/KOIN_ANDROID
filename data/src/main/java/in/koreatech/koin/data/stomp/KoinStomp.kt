package `in`.koreatech.koin.data.stomp

import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.websocket.WebSocketException
import timber.log.Timber

class KoinStomp @Inject constructor(
    private val baseUrl: String,
    private val tokenProvider: suspend () -> String,
    private val stompClient: StompClient
) {
    private val mutex = Mutex()
    private var stompSession: StompSession? = null

    suspend fun connect(): StompSession {
        return mutex.withLock {
            val session = stompSession
            if (session != null) {
                return@withLock session
            }

            Timber.d("Connecting to STOMP...")
            val authToken = tokenProvider()
            stompClient.connect(
                url = "${baseUrl.replaceFirst("https", "wss")}/ws-stomp",
                customStompConnectHeaders = mapOf("Authorization" to authToken)
            ).also {
                stompSession = it
                Timber.d("STOMP connected.")
            }
        }
    }

    private suspend fun getSession(): StompSession {
        val session = mutex.withLock { stompSession }
        return session ?: connect()
    }

    suspend fun disconnect() {
        mutex.withLock {
            stompSession?.disconnect()
            stompSession = null
        }
    }

    fun <T : Any> subscribe(
        destination: String,
        deserializer: DeserializationStrategy<T>
    ): Flow<T> {
        return flow {
            getSession().withJsonConversions().subscribe(destination, deserializer).collect { emit(it) }
        }.retry { e ->
            if (e is WebSocketException) {
                Timber.d("WebSocketException, reconnecting...")
                mutex.withLock { stompSession = null }
                connect()
                Timber.d("Reconnected. Retrying subscription...")
                return@retry true
            } else {
                return@retry false
            }
        }
    }

    suspend fun <T : Any> convertAndSend(
        headers: String,
        body: T? = null,
        serializer: SerializationStrategy<T>
    ) {
        try {
            getSession().withJsonConversions().convertAndSend(StompSendHeaders(headers), body, serializer)
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Timber.e("Websocket send failed: ${e.message}")
            mutex.withLock { stompSession = null }
            connect()
            getSession().withJsonConversions().convertAndSend(StompSendHeaders(headers), body, serializer)
        }
    }

    suspend inline fun <reified T : Any> convertAndSend(
        headers: String,
        body: T
    ) {
        convertAndSend(headers, body, serializer())
    }
}
