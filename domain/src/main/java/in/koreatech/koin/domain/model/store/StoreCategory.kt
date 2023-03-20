package `in`.koreatech.koin.domain.model.store

sealed class StoreCategory(val code: String) {
    object Etc : StoreCategory("S000")
    object Callvan : StoreCategory("S001")
    object Jeongsik : StoreCategory("S002")
    object PorkFeet : StoreCategory("S003")
    object Chinese : StoreCategory("S004")
    object Chicken : StoreCategory("S005")
    object Pizza : StoreCategory("S006")
    object Tangsuyuk : StoreCategory("S007")
    object NormalFood : StoreCategory("S008")
    object BeautySalon : StoreCategory("S009")
    object Cafe : StoreCategory("S010")
    object Unknown : StoreCategory("unknown")
}

fun String.toStoreCategory() = when (this) {
    "S000" -> StoreCategory.Etc
    "S001" -> StoreCategory.Callvan
    "S002" -> StoreCategory.Jeongsik
    "S003" -> StoreCategory.PorkFeet
    "S004" -> StoreCategory.Chinese
    "S005" -> StoreCategory.Chicken
    "S006" -> StoreCategory.Pizza
    "S007" -> StoreCategory.Tangsuyuk
    "S008" -> StoreCategory.NormalFood
    "S009" -> StoreCategory.BeautySalon
    "S010" -> StoreCategory.Cafe
    else -> null
}