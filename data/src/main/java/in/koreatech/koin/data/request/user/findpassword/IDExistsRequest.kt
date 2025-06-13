package `in`.koreatech.koin.data.request.user.findpassword

import com.google.gson.annotations.SerializedName

data class IDExistsRequest(
    @SerializedName("login_id") val loginId: String
)
