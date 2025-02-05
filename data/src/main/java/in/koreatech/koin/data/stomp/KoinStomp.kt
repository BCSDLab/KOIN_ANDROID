package `in`.koreatech.koin.data.stomp

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json.Default.serializersModule
import kotlinx.serialization.serializer
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompReceipt
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import javax.inject.Inject

class KoinStomp @Inject constructor(
    private val baseUrl: String,
    private val authToken: String,
    private val stompClient: StompClient
) {
    lateinit var stompSession: StompSession
    lateinit var jsonStompSession: StompSessionWithKxSerialization

    suspend fun connect() {
        if (!::stompSession.isInitialized) {
            stompSession = stompClient.connect(
                url = "${baseUrl.replaceFirst("https", "wss")}/ws-stomp",
                customStompConnectHeaders = mapOf("Authorization" to "Bearer $authToken")
            )
            jsonStompSession = stompSession.withJsonConversions()
        }
    }

    suspend fun disconnect() {
        stompSession.disconnect()
    }

    suspend fun <T : Any> subscribe(
        destination: String,
        deserializer: DeserializationStrategy<T>,
    ): Flow<T> {
        return jsonStompSession.subscribe(destination, deserializer)
    }

    suspend fun <T : Any> convertAndSend(
        headers: String,
        body: T? = null,
        serializer: SerializationStrategy<T>,
    ): StompReceipt? {
        return jsonStompSession.convertAndSend(StompSendHeaders(headers), body, serializer)
    }

    suspend inline fun <reified T : Any> convertAndSend(
        headers: String,
        body: T
    ): StompReceipt? {
        val serializer = serializersModule.serializer<T>()
        return jsonStompSession.convertAndSend(StompSendHeaders(headers), body, serializer)
    }
}
