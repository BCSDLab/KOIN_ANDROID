package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.club.ClubCategoriesResponse
import `in`.koreatech.koin.domain.model.club.ClubCategories

fun ClubCategoriesResponse.toClubCategories() = ClubCategories(
    clubCategories = clubCategories.map {
        ClubCategories.Categories(
            id = it.id,
            name = it.name
        )
    }
)