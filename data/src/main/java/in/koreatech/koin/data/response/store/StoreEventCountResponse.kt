package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreEventCountResponse(
    @SerializedName("count") val count: Int
)
