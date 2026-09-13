package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.recruitment.MyAppliedRecruitmentListResponse
import `in`.koreatech.koin.data.response.recruitment.MyRecruitmentListResponse
import `in`.koreatech.koin.domain.model.recruitment.MyAppliedRecruitments
import `in`.koreatech.koin.domain.model.recruitment.MyRecruitmentPosts

fun MyRecruitmentListResponse.toMyRecruitmentPosts() = MyRecruitmentPosts(
    posts = recruitments.map { it.toMyRecruitmentPost() },
    totalCount = totalCount.toLong(),
    currentCount = currentCount,
    totalPage = totalPage,
    currentPage = currentPage
)

fun MyAppliedRecruitmentListResponse.toMyAppliedRecruitments() = MyAppliedRecruitments(
    applications = applications.map { it.toMyAppliedRecruitment() },
    totalCount = totalCount.toLong(),
    currentCount = currentCount,
    totalPage = totalPage,
    currentPage = currentPage
)
