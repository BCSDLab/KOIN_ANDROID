package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.club.ClubCategoriesResponse
import `in`.koreatech.koin.data.response.club.ClubDetailsResponse
import `in`.koreatech.koin.data.response.club.ClubHotResponse
import `in`.koreatech.koin.data.response.club.ClubQnasResponse
import `in`.koreatech.koin.data.response.club.ClubQnasResponse.QnaResponse
import `in`.koreatech.koin.data.response.club.ClubsResponse
import `in`.koreatech.koin.domain.model.club.ClubCategories
import `in`.koreatech.koin.domain.model.club.ClubDetails
import `in`.koreatech.koin.domain.model.club.ClubHot
import `in`.koreatech.koin.domain.model.club.ClubQnasInfo
import `in`.koreatech.koin.domain.model.club.ClubQnasInfo.Qna
import `in`.koreatech.koin.domain.model.club.Clubs

fun ClubCategoriesResponse.toClubCategories() = ClubCategories(
    clubCategories = clubCategories.map {
        ClubCategories.Categories(
            id = it.id,
            name = it.name
        )
    }
)

fun ClubHotResponse.toClubHot() = ClubHot(
    clubId = clubId,
    name = name,
    imageUrl = imageUrl
)

fun ClubsResponse.toClubs() = Clubs(
    clubs = clubs.map {
        Clubs.ClubItem(
            id = it.id,
            name = it.name,
            category = it.category,
            likes = it.likes,
            imageUrl = it.imageUrl,
            isLiked = it.isLiked
        )
    }
)

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
    children.map { it.toFinalQna() }
)

fun QnaResponse.toFinalQna() = Qna(
    id,
    authorId,
    nickname,
    content,
    createdAt,
    listOf<Qna>()
)
