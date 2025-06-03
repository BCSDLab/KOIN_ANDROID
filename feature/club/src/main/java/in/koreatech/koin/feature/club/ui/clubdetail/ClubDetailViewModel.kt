package `in`.koreatech.koin.feature.club.ui.clubdetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.club.ClubError
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.club.CancelClubLikeUseCase
import `in`.koreatech.koin.domain.usecase.club.DeleteClubQnaUseCase
import `in`.koreatech.koin.domain.usecase.club.GetClubDetailsUseCase
import `in`.koreatech.koin.domain.usecase.club.GetClubQnasUseCase
import `in`.koreatech.koin.domain.usecase.club.PostClubQnaUseCase
import `in`.koreatech.koin.domain.usecase.club.SetClubEmpowermentUseCase
import `in`.koreatech.koin.domain.usecase.club.SetClubLikeUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.club.model.toParcelizeClubDetails
import `in`.koreatech.koin.feature.club.model.toParcelizeClubQnasInfo
import `in`.koreatech.koin.feature.club.navigation.CLUB_ID
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import retrofit2.HttpException

@HiltViewModel
class ClubDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val getClubDetailsUseCase: GetClubDetailsUseCase,
    private val getClubQnasUseCase: GetClubQnasUseCase,
    private val deleteClubQnaUseCase: DeleteClubQnaUseCase,
    private val cancelClubLikeUseCase: CancelClubLikeUseCase,
    private val postClubQnaUseCase: PostClubQnaUseCase,
    private val setClubEmpowermentUseCase: SetClubEmpowermentUseCase,
    private val setClubLikeUseCase: SetClubLikeUseCase
) : ViewModel(), ContainerHost<ClubDetailState, ClubDetailSideEffect> {
    override val container = container<ClubDetailState, ClubDetailSideEffect>(
        initialState = ClubDetailState(),
        savedStateHandle = savedStateHandle
    ) {
        val clubId = savedStateHandle.get<Int>(CLUB_ID)
        checkNotNull(clubId)
        intent {
            reduce {
                state.copy(
                    clubId = clubId
                )
            }
        }
    }

    private val userInfoFlow: StateFlow<User> =
        getUserStatusUseCase()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), User.Anonymous)

    init {
        getUserIdCollect()
        fetchAllData()
    }

    private fun showExceptionToast(e: Throwable) = viewModelScope.launch {
        intent {
            if (e is ClubError) {
                postSideEffect(ClubDetailSideEffect.ShowToast(e.message ?: DEFAULT_ERROR_MESSAGE))
            }
            else throw e
        }
    }

    private fun fetchAllData() = intent {
        if (state.isLoading) return@intent
        loadClubDetails()
        loadClubQnas()
    }

    private fun getUserIdCollect() = intent {
        userInfoFlow.collect { user ->
            when (user) {
                is User.Anonymous -> {
                    reduce { state.copy(userId = null) }
                }
                is User.Student -> {
                    reduce { state.copy(userId = user.id) }
                }
            }
        }
    }

    private fun loadClubDetails() = viewModelScope.launch {
        intent {
            reduce {
                state.copy(isLoading = true)
            }
            getClubDetailsUseCase(state.clubId).onSuccess {
                reduce {
                    state.copy(
                        clubDetails = it.toParcelizeClubDetails(),
                        isLoading = false
                    )
                }
            }.onFailure { e ->
                reduce { state.copy(isLoading = false) }
                showExceptionToast(e)
            }
        }
    }

    private fun loadClubQnas() = viewModelScope.launch {
        intent {
            reduce {
                state.copy(isLoading = true, showQnasProgressBar = true)
            }
            getClubQnasUseCase(state.clubId).onSuccess {
                reduce {
                    state.copy(
                        clubQnasInfo = it.toParcelizeClubQnasInfo(),
                        isLoading = false,
                        showQnasProgressBar = true
                    )
                }
            }
        }
    }

    fun showAddQnaDialog() = intent {
        reduce { state.copy(showAddQnaDialog = true) }
    }

    fun dismissAddQnaDialog() = viewModelScope.launch {
        intent {
            reduce { state.copy(showAddQnaDialog = false, textFieldErrorMessage = null) }
        }
    }

    fun addClubQna(
        parentId: Int?,
        content: String
    ) = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        if (content.isEmpty()) {
            reduce {
                state.copy(
                    isLoading = false,
                    textFieldErrorMessage = EMPTY_ERROR_MESSAGE
                )
            }
            return@intent
        }
        state.clubDetails?.let {
            postClubQnaUseCase(
                clubId = state.clubId,
                parentId = parentId,
                content = content
            ).onFailure { e ->
                showExceptionToast(e)
            }
        }
        loadClubQnas()
        dismissAddQnaDialog()
    }

    fun addClubQnaAnswer(
        parentId: Int,
        content: String
    ) = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        state.clubDetails?.let {
            postClubQnaUseCase(
                clubId = state.clubId,
                parentId = parentId,
                content = content
            ).onFailure { e ->
                showExceptionToast(e)
            }
        }
        loadClubQnas()
    }

    fun deleteClubQna(
        qnaId: Int
    ) = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        state.clubDetails?.let {
            deleteClubQnaUseCase(
                clubId = state.clubId,
                qnaId = qnaId
            ).onFailure { e ->
                showExceptionToast(e)
            }
        }
        loadClubQnas()
    }

    fun showLoginDialog() = intent {
        reduce { state.copy(showLoginDialog = true) }
    }

    fun dismissLoginDialog() = viewModelScope.launch {
        intent {
            reduce { state.copy(showLoginDialog = false) }
        }
    }

    fun changeClubLike() = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        state.clubDetails?.let {
            if (it.isLiked) {
                cancelClubLikeUseCase(clubId = state.clubId).onFailure { e ->
                    showExceptionToast(e)
                }
            } else {
                setClubLikeUseCase(clubId = state.clubId).onFailure { e ->
                    showExceptionToast(e)
                }
            }
        }
        loadClubDetails()
        dismissLoginDialog()
    }

    fun showEmpowermentDialog() = intent {
        reduce { state.copy(showEmpowermentDialog = true) }
    }

    fun dismissEmpowermentDialog() = viewModelScope.launch {
        intent {
            reduce { state.copy(showEmpowermentDialog = false, textFieldErrorMessage = null) }
        }
    }

    fun setManagerEmpowerment(newUserId: String) = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        if (newUserId.isEmpty()) {
            reduce { state.copy(isLoading = false, textFieldErrorMessage = EMPTY_ERROR_MESSAGE) }
            return@intent
        }
        setClubEmpowermentUseCase(
            clubId = state.clubId,
            changedManagerId = newUserId
        ).onFailure { e ->
            reduce { state.copy(isLoading = false) }
            if (e is ClubError.UserIdOrClubNotFound) {
                reduce { state.copy(textFieldErrorMessage = e.message) }
            } else {
                throw e
            }
            return@intent
        }
        loadClubDetails()
        loadClubQnas()
        dismissEmpowermentDialog()
        postSideEffect(ClubDetailSideEffect.ShowEmpowermentSnackBar)
    }

    fun openUrl(url: String) = intent {
        postSideEffect(ClubDetailSideEffect.OpenUrl(url))
    }
}
