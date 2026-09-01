package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.auth.RecruitmentAuthApi
import `in`.koreatech.koin.data.response.recruitment.MyAppliedRecruitmentListResponse
import `in`.koreatech.koin.data.response.recruitment.MyRecruitmentListResponse
import javax.inject.Inject
import retrofit2.HttpException

class RecruitmentRemoteDataSource @Inject constructor(
    private val recruitmentAuthApi: RecruitmentAuthApi
) {
    @Suppress("LongParameterList")
    suspend fun getRecruitments(
        keyword: String?,
        status: String?,
        categories: List<String>?,
        meetingType: String?,
        sort: String?,
        page: Int?,
        limit: Int?
    ) = recruitmentAuthApi.getRecruitments(
        keyword = keyword,
        status = status,
        categories = categories,
        meetingType = meetingType,
        sort = sort,
        page = page,
        limit = limit
    )

    suspend fun getRecruitmentDetail(
        recruitmentId: Int
    ) = recruitmentAuthApi.getRecruitmentDetail(recruitmentId = recruitmentId)

    suspend fun deleteRecruitment(
        recruitmentId: Int
    ) {
        val response = recruitmentAuthApi.deleteRecruitment(recruitmentId = recruitmentId)
        if (!response.isSuccessful) throw HttpException(response)
    }

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

    suspend fun getMyAppliedRecruitments(
        statuses: List<String>,
        sort: String,
        page: Int,
        limit: Int
    ): MyAppliedRecruitmentListResponse = recruitmentAuthApi.getMyAppliedRecruitments(statuses, sort, page, limit)

    suspend fun closeRecruitmentPost(postId: Int) {
        val response = recruitmentAuthApi.closeRecruitmentPost(postId)
        if (!response.isSuccessful) throw HttpException(response)
    }
}
