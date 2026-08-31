package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.auth.RecruitmentAuthApi
import javax.inject.Inject
import retrofit2.HttpException

class RecruitmentRemoteDataSource @Inject constructor(
    private val recruitmentAuthApi: RecruitmentAuthApi
) {
    suspend fun getNotifications(
        page: Int?,
        limit: Int?
    ) = recruitmentAuthApi.getNotifications(
        page = page,
        limit = limit
    )

    suspend fun deleteAllNotifications() {
        val response = recruitmentAuthApi.deleteAllNotifications()
        if (!response.isSuccessful) throw HttpException(response)
    }

    suspend fun readNotification(
        notificationId: Int
    ) {
        val response = recruitmentAuthApi.readNotification(notificationId = notificationId)
        if (!response.isSuccessful) throw HttpException(response)
    }

    suspend fun readAllNotifications() {
        val response = recruitmentAuthApi.readAllNotifications()
        if (!response.isSuccessful) throw HttpException(response)
    }
}
