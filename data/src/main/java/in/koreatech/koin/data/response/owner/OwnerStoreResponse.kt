package `in`.koreatech.koin.data.response.owner

import com.google.gson.annotations.SerializedName

data class OwnerStoreResponse(
    @SerializedName("shops") val shops: List<OwnerGetStoreResponse>
)
