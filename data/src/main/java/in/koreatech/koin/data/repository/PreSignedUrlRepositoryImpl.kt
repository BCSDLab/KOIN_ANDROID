package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.requestbody.S3RequestBody
import `in`.koreatech.koin.data.source.remote.PreSignedUrlRemoteDataSource
import `in`.koreatech.koin.domain.repository.PreSignedUrlRepository
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.HttpException
import java.io.InputStream
import javax.inject.Inject

class PreSignedUrlRepositoryImpl @Inject constructor(
    private val preSignedUrlRemoteDataSource: PreSignedUrlRemoteDataSource
): PreSignedUrlRepository {
    override suspend fun putPreSignedUrl(url: String, inputStream: InputStream, mediaType: String, mediaSize: Long): Result<Unit> {
        return try {
            val file = S3RequestBody(inputStream, mediaType.toMediaType(), mediaSize)
            preSignedUrlRemoteDataSource.putPreSignedUrl(url, file)

            Result.success(Unit)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

}