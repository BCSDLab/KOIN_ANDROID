package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class GeneralRequest(
    @SerializedName("gender") val gender: Int?,
    @SerializedName("email") val email: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("phone_number") val phoneNumber: String?
)
