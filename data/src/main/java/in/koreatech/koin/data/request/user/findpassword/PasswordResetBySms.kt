package `in`.koreatech.koin.data.request.user.findpassword

import com.google.gson.annotations.SerializedName

data class PasswordResetBySms(
    @SerializedName("login_id") val loginId: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("new_password") val newPassword: String
)
