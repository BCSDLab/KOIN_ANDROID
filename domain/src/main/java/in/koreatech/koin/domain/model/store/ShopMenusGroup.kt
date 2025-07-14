package `in`.koreatech.koin.domain.model.store

data class ShopMenusGroup(
    val count: Int,
    val menuGroups: List<ShopMenuGroup>
) {
    data class ShopMenuGroup(
        val id: Int,
        val name: String
    )
}
