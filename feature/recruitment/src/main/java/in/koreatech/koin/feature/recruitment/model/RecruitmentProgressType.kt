package `in`.koreatech.koin.feature.recruitment.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.recruitment.R

/** [analyticsLabel] 은 이벤트 로깅용 값이다. */
enum class RecruitmentProgressType(
    @StringRes val labelRes: Int,
    val apiValue: String,
    val analyticsLabel: String
) {
    ONLINE(R.string.recruitment_location_online, "ONLINE", "온라인"),
    OFFLINE(R.string.recruitment_location_offline, "OFFLINE", "오프라인"),
    HYBRID(R.string.recruitment_location_mixed, "MIXED", "온·오프라인");

    companion object {
        fun from(value: String): RecruitmentProgressType =
            entries.firstOrNull { it.apiValue == value } ?: ONLINE
    }
}
