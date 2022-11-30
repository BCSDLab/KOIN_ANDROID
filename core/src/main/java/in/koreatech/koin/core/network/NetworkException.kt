package `in`.koreatech.koin.core.network

import java.io.IOException

class NetworkException(
    private val htmlErrorCode: Int
) : IOException("Response get error with code: $htmlErrorCode") {
}