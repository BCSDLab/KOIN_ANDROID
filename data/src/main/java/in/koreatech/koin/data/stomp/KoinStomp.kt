package `in`.koreatech.koin.data.stomp

import `in`.koreatech.koin.domain.util.suspendRunCatching
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.SessionDisconnectedException
import org.hildan.krossbow.stomp.StompClient

@OptIn(ExperimentalCoroutinesApi::class)
class KoinStomp(
    baseUrl: String,
    private val tokenProvider: suspend () -> String,
    private val stompClient: StompClient,
    private val json: Json,
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) {
    private val url = "${baseUrl.trimEnd('/').replaceFirst("https:", "wss:").replaceFirst("http:", "ws:")}/ws-stomp"
    private val mutex = Mutex()
    private val current = MutableStateFlow<StompConnection?>(null)

    val connectionState: Flow<KoinStompConnectionState> = current.flatMapLatest { connection ->
        connection?.connectionState ?: flowOf(KoinStompConnectionState.Disconnected)
    }.distinctUntilChanged()

    suspend fun connect(): Result<Unit> = suspendRunCatching {
        val connection = acquireConnection()
        var connected = false
        try {
            connection.awaitConnected()
            connected = true
        } finally {
            if (!connected) releaseConnection(connection)
        }
    }

    suspend fun <T> sendMessage(
        destination: String,
        message: T,
        serializer: SerializationStrategy<T>
    ): Result<Unit> = suspendRunCatching {
        val connection = current.value ?: throw SessionDisconnectedException("STOMP is not connected")
        val payload = json.encodeToString(serializer, message)
        connection.sendMessage(destination, payload)
    }

    suspend fun disconnect(): Result<Unit> = suspendRunCatching {
        mutex.withLock {
            current.value?.close()
            current.value = null
        }
    }

    suspend fun subscribe(destination: String): Flow<String> {
        val connection = current.value ?: throw SessionDisconnectedException("STOMP is not connected")
        return connection.subscribe(destination)
    }

    private suspend fun acquireConnection(): StompConnection = mutex.withLock {
        val token = tokenProvider()
        require(token.isNotBlank()) { "STOMP authentication is required" }
        val previous = current.value
        if (previous != null && previous.token == token && previous.isActive) {
            return@withLock previous
        }
        current.value = null
        previous?.close()
        StompConnection(url, token, stompClient, coroutineContext).also { current.value = it }
    }

    private suspend fun releaseConnection(connection: StompConnection): Unit = withContext(NonCancellable) {
        mutex.withLock {
            if (current.value === connection) {
                connection.close()
                current.value = null
            }
        }
    }
}
