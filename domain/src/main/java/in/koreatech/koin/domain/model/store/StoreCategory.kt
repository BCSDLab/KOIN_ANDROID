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

fun Int.toStoreCategory() = when (this) {
    1 -> StoreCategory.All
    2 -> StoreCategory.Chicken
    3 -> StoreCategory.Pizza
    4 -> StoreCategory.DOSIRAK
    5 -> StoreCategory.PorkFeet
    6 -> StoreCategory.Chinese
    7 -> StoreCategory.NormalFood
    8 -> StoreCategory.Cafe
    9 -> StoreCategory.BeautySalon
    10 -> StoreCategory.Etc
    else -> null
}