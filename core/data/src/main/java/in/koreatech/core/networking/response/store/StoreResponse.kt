package `in`.koreatech.core.networking.response.store

import com.google.gson.annotations.SerializedName
import `in`.koreatech.core.networking.response.store.StoreItemResponse

data class StoreResponse(
    @SerializedName("shops") val shops: List<StoreItemResponse>
)
