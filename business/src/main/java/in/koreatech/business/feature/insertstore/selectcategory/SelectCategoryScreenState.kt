package `in`.koreatech.business.feature.insertstore.selectcategory

import `in`.koreatech.koin.domain.model.store.StoreCategories

data class SelectCategoryScreenState(
    val categories: List<StoreCategories> = emptyList(),
    val categoryId: Int = -1,
    val categoryIdIsValid: Boolean = false
)