package `in`.koreatech.koin.data.response.upload

import com.google.gson.annotations.SerializedName

data class UploadUrlResponse(
    @SerializedName("expiration_date") val expirationDate: String,
    @SerializedName("file_url") val fileUrl: String,
    @SerializedName("pre_signed_url") val preSignedUrl: String
)
