package `in`.koreatech.koin.data.response.cart

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.cart.CartValidate

data class CartValidateResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("errorTraceId")
    val errorTraceId: String,
) {
    fun toCartValidate() =
        CartValidate(
            code = code,
            message = message,
            errorTraceId = errorTraceId
        )
}
