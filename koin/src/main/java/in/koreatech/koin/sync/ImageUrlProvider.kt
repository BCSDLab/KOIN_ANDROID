package `in`.koreatech.koin.sync

import `in`.koreatech.koin.domain.usecase.dining.GetDiningUseCase
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first

class ImageUrlProvider @Inject constructor(
    private val getDiningUseCase: GetDiningUseCase
) {
    suspend fun getImageUrls(): List<String> {
        return coroutineScope {
            val diningImages = async {
                getDiningUseCase(TimeUtil.dateFormatToYYMMDD(DiningUtil.getCurrentDate()))
                    .first()
                    .map { it.imageUrl }
                    .filter { it.isNotBlank() }
            }

            buildList {
                addAll(diningImages.await())
            }
        }
    }
}
