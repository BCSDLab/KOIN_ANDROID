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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetNotOperationFilteredDiningUseCase @Inject constructor(
    private val diningRepository: DiningRepository,
    private val coopShopRepository: CoopShopRepository
) {
    operator fun invoke(date: String, forceRefresh: Boolean = false): Flow<List<DiningWithOperationTime>> =
        combine(
            diningRepository.getDining(date, forceRefresh),
            coopShopRepository.getCoopShopById(CoopShopType.Dining.id),
            coopShopRepository.getCoopShopById(CoopShopType.NungSu.id)
        ) { dining, diningCoopShop, nungSuCoopShop ->
            map(dining, nungSuCoopShop, diningCoopShop)
        }

    private fun map(diningList: List<Dining>, nungsuCoopShop: CoopShop?, diningCoopShop: CoopShop?): List<DiningWithOperationTime> {
        return diningList.filter { it.place != DiningPlace.Campus2.place && it.menu.first() != "미운영" }.map { dining ->
            val coopShop = if (dining.place == DiningPlace.Nungsu.place) nungsuCoopShop else diningCoopShop
            val dayTypes = getDayTypes(dining.date)
            val koreanType = DiningUtil.getKoreanName(dining.type)
            val timeInfo = dayTypes.firstNotNullOfOrNull { dayType -> coopShop?.opens?.find { it.dayOfWeek == dayType } }?.opensByDayType?.find { it.type == koreanType }

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
                createdAt = dining.createdAt,
                updatedAt = dining.updatedAt,
                soldOutAt = dining.soldOutAt,
                changedAt = dining.changedAt,
                startTime = timeInfo?.openTime.orEmpty(),
                endTime = timeInfo?.closeTime.orEmpty()
            )
        }.sortedBy { diningOrder[it.place] ?: Int.MAX_VALUE }
    }

    private fun getDayTypes(dateString: String): List<CoopShopDayType> {
        val localDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
        return when (localDate.dayOfWeek) {
            DayOfWeek.SATURDAY -> listOf(CoopShopDayType.Saturday, CoopShopDayType.Weekend)
            DayOfWeek.SUNDAY -> listOf(CoopShopDayType.Sunday, CoopShopDayType.Weekend)
            DayOfWeek.FRIDAY -> listOf(CoopShopDayType.Friday, CoopShopDayType.Weekday)
            else -> listOf(CoopShopDayType.Weekday)
        }
    }
}

private val diningOrder = mapOf(
    DiningPlace.CornerA.place to 0,
    DiningPlace.CornerB.place to 1,
    DiningPlace.CornerC.place to 2,
    DiningPlace.Nungsu.place to 3,
    DiningPlace.Campus2.place to 4
)
