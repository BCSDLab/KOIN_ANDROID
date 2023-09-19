package `in`.koreatech.koin.data.request.owner

import com.google.gson.annotations.SerializedName

data class OwnerRequest(
    @SerializedName("attachment_url") val attachmentsUrl: String?,
    @SerializedName("company_number") val companyNumber: String,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String?,
    @SerializedName("password") val password: String,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("shop_id") val shopId: String,
    @SerializedName("shop_name") val shopName: String
)
