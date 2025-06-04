package `in`.koreatech.koin.feature.club.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.club.R

enum class ClubCategories(val id: Int, @StringRes val stringRes: Int = 0, @DrawableRes val drawableRes: Int = 0) {
    ALL(0),
    ACADEMIC(1, R.string.club_main_widget_category_academic, R.drawable.ic_club_category_academic),
    SPORTS(2, R.string.club_main_widget_category_sports, R.drawable.ic_club_category_sports),
    HOBBY(3, R.string.club_main_widget_category_hobby, R.drawable.ic_club_category_hobby),
    RELIGIOUS(4, R.string.club_main_widget_category_religious, R.drawable.ic_club_category_religious),
    PERFORMANCE(5, R.string.club_main_widget_category_performance, R.drawable.ic_club_category_performance)
}

fun String.toClubCategory() = when (this) {
    "학술" -> ClubCategories.ACADEMIC
    "운동" -> ClubCategories.SPORTS
    "취미" -> ClubCategories.HOBBY
    "종교" -> ClubCategories.RELIGIOUS
    "공연" -> ClubCategories.PERFORMANCE
    else -> ClubCategories.ALL
}

val clubCategories = listOf(
    ClubCategories.PERFORMANCE,
    ClubCategories.SPORTS,
    ClubCategories.RELIGIOUS,
    ClubCategories.HOBBY,
    ClubCategories.ACADEMIC
)
