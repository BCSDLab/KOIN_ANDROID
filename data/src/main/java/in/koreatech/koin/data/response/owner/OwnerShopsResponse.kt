package `in`.koreatech.koin.data.response.owner

import com.google.gson.annotations.SerializedName

data class OwnerShopsResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
