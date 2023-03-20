package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreResponse(
    @SerializedName("shops") val shops: List<StoreItemResponse>
)
