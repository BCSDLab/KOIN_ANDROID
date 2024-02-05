package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.httpExceptionMapper
import `in`.koreatech.koin.data.request.upload.UploadUrlRequest
import `in`.koreatech.koin.data.source.remote.UploadUrlRemoteDataSource
import `in`.koreatech.koin.domain.repository.UploadUrlRepository
import retrofit2.HttpException
import javax.inject.Inject

class UploadUrlRepositoryImpl @Inject constructor(
    private val uploadUrlRemoteDataSource: UploadUrlRemoteDataSource
): UploadUrlRepository {
    override suspend fun getUploadUrlResult(
        contentLength: Long,
        contentType: String,
        fileName: String
    ): Result<Unit> {
        return try {
            uploadUrlRemoteDataSource.postUploadUrl(
                UploadUrlRequest(contentLength, contentType, fileName)
            )
            Result.success(Unit)
        } catch (e: HttpException) {
            e.httpExceptionMapper()
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

}