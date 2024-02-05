package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.domain.error.owner.OwnerError
import `in`.koreatech.koin.domain.error.upload.UploadError
import retrofit2.HttpException

fun HttpException.httpExceptionMapper(): Result<Unit> {
    return when(this.code()) {
        404 -> Result.failure(UploadError.NotExistDomainException)
        409 -> Result.failure(OwnerError.SignupAlreadySentEmailException)
        410 -> Result.failure(OwnerError.OverDueTimeException)
        413 -> Result.failure(UploadError.BoundOfSizeException)
        415 -> Result.failure(UploadError.NotValidFileException)
        422 -> Result.failure(OwnerError.IncorrectVerificationCodeException)
        else -> Result.failure(this)
    }
}