package `in`.koreatech.koin.domain.model.store

data class StoreWithMenu(
    val store: Store,
    val storeMenus: List<StoreMenu>
)