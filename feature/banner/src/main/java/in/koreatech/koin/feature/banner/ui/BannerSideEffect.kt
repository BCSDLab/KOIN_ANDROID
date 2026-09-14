package `in`.koreatech.koin.feature.banner.ui

sealed class BannerSideEffect {
    data object Dismiss : BannerSideEffect()
}
