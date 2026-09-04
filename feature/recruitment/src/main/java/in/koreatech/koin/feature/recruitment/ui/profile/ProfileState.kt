package `in`.koreatech.koin.feature.recruitment.ui.profile

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentProfile

@Immutable
data class ProfileState(
    val profile: RecruitmentProfile? = null
)
