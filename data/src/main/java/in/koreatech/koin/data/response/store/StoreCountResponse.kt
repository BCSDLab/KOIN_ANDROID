package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreCountResponse(
    @SerializedName("open_count") val openCount: Int,
    @SerializedName("total_count") val totalCount: Int
)
