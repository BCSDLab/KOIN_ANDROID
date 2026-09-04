package `in`.koreatech.koin.feature.recruitment.ui.notification

internal sealed class RecruitmentNotificationSideEffect {
    data object Error : RecruitmentNotificationSideEffect()
    data class NavigateToApplicantManagement(val recruitmentId: Int) : RecruitmentNotificationSideEffect()
    data class NavigateToGroupChat(val recruitmentId: Int, val chatRoomId: Int) : RecruitmentNotificationSideEffect()
    data class NavigateToDirectChat(val recruitmentId: Int, val applicationId: Int) : RecruitmentNotificationSideEffect()
    data object NavigateToMyAppliedRecruitment : RecruitmentNotificationSideEffect()
    data object Deleted : RecruitmentNotificationSideEffect()
}
