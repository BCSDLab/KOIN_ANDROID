package `in`.koreatech.koin.feature.signup.ui.term

sealed class SignUpTermSideEffect {
    data object FailedToFetchTerm : SignUpTermSideEffect()
}
