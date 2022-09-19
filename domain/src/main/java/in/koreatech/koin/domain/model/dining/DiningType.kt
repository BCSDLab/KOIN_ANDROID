package `in`.koreatech.koin.domain.model.dining

sealed class DiningType(
    val typeCode: Int,
    val typeEnglish: String,
    val typeKorean: String
) {
    object Breakfast : DiningType(0, "BREAKFAST", "아침")
    object Lunch : DiningType(1, "LUNCH", "점심")
    object Dinner : DiningType(2, "DINNER", "저녁")
}
