package `in`.koreatech.koin.feature.recruitment.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.recruitment.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

enum class RecruitmentCategory(
    @StringRes val labelRes: Int
) {
    CONTEST(R.string.recruitment_category_contest),
    EXTERNAL_ACTIVITY(R.string.recruitment_category_activity),
    STUDY(R.string.recruitment_category_study),
    PROJECT(R.string.recruitment_category_project),
    ETC(R.string.recruitment_category_etc);

    companion object {
        val ALL: ImmutableList<RecruitmentCategory> = entries.toImmutableList()
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
