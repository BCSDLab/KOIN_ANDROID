package `in`.koreatech.koin.domain.model.dining

import `in`.koreatech.koin.domain.constant.BREAKFAST
import `in`.koreatech.koin.domain.constant.DINNER
import `in`.koreatech.koin.domain.constant.LUNCH

sealed class DiningType(
    val typeCode: Int,
    val typeEnglish: String,
    val typeKorean: String
) : Comparable<DiningType> {
    object Breakfast : DiningType(0, BREAKFAST, "아침")
    object Lunch : DiningType(1, LUNCH, "점심")
    object Dinner : DiningType(2, DINNER, "저녁")

    override fun compareTo(other: DiningType): Int {
        return typeCode.compareTo(other.typeCode)
    }
}

fun String.toDiningType() = when(this) {
    BREAKFAST -> DiningType.Breakfast
    LUNCH -> DiningType.Lunch
    DINNER -> DiningType.Dinner
    else -> throw IllegalArgumentException("Invalid dining type string")
}