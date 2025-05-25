package `in`.koreatech.koin.feature.club.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.club.CancelClubLikeUseCase
import `in`.koreatech.koin.domain.usecase.club.DeleteClubQnaUseCase
import `in`.koreatech.koin.domain.usecase.club.GetClubDetailsUseCase
import `in`.koreatech.koin.domain.usecase.club.GetClubQnasUseCase
import `in`.koreatech.koin.domain.usecase.club.PostClubQnaUseCase
import `in`.koreatech.koin.domain.usecase.club.SetClubEmpowermentUseCase
import `in`.koreatech.koin.domain.usecase.club.SetClubLikeUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.club.intent.detail.ClubDetailIntent
import `in`.koreatech.koin.feature.club.state.ClubDetailState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ClubDetailViewModel @Inject constructor(
    private val getUserStatusUseCase: GetUserStatusUseCase,
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
    private var selectedClubId = 1 // 연결 이전 hard Coding

    private val userInfoFlow: StateFlow<User> =
        getUserStatusUseCase()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), User.Anonymous)

    init {
        getUserIdCollect()
        handleIntent(ClubDetailIntent.loadClubDetailsAndQnas)
    }

    fun handleIntent(intent: ClubDetailIntent) {
        when (intent) {
            is ClubDetailIntent.loadClubDetailsAndQnas -> {
                startJob {
                    loadClubDetails()
                    loadClubQnas()
                }
            }
            is ClubDetailIntent.LoadClubDetails -> {
                startJob {
                    loadClubDetails()
                }
            }
            is ClubDetailIntent.LoadClubQnas -> {
                startJob {
                    loadClubQnas()
                }
            }
            is ClubDetailIntent.addClubQna -> {
                startJob {
                    _state.value.clubDetails?.let {
                        addClubQna(selectedClubId, intent.parentId, intent.content)
                        loadClubQnas()
                    }
                }
            }
            is ClubDetailIntent.deleteClubQna -> {
                startJob {
                    _state.value.clubDetails?.let {
                        deleteClubQna(selectedClubId, intent.qnaId)
                        loadClubQnas()
                    }
                }
            }
            is ClubDetailIntent.changeClubLike -> {
                startJob {
                    _state.value.clubDetails?.let {
                        if (it.isLiked) {
                            cancelClubLike(selectedClubId)
                        } else {
                            setClubLike(selectedClubId)
                        }
                        loadClubDetails()
                    }
                }
            }
            else -> {}
        }
    }

    fun startJob(job: suspend () -> Unit) {
        if (_state.value.isLoading) return
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            job()
            _state.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun getUserIdCollect() {
        viewModelScope.launch {
            userInfoFlow.collect { user ->
                when (user) {
                    is User.Anonymous -> {
                        _state.update {
                            it.copy(userId = null)
                        }
                    }
                    is User.Student -> {
                        _state.update {
                            it.copy(userId = user.id)
                        }
                    }
                }
            }
        }
    }

    suspend fun loadClubDetails() {
        getClubDetailsUseCase(selectedClubId).onSuccess {
            val clubDetails = it
            _state.update {
                it.copy(clubDetails = clubDetails)
            }
        }
    }

    suspend fun loadClubQnas() {
        getClubQnasUseCase(selectedClubId).onSuccess {
            val clubQnasInfo = it
            _state.update {
                it.copy(clubQnasInfo = clubQnasInfo)
            }
        }
    }

    suspend fun addClubQna(
        clubId: Int,
        parentId: Int?,
        content: String
    ) {
        postClubQnaUseCase(
            clubId = clubId,
            parentId = parentId,
            content = content
        ).onSuccess {
        }
    }

    suspend fun deleteClubQna(
        clubId: Int,
        qnaId: Int
    ) {
        deleteClubQnaUseCase(
            clubId = clubId,
            qnaId = qnaId
        ).onSuccess {
        }
    }

    suspend fun setClubLike(
        clubId: Int
    ) {
        setClubLikeUseCase(
            clubId = clubId
        ).onSuccess {
        }
    }

    suspend fun cancelClubLike(
        clubId: Int
    ) {
        cancelClubLikeUseCase(
            clubId = clubId
        ).onSuccess {
        }
    }
}
