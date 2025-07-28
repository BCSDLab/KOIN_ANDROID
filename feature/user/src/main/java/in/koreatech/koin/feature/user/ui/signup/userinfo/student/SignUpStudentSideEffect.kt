package `in`.koreatech.koin.feature.user.ui.signup.userinfo.student

sealed class SignUpStudentSideEffect {
    data object SignUpSuccess : SignUpStudentSideEffect()
    data object SignUpFailure : SignUpStudentSideEffect()
}
