package `in`.koreatech.koin.domain.error

open class KoinErrorException : IllegalAccessException() {
    override var message: String? = null
}
