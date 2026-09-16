package `in`.koreatech.koin.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import `in`.koreatech.koin.domain.error.KoinUnknownErrorException
import `in`.koreatech.koin.domain.error.weather.KoinWeatherException
import `in`.koreatech.koin.domain.usecase.home.SyncHomeUseCase
import java.io.IOException

@HiltWorker
class HomeSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val syncHomeUseCase: SyncHomeUseCase
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        return syncHomeUseCase().fold(
            onSuccess = { Result.success() },
            onFailure = { error ->
                when (error) {
                    is IOException,
                    is KoinUnknownErrorException,
                    is KoinWeatherException.ExternalApiErrorException -> Result.retry()
                    else -> Result.failure()
                }
            }
        )
    }
}
