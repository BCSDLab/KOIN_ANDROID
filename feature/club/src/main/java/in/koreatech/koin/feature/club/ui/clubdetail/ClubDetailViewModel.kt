package `in`.koreatech.koin.feature.club.ui.clubdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.domain.error.club.KoinClubException
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.club.CancelClubLikeUseCase
import `in`.koreatech.koin.domain.usecase.club.DeleteClubQnaUseCase
import `in`.koreatech.koin.domain.usecase.club.GetClubDetailsUseCase
import `in`.koreatech.koin.domain.usecase.club.GetClubQnasUseCase
import `in`.koreatech.koin.domain.usecase.club.PostClubQnaUseCase
import `in`.koreatech.koin.domain.usecase.club.SetClubEmpowermentUseCase
import `in`.koreatech.koin.domain.usecase.club.SetClubLikeUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.club.R
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
    }

    fun fetchAllData() = intent {
        if (state.isLoading) return@intent
        loadClubDetails()
        loadClubQnas()
    }

    private fun getUserIdCollect() = intent {
        userInfoFlow.collect { user ->
            when (user) {
                is User.Anonymous -> {
                    reduce { state.copy(userId = null, userLoginId = null) }
                }
                is User.Student -> {
                    reduce { state.copy(userId = user.id, userLoginId = user.email?.removeSuffix("@koreatech.ac.kr")) }
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
                when (e) {
                    is KoinClubException.ClubNotFoundException -> {
                        postSideEffect(ClubDetailSideEffect.ClubNotFoundError)
                    }
                    else -> throw e
                }
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
                        showQnasProgressBar = false
                    )
                }
            }
        }
    }

    fun showAddQnaDialog() = intent {
        reduce { state.copy(showAddQnaDialog = true) }
    }

    fun dismissAddQnaDialog() = intent {
        reduce { state.copy(showAddQnaDialog = false, textFieldErrorMessageResId = null) }
    }

    fun addClubQna(
        parentId: Int?,
        content: String
    ) = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        if (content.isBlank()) {
            reduce {
                state.copy(
                    isLoading = false,
                    textFieldErrorMessageResId = R.string.detail_error_empty_text_field
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
                when (e) {
                    is KoinClubException.UnauthorizedException -> {
                        postSideEffect(ClubDetailSideEffect.UnauthorizedError)
                    }
                    is KoinClubException.ClubNotFoundException -> {
                        postSideEffect(ClubDetailSideEffect.ClubNotFoundError)
                    }
                    is KoinClubException.NotClubManagerException -> {
                        postSideEffect(ClubDetailSideEffect.NotClubManagerError)
                    }
                    else -> throw e
                }
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
                when (e) {
                    is KoinClubException.UnauthorizedException -> {
                        postSideEffect(ClubDetailSideEffect.UnauthorizedError)
                    }
                    is KoinClubException.ClubNotFoundException -> {
                        postSideEffect(ClubDetailSideEffect.ClubNotFoundError)
                    }
                    is KoinClubException.NotClubManagerException -> {
                        postSideEffect(ClubDetailSideEffect.NotClubManagerError)
                    }
                    else -> throw e
                }
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
                when (e) {
                    is KoinClubException.QnaNotFoundException -> {
                        postSideEffect(ClubDetailSideEffect.QnaNotFoundError)
                    }
                    is KoinClubException.DeletePermissionDeniedException -> {
                        postSideEffect(ClubDetailSideEffect.DeletePermissionDeniedError)
                    }
                    else -> throw e
                }
            }
        }
        loadClubQnas()
    }

    fun showLoginDialog() = intent {
        reduce { state.copy(showLoginDialog = true) }
    }

    fun dismissLoginDialog() = intent {
        reduce { state.copy(showLoginDialog = false) }
    }

    fun changeClubLike() = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        state.clubDetails?.let {
            if (it.isLiked) {
                cancelClubLikeUseCase(clubId = state.clubId).onSuccess { _ ->
                    EventLogger.logCampusClickEvent(
                        AnalyticsConstant.Label.Club.CLUB_INTRODUCTION_LIKE_CANCEL,
                        it.name
                    )
                }.onFailure { e ->
                    when (e) {
                        is KoinClubException.UnauthorizedException -> {
                            postSideEffect(ClubDetailSideEffect.UnauthorizedError)
                        }
                        is KoinClubException.AlreadyNotLikedException -> {
                            postSideEffect(ClubDetailSideEffect.AlreadyNotLikedError)
                        }
                        else -> throw e
                    }
                }
            } else {
                setClubLikeUseCase(clubId = state.clubId).onSuccess { _ ->
                    EventLogger.logCampusClickEvent(
                        AnalyticsConstant.Label.Club.CLUB_INTRODUCTION_LIKE,
                        it.name
                    )
                }.onFailure { e ->
                    when (e) {
                        is KoinClubException.UnauthorizedException -> {
                            postSideEffect(ClubDetailSideEffect.UnauthorizedError)
                        }
                        is KoinClubException.AlreadyLikedException -> {
                            postSideEffect(ClubDetailSideEffect.AlreadyLikedError)
                        }
                        else -> throw e
                    }
                }
            }
        }
        loadClubDetails()
        dismissLoginDialog()
    }

    fun showEmpowermentDialog() = intent {
        reduce { state.copy(showEmpowermentDialog = true) }
    }

    fun dismissEmpowermentDialog() = intent {
        reduce { state.copy(showEmpowermentDialog = false, textFieldErrorMessageResId = null) }
    }

    fun setManagerEmpowerment(newUserId: String) = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        if (newUserId.isEmpty()) {
            reduce { state.copy(isLoading = false, textFieldErrorMessageResId = R.string.detail_error_empty_text_field) }
            return@intent
        }
        setClubEmpowermentUseCase(
            clubId = state.clubId,
            changedManagerId = newUserId
        ).onSuccess {
            EventLogger.logCampusClickEvent(
                AnalyticsConstant.Label.Club.CLUB_DELEGATION_AUTHORITY_CONFIRM,
                "권한위임"
            )
        }.onFailure { e ->
            reduce { state.copy(isLoading = false) }
            when (e) {
                is KoinClubException.AlreadyManagerException -> {
                    reduce { state.copy(textFieldErrorMessageResId = R.string.detail_error_already_manager) }
                }
                is KoinClubException.UnauthorizedException -> {
                    postSideEffect(ClubDetailSideEffect.UnauthorizedError)
                }
                is KoinClubException.NotClubManagerException -> {
                    postSideEffect(ClubDetailSideEffect.NotClubManagerError)
                }
                is KoinClubException.LoginIdNotFoundException -> {
                    reduce { state.copy(textFieldErrorMessageResId = R.string.detail_error_user_id_not_found) }
                }
                is KoinClubException.ClubNotFoundException -> {
                    postSideEffect(ClubDetailSideEffect.ClubNotFoundError)
                }
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

    fun showImageDialog() = intent {
        reduce { state.copy(showImageDialog = true) }
    }

    fun dismissImageDialog() = intent {
        reduce { state.copy(showImageDialog = false) }
    }
}
