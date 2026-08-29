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
