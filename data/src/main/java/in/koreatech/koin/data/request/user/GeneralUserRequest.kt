package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class GeneralUserRequest(
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("phone_number") val phoneNumber: String?,
    @SerializedName("gender") val gender: Int?,
    @SerializedName("password") val hashedPassword: String?,
    @SerializedName("email") val email: String?
)
