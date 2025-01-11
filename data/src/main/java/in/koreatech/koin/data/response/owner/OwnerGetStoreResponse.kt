package `in`.koreatech.koin.data.response.owner

import com.google.gson.annotations.SerializedName

data class OwnerGetStoreResponse (
    @SerializedName("id") val uid: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("is_event") val  isEvent: Boolean?
)