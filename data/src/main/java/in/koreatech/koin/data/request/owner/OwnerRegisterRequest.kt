package `in`.koreatech.koin.data.request.owner

import com.google.gson.annotations.SerializedName

data class OwnerRegisterRequest(
    @SerializedName("attachment_urls") val attachmentUrls: List<AttachmentUrlRequest>,
    @SerializedName("company_number") val companyNumber: String,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("shop_id") val shopId: Int,
    @SerializedName("shop_name") val shopName: String
)