package `in`.koreatech.koin.data.repository

import android.util.Log
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
    ): Result<Pair<String, String>> {
        return try {
            val fileUrl = uploadUrlRemoteDataSource.postUploadUrl(
                UploadUrlRequest(contentLength, contentType, fileName)
            ).fileUrl

            val preSignedUrl = uploadUrlRemoteDataSource.postUploadUrl(
                UploadUrlRequest(contentLength, contentType, fileName)
            ).preSignedUrl

            Result.success(Pair(fileUrl, preSignedUrl))
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun getUploadMarketUrlResult(
        contentLength: Long,
        contentType: String,
        fileName: String
    ): Result<Pair<String, String>> {
        return try {
            val dataSource = uploadUrlRemoteDataSource.postUploadMarketUrl(
                UploadUrlRequest(contentLength, contentType, fileName)
            )

            val preSignedUrl = dataSource.preSignedUrl
            val fileUrl = dataSource.fileUrl

            Result.success(Pair(fileUrl, preSignedUrl))
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

}