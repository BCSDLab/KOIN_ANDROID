package `in`.koreatech.koin.feature.land.ui.detail

internal sealed interface LandDetailSideEffect {
    data object NavigateBack : LandDetailSideEffect
    data class CallPhone(val phone: String) : LandDetailSideEffect
}
