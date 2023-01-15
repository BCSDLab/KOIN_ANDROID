package `in`.koreatech.koin.ui.store.state

import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreCategory

data class StoreActivityState(
    val isLoading: Boolean,
    val category: StoreCategory?,
    val search: String,
    val stores: List<Store>
) {
    companion object {
        val initialState
            get() = StoreActivityState(
                isLoading = true,
                category = null,
                search = "",
                stores = emptyList()
            )
    }
}