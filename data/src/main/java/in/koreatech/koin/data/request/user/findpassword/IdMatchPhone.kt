package `in`.koreatech.koin.data.request.user.findpassword

import com.google.gson.annotations.SerializedName

data class IdMatchPhone(
    @SerializedName("login_id") val loginId: String,
    @SerializedName("phone_number") val phoneNumber: String
)
