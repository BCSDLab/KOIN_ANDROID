package `in`.koreatech.koin.feature.chat.ui

import `in`.koreatech.koin.core.qualifier.IoDispatcher
import `in`.koreatech.koin.domain.usecase.chat.ChatWSConnectUseCase
import `in`.koreatech.koin.domain.usecase.chat.ChatWSDisconnectUseCase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber

@Singleton
internal class SharedChatConnection @Inject constructor(
    private val connect: ChatWSConnectUseCase,
    private val disconnect: ChatWSDisconnectUseCase,
    @IoDispatcher dispatcher: CoroutineDispatcher
) {
    private val scope = CoroutineScope(SupervisorJob() + dispatcher)
    private val mutex = Mutex()
    private val owners = mutableMapOf<Job, Deferred<Result<Unit>>>()
    private val pendingConnections = mutableSetOf<Deferred<Result<Unit>>>()

    suspend fun acquire(owner: Job): Result<Unit> {
        val connection = mutex.withLock {
            owner.ensureActive()
            owners[owner] ?: run {
                pendingConnections.removeAll { it.isCompleted }
                scope.async { connect() }.also {
                    owners[owner] = it
                    pendingConnections.add(it)
                }
            }
        }
        return connection.await()
    }

    suspend fun release(owner: Job): Unit = withContext(NonCancellable) {
        mutex.withLock {
            if (owners.remove(owner) == null) return@withLock
            if (owners.isNotEmpty()) {
                pendingConnections.removeAll { it.isCompleted }
                return@withLock
            }
            pendingConnections.forEach { it.cancel() }
            pendingConnections.forEach { it.join() }
            pendingConnections.clear()
            disconnect().onFailure { Timber.e(it) }
        }
    }
}
