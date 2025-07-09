package `in`.koreatech.koin.feature.club.ui.clubrecruitcreate

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ClubRecruitCreateViewModel @Inject constructor() : ViewModel(), ContainerHost<ClubRecruitCreateState, ClubRecruitCreateSideEffect> {

    override val container = container<ClubRecruitCreateState, ClubRecruitCreateSideEffect>(ClubRecruitCreateState())
}
