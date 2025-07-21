package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class GeneralInfoRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("login_id")
    val loginId: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("email")
    val email: String?,
    @SerializedName("nickname")
    val nickname: String?
)
