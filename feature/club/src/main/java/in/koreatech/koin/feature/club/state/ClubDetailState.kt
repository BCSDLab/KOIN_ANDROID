package `in`.koreatech.koin.feature.club.state

import `in`.koreatech.koin.domain.model.club.ClubDetails
import `in`.koreatech.koin.domain.model.club.ClubQnasInfo

data class ClubDetailState(
    val isLoading: Boolean = true,
    val isLogin: Boolean = false,
    val clubDetails: ClubDetails? = null,
    val clubQnasInfo: ClubQnasInfo? = null
)
