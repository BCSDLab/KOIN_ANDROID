package `in`.koreatech.koin.data.response.owner

import com.google.gson.annotations.SerializedName

data class CheckCompanyNumberResponse(
    @SerializedName("company_number") val companyNumber: String?
)
