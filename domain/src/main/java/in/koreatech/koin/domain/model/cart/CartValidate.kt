package `in`.koreatech.koin.domain.model.cart

data class CartValidate (
    val code: String,
    val message: String,
    val errorTraceId: String,
){
    companion object {
        val Empty = CartValidate(
            code = "",
            message = "",
            errorTraceId = ""
        )
    }
}
