package `in`.koreatech.koin.sync

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KoinSyncScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun schedule(workerClass: Class<out ListenableWorker>, workName: String) {
        val workManager = WorkManager.getInstance(context)

        workManager.enqueueUniqueWork(
            "${workName}_once",
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            oneTimeWorkRequest(workerClass)
        )
        schedulePeriodic(workManager, workerClass, workName)
    }

    fun enqueue(workerClass: Class<out ListenableWorker>, workName: String) {
        WorkManager.getInstance(context).enqueueUniqueWork(
            "${workName}_immediate",
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            oneTimeWorkRequest(workerClass)
        )
    }

    private fun schedulePeriodic(
        workManager: WorkManager,
        workerClass: Class<out ListenableWorker>,
        workName: String
    ) {
        workManager.enqueueUniquePeriodicWork(
            "${workName}_periodic",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest(workerClass)
        )
    }

    private fun oneTimeWorkRequest(workerClass: Class<out ListenableWorker>): OneTimeWorkRequest =
        OneTimeWorkRequest.Builder(workerClass)
            .setConstraints(networkConstraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, BACKOFF_DELAY_MINUTES, TimeUnit.MINUTES)
            .build()

    private fun periodicWorkRequest(workerClass: Class<out ListenableWorker>): PeriodicWorkRequest =
        PeriodicWorkRequest.Builder(workerClass, SYNC_INTERVAL_HOURS, TimeUnit.HOURS)
            .setConstraints(networkConstraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, BACKOFF_DELAY_MINUTES, TimeUnit.MINUTES)
            .build()

    private val networkConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    private companion object {
        const val BACKOFF_DELAY_MINUTES = 10L
        const val SYNC_INTERVAL_HOURS = 6L
    }
}
