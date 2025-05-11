package `in`.koreatech.koin.feature.signup.ui.userinfo.student

sealed class SignUpStudentSideEffect {
    data object SignUpSuccess : SignUpStudentSideEffect()
    data object SignUpFailure : SignUpStudentSideEffect()
}
