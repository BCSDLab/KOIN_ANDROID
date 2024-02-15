package `in`.koreatech.koin.data.response.owner

import com.google.gson.annotations.SerializedName

data class OwnerResponse(
    @SerializedName("attachments") val attachments: List<OwnerAttachmentsResponse>,
    @SerializedName("company_number") val companyNumber: String,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("shops") val shops: List<OwnerShopsResponse>
)
