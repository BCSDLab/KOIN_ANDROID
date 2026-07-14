package `in`.koreatech.koin.feature.category

import `in`.koreatech.koin.feature.category.component.CategoryMenuId

sealed class CategorySideEffect {
    data class NavigateToMenu(val id: CategoryMenuId) : CategorySideEffect()
}
