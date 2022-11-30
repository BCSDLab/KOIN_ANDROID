package `in`.koreatech.koin.data.response

import com.google.gson.annotations.SerializedName

data class VersionResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("version") val version: String,
    @SerializedName("type") val type: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)
