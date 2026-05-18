package `in`.koreatech.koin.feature.club.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ClubNavType {
    @Serializable
    data class ClubList(val categoryId: Int) : ClubNavType()

    @Serializable
    data class ClubDetail(
        val clubId: Int,
        val recruitEvent: Boolean = false,
        val eventId: Int = -1
    ) : ClubNavType()

    @Serializable
    data object ClubCreate : ClubNavType()

    @Serializable
    data class ClubModify(val clubId: Int) : ClubNavType()

    @Serializable
    data class ClubRecruitCreate(val clubId: Int) : ClubNavType()

    @Serializable
    data class ClubRecruitModify(val clubId: Int) : ClubNavType()

    @Serializable
    data class ClubEventCreate(val clubId: Int) : ClubNavType()

    @Serializable
    data class ClubEventModify(val clubId: Int, val eventId: Int) : ClubNavType()
}
