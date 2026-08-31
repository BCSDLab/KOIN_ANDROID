package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.auth.RecruitmentAuthApi
import `in`.koreatech.koin.data.response.recruitment.MyRecruitmentListResponse
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

    suspend fun getMyRecruitmentPosts(
        status: String,
        sort: String,
        page: Int,
        limit: Int
    ): MyRecruitmentListResponse = recruitmentAuthApi.getMyRecruitmentPosts(status, sort, page, limit)

    suspend fun closeRecruitmentPost(postId: Int) {
        val response = recruitmentAuthApi.closeRecruitmentPost(postId)
        if (!response.isSuccessful) throw HttpException(response)
    }
}
