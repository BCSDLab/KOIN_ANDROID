package `in`.koreatech.koin.data.response.owner

import com.google.gson.annotations.SerializedName

data class OwnerAttachmentsResponse(
    @SerializedName("file_name") val fileName: String,
    @SerializedName("file_url") val fileUrl: String,
    @SerializedName("id") val id: Int
)
