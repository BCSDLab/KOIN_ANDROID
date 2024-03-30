package `in`.koreatech.koin.domain.error.upload

sealed class UploadError {
    object BoundOfSizeException: IllegalAccessException()
    object NotAllowedDomainException: IllegalAccessException()
    object NotExistDomainException: IllegalAccessException()
    object NotValidFileException: IllegalAccessException()
}