package `in`.koreatech.koin.feature.login.ui

import `in`.koreatech.koin.feature.login.ui.component.UiStatus

data class LoginState(
    val status: UiStatus = UiStatus.Init
)
