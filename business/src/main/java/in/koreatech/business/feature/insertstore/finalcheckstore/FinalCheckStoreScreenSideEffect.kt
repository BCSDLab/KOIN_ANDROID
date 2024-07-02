package `in`.koreatech.business.feature.insertstore.finalcheckstore

import `in`.koreatech.business.feature.insertstore.selectcategory.SelectCategoryScreenSideEffect

sealed class FinalCheckStoreScreenSideEffect {
    object GoToFinishScreen: FinalCheckStoreScreenSideEffect()
    object FailRegisterStore: FinalCheckStoreScreenSideEffect()
}