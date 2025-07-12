package `in`.koreatech.koin.domain.model.ordershop

data class OrderMenuList (
    val menuGroupId: Int,
    val menuGroupName: String,
    val menus: List<OrderMenu>
)