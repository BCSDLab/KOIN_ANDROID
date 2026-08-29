package `in`.koreatech.koin.feature.recruitment.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.recruitment.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

enum class RecruitmentStatus(
    @StringRes val labelRes: Int
) {
    RECRUITING(R.string.recruitment_filter_status_recruiting),
    COMPLETED(R.string.recruitment_filter_status_completed);

    companion object {
        val ALL: ImmutableList<RecruitmentStatus> = entries.toImmutableList()
    }
}

enum class RecruitmentLocation(
    @StringRes val labelRes: Int
) {
    ONLINE(R.string.recruitment_location_online),
    OFFLINE(R.string.recruitment_location_offline),
    MIXED(R.string.recruitment_location_mixed);

    companion object {
        val ALL: ImmutableList<RecruitmentLocation> = entries.toImmutableList()
    }
}
