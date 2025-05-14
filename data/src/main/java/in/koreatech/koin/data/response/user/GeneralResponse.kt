package `in`.koreatech.koin.data.response.user

import com.google.gson.annotations.SerializedName

data class GeneralResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("login_id") val userId: String,
    @SerializedName("gender") val gender: Int?,
    @SerializedName("email") val email: String?,
    @SerializedName("name") val name: String,
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("user_type") val userType: String
)
