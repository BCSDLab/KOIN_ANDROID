package `in`.koreatech.koin.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("message")
    val message: String?,
    @SerialName("code")
    val code: String?,
    @SerialName("errorTraceId")
    val errorTraceId: String?
)
