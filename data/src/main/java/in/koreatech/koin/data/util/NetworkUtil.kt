package `in`.koreatech.koin.data.util

import com.google.gson.Gson
import `in`.koreatech.koin.data.response.ErrorResponse
import retrofit2.HttpException

fun HttpException.getErrorResponse(): ErrorResponse? =
    Gson().fromJson(response()?.errorBody()?.string(), ErrorResponse::class.java)
