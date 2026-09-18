package `in`.koreatech.koin.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil.request.ErrorResult
import coil.request.ImageRequest
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import `in`.koreatech.koin.core.util.KoinCoilImageLoader
import java.io.IOException

@HiltWorker
class ImageSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val imageUrlProvider: ImageUrlProvider
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result = runCatching {
        val imageLoader = KoinCoilImageLoader.getImageLoader(applicationContext)
        val imageUrls = imageUrlProvider.getImageUrls()

        imageUrls.forEach { imageUrl ->
            val result = imageLoader.execute(
                ImageRequest.Builder(applicationContext)
                    .data(imageUrl)
                    .build()
            )

            if (result is ErrorResult) throw result.throwable
        }
    }.fold(
        onSuccess = { Result.success() },
        onFailure = { error -> if (error is IOException) Result.retry() else Result.failure() }
    )
}
