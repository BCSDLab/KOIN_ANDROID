package `in`.koreatech.koin.data.util

import com.google.gson.Gson
import `in`.koreatech.koin.data.response.ErrorResponse
import okhttp3.ResponseBody

fun ResponseBody.getErrorResponse(): ErrorResponse {
    val errorResponse = Gson().fromJson(this.string(), ErrorResponse::class.java)
    return errorResponse
}
