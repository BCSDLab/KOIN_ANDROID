package `in`.koreatech.koin.feature.recruitment.ui.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.recruitment.KoinRecruitmentException
import `in`.koreatech.koin.domain.usecase.recruitment.GetTeamRecruitmentProfileUseCase
import `in`.koreatech.koin.feature.recruitment.mapper.toRecruitmentErrorMessage
import `in`.koreatech.koin.feature.recruitment.mapper.toRecruitmentProfile
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getTeamRecruitmentProfileUseCase: GetTeamRecruitmentProfileUseCase
) : ViewModel(), ContainerHost<ProfileState, ProfileSideEffect> {

    override val container = container<ProfileState, ProfileSideEffect>(ProfileState()) {
        loadProfile()
    }

    fun loadProfile() = intent {
        reduce { state.copy(loadState = ProfileLoadState.Loading) }
        getTeamRecruitmentProfileUseCase()
            .onSuccess { profile ->
                reduce { state.copy(loadState = ProfileLoadState.Loaded(profile.toRecruitmentProfile())) }
            }
            .onFailure { throwable ->
                val loadState = when (throwable) {
                    is KoinRecruitmentException.ProfileNotFoundException -> ProfileLoadState.NotFound
                    else -> ProfileLoadState.Error(throwable.toRecruitmentErrorMessage())
                }
                reduce { state.copy(loadState = loadState) }
            }
    }

    fun onMyRecruitmentClick() = intent {
        postSideEffect(ProfileSideEffect.NavigateToMyRecruitment)
    }

    fun onMyAppliedRecruitmentClick() = intent {
        postSideEffect(ProfileSideEffect.NavigateToMyAppliedRecruitment)
    }

    fun onCreateProfileClick() = intent {
        postSideEffect(ProfileSideEffect.NavigateToProfileCreate(isEditMode = false))
    }

    fun onEditProfileClick() = intent {
        postSideEffect(ProfileSideEffect.NavigateToProfileCreate(isEditMode = true))
    }
}
