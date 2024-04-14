package `in`.koreatech.koin.ui.login

import `in`.koreatech.koin.common.UiStatus

data class LoginState(
    val status: UiStatus = UiStatus.Init,
)