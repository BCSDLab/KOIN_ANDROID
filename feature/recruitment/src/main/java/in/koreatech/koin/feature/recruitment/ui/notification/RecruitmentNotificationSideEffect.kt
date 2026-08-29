package `in`.koreatech.koin.feature.recruitment.ui.notification

internal sealed class RecruitmentNotificationSideEffect {
    data object Error : RecruitmentNotificationSideEffect()
    data class NavigateToPost(val postId: Int) : RecruitmentNotificationSideEffect()
    data object Deleted : RecruitmentNotificationSideEffect()
}
