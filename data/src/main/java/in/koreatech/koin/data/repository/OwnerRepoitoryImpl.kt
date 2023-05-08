package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.remote.OwnerRemoteDataSource
import `in`.koreatech.koin.domain.repository.OwnerRepository
import android.util.Log
import javax.inject.Inject

class OwnerRepoitoryImpl @Inject constructor(
    private val ownerRemoteDataSource: OwnerRemoteDataSource
) : OwnerRepository {

    override suspend fun getOwnerName(): String {
        val response = ownerRemoteDataSource.getOwnerName()
        return try {
            if (response.isSuccessful) {
                response.body()?.name ?: ""
            } else {
                val errorBody = response.errorBody()?.string() // 오류 응답을 문자열로 가져오기
                val errorMessage = "오류 발생: ${response.code()} ${response.message()} $errorBody"
                Log.e("err", errorMessage)
                ""
            }
        } catch (e: Exception) {
            Log.e("err", "exception occurred ${e.message}")
            ""
        }
    }

}