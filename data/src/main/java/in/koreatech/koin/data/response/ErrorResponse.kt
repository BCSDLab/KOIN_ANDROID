package `in`.koreatech.koin.data.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String?,
    val code: String?,
    val errorTraceId: String?
)