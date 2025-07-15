package `in`.koreatech.koin.feature.user.ui.signup.term

sealed class SignUpTermSideEffect {
    data object FailedToFetchTerm : SignUpTermSideEffect()
}
