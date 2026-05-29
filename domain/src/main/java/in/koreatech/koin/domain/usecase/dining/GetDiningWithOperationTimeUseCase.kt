package `in`.koreatech.koin.domain.usecase.dining

import `in`.koreatech.koin.domain.model.coopshop.CoopShop
import `in`.koreatech.koin.domain.model.coopshop.CoopShopDayType
import `in`.koreatech.koin.domain.model.coopshop.CoopShopType
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningPlace
import `in`.koreatech.koin.domain.model.dining.DiningWithOperationTime
import `in`.koreatech.koin.domain.repository.CoopShopRepository
import `in`.koreatech.koin.domain.repository.DiningRepository
import `in`.koreatech.koin.domain.util.DiningUtil
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetDiningWithOperationTimeUseCase @Inject constructor(
    private val diningRepository: DiningRepository,
    private val coopShopRepository: CoopShopRepository
) {
    suspend operator fun invoke(date: String): Result<List<DiningWithOperationTime>> = runCatching {
        coroutineScope {
            val diningList = async { diningRepository.getDining(date) }
            val diningCoopShop = async { coopShopRepository.getCoopShopById(CoopShopType.Dining.id).getOrThrow() }
            val nungsuCoopShop = async { coopShopRepository.getCoopShopById(CoopShopType.NungSu.id).getOrThrow() }

            map(diningList.await(), nungsuCoopShop.await(), diningCoopShop.await())
        }
    }

    private fun map(diningList: List<Dining>, nungsuCoopShop: CoopShop?, diningCoopShop: CoopShop?): List<DiningWithOperationTime> {
        return diningList.filter { it.place != DiningPlace.Campus2.place }.map { dining ->
            val coopShop = if (dining.place == DiningPlace.Nungsu.place) nungsuCoopShop else diningCoopShop
            val dayType = getDayType(dining.date)
            val koreanType = DiningUtil.getKoreanName(dining.type)
            val timeInfo = coopShop?.opens?.find { it.dayOfWeek == dayType }?.opensByDayType?.find { it.type == koreanType }

            DiningWithOperationTime(
                id = dining.id,
                date = dining.date,
                type = dining.type,
                place = dining.place,
                priceCard = dining.priceCard,
                priceCash = dining.priceCash,
                kcal = dining.kcal,
                menu = dining.menu,
                imageUrl = dining.imageUrl,
                soldOutAt = dining.soldOutAt,
                startTime = timeInfo?.openTime.orEmpty(),
                endTime = timeInfo?.closeTime.orEmpty()
            )
        }
    }

    private fun getDayType(dateString: String): CoopShopDayType {
        val localDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
        return when (localDate.dayOfWeek) {
            DayOfWeek.SATURDAY -> CoopShopDayType.Saturday
            DayOfWeek.SUNDAY   -> CoopShopDayType.Weekend
            else               -> CoopShopDayType.Weekday
        }
    }
}
