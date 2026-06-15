package `in`.koreatech.koin.domain.error.weather

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinWeatherException : KoinErrorException() {
    /*
     * Exceptions for 500
     */
    class ExternalApiErrorException : KoinWeatherException()
}
