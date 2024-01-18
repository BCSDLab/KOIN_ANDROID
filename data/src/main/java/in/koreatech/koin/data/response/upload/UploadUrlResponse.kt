package `in`.koreatech.koin.data.response.upload

import com.google.gson.annotations.SerializedName

data class UploadUrlResponse(
    @SerializedName("expiration_date") val expiration_date: String,
    @SerializedName("file_rul") val file_url: String,
    @SerializedName("pre_signed_url") val pre_signed_url: String
)
