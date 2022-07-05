package `in`.koreatech.koin.data.response

import com.google.gson.annotations.SerializedName

data class DefaultResponse(
    val success: Boolean,
    val message: String,
    val error: String,
    val code: String,
    @SerializedName("grantEdit") val isGrantEdit: Boolean
)
