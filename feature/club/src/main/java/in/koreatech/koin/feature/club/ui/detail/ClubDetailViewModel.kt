package `in`.koreatech.koin.feature.club.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.club.CancelClubLikeUseCase
import `in`.koreatech.koin.domain.usecase.club.DeleteClubQnaUseCase
import `in`.koreatech.koin.domain.usecase.club.GetClubDetailsUseCase
import `in`.koreatech.koin.domain.usecase.club.GetClubQnasUseCase
import `in`.koreatech.koin.domain.usecase.club.PostClubQnaUseCase
import `in`.koreatech.koin.domain.usecase.club.SetClubEmpowermentUseCase
import `in`.koreatech.koin.domain.usecase.club.SetClubLikeUseCase
import `in`.koreatech.koin.feature.club.intent.ClubDetailIntent
import `in`.koreatech.koin.feature.club.state.ClubDetailState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ClubDetailViewModel @Inject constructor(
    private val getClubDetailsUseCase: GetClubDetailsUseCase,
    private val getClubQnasUseCase: GetClubQnasUseCase,
    private val deleteClubQnaUseCase: DeleteClubQnaUseCase,
    private val cancelClubLikeUseCase: CancelClubLikeUseCase,
    private val postClubQnaUseCase: PostClubQnaUseCase,
    private val setClubEmpowermentUseCase: SetClubEmpowermentUseCase,
    private val setClubLikeUseCase: SetClubLikeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ClubDetailState())
    val state: StateFlow<ClubDetailState> = _state.asStateFlow()

    init {
        handleIntent(ClubDetailIntent.LoadClubDetails)
    }

    fun handleIntent(intent: ClubDetailIntent) {
        when (intent) {
            is ClubDetailIntent.LoadClubDetails -> loadClubDetails()
        }
    }

    fun loadClubDetails() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            getClubDetailsUseCase(1).onSuccess {
                val clubDetails = it
                _state.update {
                    it.copy(clubDetails = clubDetails)
                }
            }.onFailure {
                Log.e("MYLOG", "GetDetails ERROR")
            }
        }
    }
}
