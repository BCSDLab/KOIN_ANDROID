package `in`.koreatech.koin.data.stomp

import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.hildan.krossbow.stomp.ConnectionTimeout
import org.hildan.krossbow.stomp.LostReceiptException
import org.hildan.krossbow.stomp.MissingHeartBeatException
import org.hildan.krossbow.stomp.SessionDisconnectedException
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.WebSocketClosedUnexpectedly
import org.hildan.krossbow.stomp.frame.FrameBody
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.websocket.WebSocketConnectionException
import org.hildan.krossbow.websocket.WebSocketException
import timber.log.Timber

internal class StompConnection(
    private val url: String,
    val token: String,
    private val client: StompClient,
    coroutineContext: CoroutineContext
) {
    private val lifetime = SupervisorJob()
    private val lock = Any()
    private var subscriptions: MutableSet<StompSubscription>? = mutableSetOf()
    private var activeSession: ActiveSession? = null
    private val ended = CompletableDeferred<Throwable?>()
    private val status = MutableStateFlow<KoinStompConnectionState>(KoinStompConnectionState.Connecting)
    private val worker = CoroutineScope(coroutineContext + lifetime).launch {
        try {
            reconnect()
        } finally {
            finish(null)
        }
    }

    val connectionState: Flow<KoinStompConnectionState> = status.asStateFlow()
    val isActive: Boolean get() = worker.isActive

    suspend fun awaitConnected() {
        val current = status.first {
            it == KoinStompConnectionState.Connected || it == KoinStompConnectionState.Disconnected
        }
        if (current == KoinStompConnectionState.Disconnected) {
            throw ended.await() ?: SessionDisconnectedException("STOMP connection is closed")
        }
    }

    suspend fun sendMessage(destination: String, payload: String) {
        val session = synchronized(lock) {
            activeSession?.session ?: throw SessionDisconnectedException("STOMP is not connected")
        }
        session.send(StompSendHeaders(destination) { contentType = "application/json" }, FrameBody.Text(payload))
    }

    suspend fun subscribe(destination: String): Flow<String> {
        val subscription = StompSubscription(destination)
        val registered = synchronized(lock) {
            if (ended.isCompleted) {
                false
            } else {
                subscriptions?.add(subscription)
                activeSession?.let { subscription.attach(it.session, it.scope) }
                true
            }
        }
        if (!registered) throw ended.await() ?: SessionDisconnectedException("STOMP connection is closed")
        val cleanup = currentCoroutineContext().job.invokeOnCompletion { release(subscription) }
        try {
            subscription.awaitSubscribed()
            return flow {
                try {
                    emitAll(subscription.messages)
                } finally {
                    cleanup.dispose()
                    release(subscription)
                }
            }
        } catch (error: Throwable) {
            cleanup.dispose()
            release(subscription)
            throw error
        }
    }

    private fun release(subscription: StompSubscription) {
        synchronized(lock) { subscriptions?.remove(subscription) }
        subscription.cancel()
    }

    suspend fun close(): Unit = withContext(NonCancellable) {
        lifetime.cancelAndJoin()
        finish(null)
    }

    private suspend fun reconnect() {
        var attempt = 0
        var retryDelay = 1.seconds
        while (true) {
            val error = runSession()
            if (status.value == KoinStompConnectionState.Connecting || !error.isRecoverable()) {
                finish(error)
                return
            }
            if (status.value == KoinStompConnectionState.Connected) {
                attempt = 0
                retryDelay = 1.seconds
            }
            status.value = KoinStompConnectionState.Reconnecting(++attempt)
            delay(retryDelay)
            retryDelay = (retryDelay * 2).coerceAtMost(30.seconds)
        }
    }

    private suspend fun runSession(): Throwable = try {
        coroutineScope {
            val session = client.connect(
                url = url,
                customStompConnectHeaders = mapOf("Authorization" to "Bearer $token"),
                sessionCoroutineContext = currentCoroutineContext()
            )
            try {
                synchronized(lock) {
                    activeSession = ActiveSession(session, this)
                    subscriptions?.forEach { it.attach(session, this) }
                    status.value = KoinStompConnectionState.Connected
                }
                awaitCancellation()
            } finally {
                synchronized(lock) { activeSession = null }
                withContext(NonCancellable) {
                    try {
                        session.disconnect()
                    } catch (error: Exception) {
                        Timber.d(error, "STOMP session already closed")
                    }
                }
            }
        }
    } catch (error: Exception) {
        currentCoroutineContext().ensureActive()
        error.connectionFailure()
    }

    private fun finish(error: Throwable?) {
        val closing = synchronized(lock) {
            if (!ended.complete(error)) return
            // Closing a channel can immediately re-enter a collector's cleanup.
            subscriptions.also {
                subscriptions = null
                status.value = KoinStompConnectionState.Disconnected
            }
        }
        closing?.forEach { it.close(error) }
    }

    private class ActiveSession(val session: StompSession, val scope: CoroutineScope)

    private fun Throwable.connectionFailure(): Throwable {
        var failure = this
        while (failure is CancellationException) {
            failure = failure.cause ?: return SessionDisconnectedException("STOMP session ended")
        }
        return failure
    }

    private fun Throwable.isRecoverable(): Boolean {
        val statusCode = (this as? WebSocketConnectionException)?.httpStatusCode
        if (statusCode != null && statusCode in 400..499) return false
        return this is WebSocketException || this is WebSocketClosedUnexpectedly ||
            this is MissingHeartBeatException || this is SessionDisconnectedException ||
            this is ConnectionTimeout || this is LostReceiptException
    }
}
