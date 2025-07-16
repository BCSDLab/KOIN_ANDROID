package `in`.koreatech.koin.data.request.user.findpassword

import com.google.gson.annotations.SerializedName

data class IdMatchEmail(
    @SerializedName("login_id") val loginId: String,
    @SerializedName("email") val email: String
)
