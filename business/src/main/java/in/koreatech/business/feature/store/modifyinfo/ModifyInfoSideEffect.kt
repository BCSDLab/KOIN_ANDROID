package `in`.koreatech.business.feature.store.modifyinfo

sealed class ModifyInfoSideEffect {
    data object NavigateToBackScreen :  ModifyInfoSideEffect()
    data object NavigateToSettingOperatingTime :  ModifyInfoSideEffect()
}
