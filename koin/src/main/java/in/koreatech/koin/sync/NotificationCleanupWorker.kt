package `in`.koreatech.koin.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import `in`.koreatech.koin.domain.repository.NotificationRepository
import java.time.LocalDateTime

@HiltWorker
class NotificationCleanupWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val notificationRepository: NotificationRepository
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        return notificationRepository.deleteOldNotificationsFromLocal(LocalDateTime.now().minusDays(RETENTION_DAYS))
            .fold(
                onSuccess = { Result.success() },
                onFailure = { Result.retry() }
            )
    }

    private companion object {
        const val RETENTION_DAYS = 14L
    }
}
