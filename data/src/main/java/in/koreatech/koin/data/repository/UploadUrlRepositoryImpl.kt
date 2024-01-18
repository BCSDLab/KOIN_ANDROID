package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.request.upload.UploadUrlRequest
import `in`.koreatech.koin.data.source.remote.UploadUrlRemoteDataSource
import `in`.koreatech.koin.domain.error.upload.BoundOfSizeException
import `in`.koreatech.koin.domain.error.upload.NotAllowedDomainException
import `in`.koreatech.koin.domain.error.upload.NotExistDomainException
import `in`.koreatech.koin.domain.error.upload.NotValidFileException
import `in`.koreatech.koin.domain.repository.UploadUrlRepository
import retrofit2.HttpException
import javax.inject.Inject

class UploadUrlRepositoryImpl @Inject constructor(
    private val uploadUrlRemoteDataSource: UploadUrlRemoteDataSource
): UploadUrlRepository {
    override suspend fun getUploadUrlResult(
        content_length: Long,
        content_type: String,
        file_name: String
    ): Result<Unit> {
        return try {
            uploadUrlRemoteDataSource.postUploadUrl(
                UploadUrlRequest(content_length, content_type, file_name)
            )
            Result.success(Unit)
        } catch (e: HttpException) {
            if(e.code() == 404) Result.failure(NotExistDomainException())
            else if(e.code() == 413) Result.failure(BoundOfSizeException())
            else if(e.code() == 415) Result.failure(NotAllowedDomainException())
            else if(e.code() == 422) Result.failure(NotValidFileException())
            else Result.failure(e)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

}