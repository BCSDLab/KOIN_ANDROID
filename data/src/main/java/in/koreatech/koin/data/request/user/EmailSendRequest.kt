package `in`.koreatech.koin.data.request.user

import com.google.gson.annotations.SerializedName

data class EmailSendRequest(
    @SerializedName("email")
    val email: String
)
