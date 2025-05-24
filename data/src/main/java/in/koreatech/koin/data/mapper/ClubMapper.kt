package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.club.ClubDetailsResponse
import `in`.koreatech.koin.data.response.club.ClubQnasResponse
import `in`.koreatech.koin.data.response.club.ClubQnasResponse.QnaResponse
import `in`.koreatech.koin.domain.model.club.ClubDetails
import `in`.koreatech.koin.domain.model.club.ClubQnasInfo
import `in`.koreatech.koin.domain.model.club.ClubQnasInfo.Qna

fun ClubDetailsResponse.toClubDetails() = ClubDetails(
    id,
    name,
    category,
    location,
    imageUrl,
    likes,
    description,
    introduction,
    instagram,
    googleForm,
    openChat,
    phoneNumber,
    manager,
    isLiked,
    updatedAt
)

fun ClubQnasResponse.toClubQnasInfo() = ClubQnasInfo(
    rootCount,
    totalCount,
    qnas.map { it.toQna() }
)

fun QnaResponse.toQna() = Qna(
    id,
    authorId,
    nickname,
    content,
    createdAt,
    children
)
