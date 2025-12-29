package `in`.koreatech.koin.data.stomp

import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
    private val authToken: String,
    private val stompClient: StompClient
) {
    var stompSession: StompSession? = null

    suspend fun connect(): StompSession {
        Timber.d("Connecting to STOMP...")
        return stompClient.connect(
            url = "${baseUrl.replaceFirst("https", "wss")}/ws-stomp",
            customStompConnectHeaders = mapOf("Authorization" to authToken)
        ).also {
            stompSession = it
            Timber.d("STOMP connected.")
        }
    }

    private suspend fun getSession(): StompSession {
        return stompSession ?: connect()
    }

    suspend fun disconnect() {
        stompSession?.disconnect()
        stompSession = null
    }

    fun <T : Any> subscribe(
        destination: String,
        deserializer: DeserializationStrategy<T>
    ): Flow<T> {
        return flow {
            while (true) {
                try {
                    getSession().withJsonConversions().subscribe(destination, deserializer)
                        .collect { emit(it) }
                } catch (e: WebSocketException) {
                    Timber.d("WebSocketException, reconnecting...")
                    connect()
                    Timber.d("Reconnected. Retrying subscription...")
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    throw e
                }
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
            Timber.e(e)
        }
    }

    suspend inline fun <reified T : Any> convertAndSend(
        headers: String,
        body: T
    ) {
        convertAndSend(headers, body, serializer())
    }
}
