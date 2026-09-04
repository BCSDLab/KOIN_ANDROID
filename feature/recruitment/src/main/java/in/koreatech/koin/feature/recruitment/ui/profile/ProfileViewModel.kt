package `in`.koreatech.koin.feature.recruitment.ui.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel(), ContainerHost<ProfileState, ProfileSideEffect> {

    override val container = container<ProfileState, ProfileSideEffect>(ProfileState())

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
