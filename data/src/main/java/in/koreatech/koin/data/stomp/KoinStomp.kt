package `in`.koreatech.koin.data.stomp

import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json.Default.serializersModule
import kotlinx.serialization.serializer
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.websocket.WebSocketException
import timber.log.Timber
import javax.inject.Inject

class KoinStomp @Inject constructor(
    private val baseUrl: String,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val stompClient: StompClient
) {
    var stompSession: StompSession? = null
    lateinit var jsonStompSession: StompSessionWithKxSerialization

    suspend fun connect(retry: Boolean) {
        val authToken = tokenLocalDataSource.getAccessToken() ?: throw IllegalStateException("No Auth Token")
        if (stompSession == null || retry) {
            stompSession =
                stompClient.connect(
                    url = "${baseUrl.replaceFirst("https", "wss")}/ws-stomp",
                    customStompConnectHeaders = mapOf("Authorization" to authToken)
                )
            jsonStompSession = stompSession!!.withJsonConversions()
        }
    }

    suspend fun disconnect() {
        stompSession?.disconnect()
        stompSession = null
    }

    fun <T : Any> subscribe(
        destination: String,
        deserializer: DeserializationStrategy<T>
    ): Flow<T> {
        val subscriptionFlow = flow {
            jsonStompSession.subscribe(destination, deserializer)
                .collect { emit(it) }
        }
        return subscriptionFlow.retryWhen { cause, attempt ->
            if (cause is WebSocketException || cause is ClosedReceiveChannelException) {
                Timber.w("WebSocketException: attempting reconnect...")
                disconnect()
                connect(retry = true)
                val delayTime = (1000L * attempt.coerceAtMost(5)).coerceAtMost(16000L)
                delay(delayTime)
                Timber.w("Re-subscribing to $destination")
                return@retryWhen true
            }
            return@retryWhen false
        }
    }

    suspend inline fun <reified T : Any> convertAndSend(
        headers: String,
        body: T
    ) {
        val serializer = serializersModule.serializer<T>()
        jsonStompSession.convertAndSend(StompSendHeaders(headers), body, serializer)
    }
}
