package `in`.koreatech.koin.feature.club.state

import `in`.koreatech.koin.domain.model.club.ClubDetails

data class ClubDetailState(
    val isLogin: Boolean = false,
    val clubDetails: ClubDetails? = null
)
