package `in`.koreatech.koin.domain.error.LoginError

sealed class LoginError {
    object IncorrectIdPwError : IllegalAccessException()

    object NetworkError : IllegalAccessException()

    object UnknownError : IllegalAccessException()
}
