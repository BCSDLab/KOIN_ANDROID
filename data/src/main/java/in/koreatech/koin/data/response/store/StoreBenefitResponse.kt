package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreBenefitResponse(
    @SerializedName("count") val count: Int?,
    @SerializedName("shops") val shops: List<StoreItemResponse>?
)