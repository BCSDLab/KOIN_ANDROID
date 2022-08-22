package `in`.koreatech.koin.ui.splash.state

sealed class TokenState {
    object Valid: TokenState()
    object Invalid: TokenState()
    object NotFound: TokenState()
}
