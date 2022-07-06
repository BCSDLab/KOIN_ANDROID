package `in`.koreatech.koin.data.response

import com.google.gson.annotations.SerializedName

data class VersionResponse(
    val id: Int,
    val version: String,
    val type: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)
