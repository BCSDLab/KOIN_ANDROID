package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class ShopMenuOptionsResponse(
    @SerializedName("option") val option: String?,
    @SerializedName("price") val price: Int?,
)