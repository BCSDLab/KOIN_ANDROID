package `in`.koreatech.koin.feature.recruitment.ui.profile

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentProfile

sealed interface ProfileLoadState {
    data object Loading : ProfileLoadState
    data object NotFound : ProfileLoadState
    data class Loaded(val profile: RecruitmentProfile) : ProfileLoadState
    data class Error(val message: String? = null) : ProfileLoadState
}

@Immutable
data class ProfileState(
    val loadState: ProfileLoadState = ProfileLoadState.Loading
) {
    val profile: RecruitmentProfile?
        get() = (loadState as? ProfileLoadState.Loaded)?.profile
}
