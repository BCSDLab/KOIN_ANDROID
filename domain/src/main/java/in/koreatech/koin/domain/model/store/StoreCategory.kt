package `in`.koreatech.koin.domain.model.store

sealed class StoreCategory(val code: Int) {
    object All : StoreCategory(1)
    object Chicken : StoreCategory(2)
    object Pizza : StoreCategory(3)
    object DOSIRAK : StoreCategory(4)
    object PorkFeet : StoreCategory(5)
    object Chinese : StoreCategory(6)

    object NormalFood : StoreCategory(7)
    object Cafe : StoreCategory(8)
    object BeautySalon : StoreCategory(9)
    object Etc : StoreCategory(10)
}
