package `in`.koreatech.koin.data.request.recruitment

import com.google.gson.annotations.SerializedName

data class UpdateApplicationStatusRequest(
    @SerializedName("status") val status: String
)
