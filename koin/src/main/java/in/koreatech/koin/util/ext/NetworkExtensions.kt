package `in`.koreatech.koin.util.ext

import okhttp3.Request

inline fun Request.newRequest(block: Request.Builder.() -> Request.Builder): Request {
    return this.newBuilder().block().build()
}

fun Request.Builder.putAccessToken(
    accessToken: String,
    name: String = "Authorization",
    prefix: String = "Bearer "
): Request.Builder {
    return addHeader(name, "$prefix$accessToken")
}