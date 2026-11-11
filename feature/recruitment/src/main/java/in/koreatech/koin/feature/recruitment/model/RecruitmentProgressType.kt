package `in`.koreatech.koin.feature.recruitment.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.recruitment.R

/**
 * [analyticsLabel] 은 화면 표시용이 아니라 분석 이벤트로 전송되는 값이다.
 * 표시 문구([labelRes])가 바뀌어도 지표가 달라지지 않도록 분리해서 유지한다.
 */
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
