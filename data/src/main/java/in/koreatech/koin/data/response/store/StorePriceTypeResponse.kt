package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class StorePriceTypeResponse(
    @SerializedName("size") val size: String,
    @SerializedName("price") val price: String
)