package `in`.koreatech.koin.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import `in`.koreatech.koin.domain.error.KoinUnknownErrorException
import `in`.koreatech.koin.domain.usecase.dining.SyncDiningUseCase
import java.io.IOException

@HiltWorker
class DiningSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val syncDiningUseCase: SyncDiningUseCase
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result = syncDiningUseCase().fold(
        onSuccess = { Result.success() },
        onFailure = { error ->
            if (error is IOException || error is KoinUnknownErrorException) Result.retry() else Result.failure()
        }
    )
}
