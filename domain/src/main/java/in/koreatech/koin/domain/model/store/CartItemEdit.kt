package `in`.koreatech.koin.domain.model.store

data class CartItemEdit(
    val id: Int,
    val name: String,
    val description: String,
    val images: List<String>,
    val prices: List<CartItemEditPrice>,
    val optionGroups: List<CartItemEditOptionGroup>
) {
    data class CartItemEditPrice(
        val id: Int,
        val name: String?,
        val price: Int,
        val isSelected: Boolean
    )

    data class CartItemEditOptionGroup(
        val id: Int,
        val name: String,
        val description: String,
        val isRequired: Boolean,
        val minSelect: Int,
        val maxSelect: Int,
        val options: List<CartItemEditPrice>
    )
}
