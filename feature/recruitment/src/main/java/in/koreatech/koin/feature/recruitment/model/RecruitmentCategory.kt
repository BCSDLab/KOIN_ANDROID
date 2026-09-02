package `in`.koreatech.koin.feature.recruitment.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.recruitment.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

enum class RecruitmentCategory(
    @StringRes val labelRes: Int,
    val apiValue: String
) {
    CONTEST(R.string.recruitment_category_contest, "CONTEST"),
    EXTERNAL_ACTIVITY(R.string.recruitment_category_activity, "EXTERNAL_ACTIVITY"),
    STUDY(R.string.recruitment_category_study, "STUDY"),
    PROJECT(R.string.recruitment_category_project, "PROJECT"),
    ETC(R.string.recruitment_category_etc, "OTHER");

    companion object {
        val ALL: ImmutableList<RecruitmentCategory> = entries.toImmutableList()

        fun from(value: String): RecruitmentCategory =
            entries.firstOrNull { it.apiValue == value } ?: ETC
    }
}

fun String.toRecruitmentCategory(): RecruitmentCategory =
    RecruitmentCategory.entries.firstOrNull { it.name == this } ?: RecruitmentCategory.ETC

fun String.toRecruitmentLocation(): String = when (this) {
    "ONLINE" -> "온라인"
    "OFFLINE" -> "오프라인"
    else -> "온·오프라인"
}

fun String.toDateRange(endDate: String): String =
    "${replace("-", ".")} ~ ${endDate.replace("-", ".")}"
