package `in`.koreatech.koin.feature.recruitment.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.recruitment.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

enum class RecruitmentStatus(
    @StringRes val labelRes: Int,
    val apiValue: String
) {
    RECRUITING(R.string.recruitment_filter_status_recruiting, "RECRUITING"),
    COMPLETED(R.string.recruitment_filter_status_complete, "CLOSED");

    companion object {
        val ALL: ImmutableList<RecruitmentStatus> = entries.toImmutableList()

        fun from(value: String): RecruitmentStatus =
            entries.firstOrNull { it.apiValue == value } ?: RECRUITING
    }
}

enum class RecruitmentLocation(
    @StringRes val labelRes: Int,
    val apiValue: String
) {
    ONLINE(R.string.recruitment_location_online, "ONLINE"),
    OFFLINE(R.string.recruitment_location_offline, "OFFLINE"),
    MIXED(R.string.recruitment_location_mixed, "MIXED");

    companion object {
        val ALL: ImmutableList<RecruitmentLocation> = entries.toImmutableList()

        fun from(value: String): RecruitmentLocation =
            entries.firstOrNull { it.apiValue == value } ?: ONLINE
    }
}

enum class RecruitmentType(val apiValue: String) {
    GENERAL("GENERAL"),
    ROLE_BASED("ROLE_BASED");

    companion object {
        fun from(value: String): RecruitmentType =
            entries.firstOrNull { it.apiValue == value } ?: GENERAL
    }
}
