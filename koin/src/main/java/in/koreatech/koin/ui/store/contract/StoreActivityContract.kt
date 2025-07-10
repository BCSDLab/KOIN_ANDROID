package `in`.koreatech.koin.ui.store.contract

class StoreActivityContract() {
    companion object {
        const val STORE_CATEGORY = "STORE_CATEGORY"
        const val CATEGORY_POSITION = "CATEGORY_POSITION"
        val MIN_ORDER_OPTIONS = listOf("5,000", "10,000", "15,000", "20,000", "전체")
    }
}
