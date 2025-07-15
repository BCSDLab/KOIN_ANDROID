package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class CheckEmailExistsRequest(
    @SerializedName("email") val email: String
)
