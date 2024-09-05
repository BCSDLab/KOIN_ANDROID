package `in`.koreatech.koin.domain.model.owner.menu

data class StoreMenuInfo(
    val shopId: Int,
    val name: String,
    val isSingle: Boolean,
    val singlePrice: Int,
    val optionPrice: List<StoreMenuOptionPrice>,
    val description: String,
    val categoryIds: List<Int>,
    val imageUrl: List<String>
)