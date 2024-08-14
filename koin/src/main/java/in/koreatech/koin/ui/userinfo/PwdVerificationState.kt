package `in`.koreatech.koin.ui.userinfo

import `in`.koreatech.koin.common.UiStatus

data class PwdVerificationState(
    val status: UiStatus = UiStatus.Init,
    val isEdited: Boolean
)