package `in`.koreatech.koin.domain.error.owner

sealed class OwnerError {
    object SignupAlreadySentEmailException: IllegalAccessException()
    object OverDueTimeException: IllegalAccessException()
    object IncorrectVerificationCodeException: IllegalAccessException()
    object NotValidEmailException: IllegalAccessException()
    object AlreadyUsingEmailException: IllegalAccessException()
    object AlreadyUsingRegistrationNumberException: IllegalAccessException()
    object AlreadyValidIdException: IllegalAccessException()

    object IncorrectParaMeter: IllegalAccessException()
    object NotValidPhoneNumberException: IllegalAccessException()
    object NotExistsPhoneNumberException: IllegalAccessException()
    object ExistsPhoneNumberException: IllegalAccessException()

}