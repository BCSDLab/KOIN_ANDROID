package `in`.koreatech.koin.domain.model.store

data class CartPaymentSummary(
    val itemTotalAmount: Int,
    val deliveryFee: Int,
    val totalAmount: Int,
    val finalPaymentAmount: Int
)
