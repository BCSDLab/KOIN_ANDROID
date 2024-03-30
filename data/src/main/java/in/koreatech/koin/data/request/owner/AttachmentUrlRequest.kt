package `in`.koreatech.koin.data.request.owner

import com.google.gson.annotations.SerializedName

data class AttachmentUrlRequest(
    @SerializedName("file_url") val fileUrl: String
)
