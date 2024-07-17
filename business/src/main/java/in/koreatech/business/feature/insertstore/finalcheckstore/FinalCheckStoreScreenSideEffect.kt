package `in`.koreatech.business.feature.insertstore.finalcheckstore

sealed class FinalCheckStoreScreenSideEffect {
    object GoToFinishScreen: FinalCheckStoreScreenSideEffect()
    object FailRegisterStore: FinalCheckStoreScreenSideEffect()
}