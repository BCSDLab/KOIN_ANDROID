package `in`.koreatech.koin.domain.error

data class KoinUnknownErrorException(
    val code: String?,
    val errorMessage: String?,
    val errorTraceId: String?
) : KoinErrorException()
