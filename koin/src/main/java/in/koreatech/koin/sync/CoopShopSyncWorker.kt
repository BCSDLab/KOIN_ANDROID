package `in`.koreatech.koin.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import `in`.koreatech.koin.domain.usecase.coopshop.SyncCoopShopUseCase
import java.io.IOException
import timber.log.Timber

@HiltWorker
class CoopShopSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val syncCoopShopUseCase: SyncCoopShopUseCase
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result = runCatching {
        syncCoopShopUseCase().getOrThrow()
    }.fold(
        onSuccess = { Result.success() },
        onFailure = {
            Timber.e(it, "Coop shop sync failed")
            if (it is IOException) Result.retry() else Result.failure()
        }
    )
}
