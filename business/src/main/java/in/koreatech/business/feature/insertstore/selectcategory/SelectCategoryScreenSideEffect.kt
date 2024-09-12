package `in`.koreatech.business.feature.insertstore.selectcategory


sealed class SelectCategoryScreenSideEffect {
    data class NavigateToInsertBasicInfoScreen(val categoryId: Int): SelectCategoryScreenSideEffect()
    object NotSelectCategory: SelectCategoryScreenSideEffect()
}