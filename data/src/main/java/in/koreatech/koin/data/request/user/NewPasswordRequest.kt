package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class NewPasswordRequest(
    @SerializedName("new_password")
    val newPassword: String
)
