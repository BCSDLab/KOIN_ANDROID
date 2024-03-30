package `in`.koreatech.koin.data.request.upload

import com.google.gson.annotations.SerializedName

data class UploadUrlRequest(
    @SerializedName("content_length") val contentLength: Long,
    @SerializedName("content_type") val contentType: String,
    @SerializedName("file_name") val fileName: String
)