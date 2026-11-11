package `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate.model

import androidx.annotation.StringRes
import `in`.koreatech.koin.feature.recruitment.R

/**
 * [analyticsLabel] 은 화면 표시용이 아니라 분석 이벤트로 전송되는 값이다.
 * 표시 문구([labelRes])가 바뀌어도 지표가 달라지지 않도록 분리해서 유지한다.
 */
enum class TeamRecruitmentCategory(
    @StringRes val labelRes: Int,
    val analyticsLabel: String
) {
    CONTEST(R.string.recruitment_category_contest, "공모전"),
    EXTERNAL_ACTIVITY(R.string.recruitment_category_activity, "대외활동"),
    STUDY(R.string.recruitment_category_study, "스터디"),
    PROJECT(R.string.recruitment_category_project, "프로젝트"),
    OTHER(R.string.recruitment_category_etc, "기타")
}
