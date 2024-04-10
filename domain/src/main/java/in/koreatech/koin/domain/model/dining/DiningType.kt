package `in`.koreatech.koin.domain.model.dining

import `in`.koreatech.koin.domain.constant.BREAKFAST
import `in`.koreatech.koin.domain.constant.DINNER
import `in`.koreatech.koin.domain.constant.LUNCH

enum class DiningType(
    val typeEnglish: String,
    val typeKorean: String
) {
    Breakfast (BREAKFAST, "아침"),
    Lunch (LUNCH, "점심"),
    Dinner (DINNER, "저녁"),
    NextBreakfast (BREAKFAST, "내일 아침");
}