package `in`.koreatech.koin.domain.model.ordershop

data class OrderMenu(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnailImage: String,
    val isSoldOut: Boolean,
    val prices: List<Price>
) {
    data class Price(
        val id: Int,
        val name: String?, // null인 경우 단일메뉴
        val price: Int
    )
}
