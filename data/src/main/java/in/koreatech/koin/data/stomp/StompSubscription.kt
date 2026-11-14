package `in`.koreatech.koin.data.stomp

import `in`.koreatech.koin.domain.util.suspendRunCatching
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.hildan.krossbow.stomp.SessionDisconnectedException
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.frame.StompFrame
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders

internal class StompSubscription(private val destination: String) {
    private val frames = Channel<StompFrame.Message>(Channel.BUFFERED)
    private var receiver: Job? = null
    private val subscribed = CompletableDeferred<Unit>()

    val messages: Flow<String> = frames.receiveAsFlow().map { it.bodyAsText }

    suspend fun awaitSubscribed() {
        subscribed.await()
    }

    fun attach(session: StompSession, scope: CoroutineScope) {
        receiver = scope.launch {
            val result = try {
                suspendRunCatching { session.subscribe(StompSubscribeHeaders(destination)) }
            } catch (error: CancellationException) {
                Result.failure(error)
            }
            val messages = result.getOrElse { error ->
                subscribed.completeExceptionally(error)
                frames.close(error)
                return@launch
            }
            subscribed.complete(Unit)
            messages.collect { frames.send(it) }
        }
    }

    fun cancel() {
        receiver?.cancel()
        subscribed.cancel()
        frames.cancel()
    }

    fun close(error: Throwable?) {
        receiver?.cancel()
        subscribed.completeExceptionally(error ?: SessionDisconnectedException("STOMP connection is closed"))
        frames.close(error)
    }
}
