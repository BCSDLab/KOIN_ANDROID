package `in`.koreatech.koin.data.util

import `in`.koreatech.koin.data.response.ErrorResponse
import kotlinx.serialization.json.Json
import retrofit2.HttpException

fun HttpException.getErrorResponse(): ErrorResponse {
    return Json.decodeFromString<ErrorResponse>(response()?.errorBody()?.string() ?: "")
}