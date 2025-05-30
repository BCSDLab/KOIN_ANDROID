package `in`.koreatech.koin.feature.club.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.error.club.ClubError
import `in`.koreatech.koin.domain.model.club.ClubDetails
import `in`.koreatech.koin.domain.model.club.ClubQnasInfo
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.club.CancelClubLikeUseCase
import `in`.koreatech.koin.domain.usecase.club.DeleteClubQnaUseCase
import `in`.koreatech.koin.domain.usecase.club.GetClubDetailsUseCase
import `in`.koreatech.koin.domain.usecase.club.GetClubQnasUseCase
import `in`.koreatech.koin.domain.usecase.club.PostClubQnaUseCase
import `in`.koreatech.koin.domain.usecase.club.SetClubEmpowermentUseCase
import `in`.koreatech.koin.domain.usecase.club.SetClubLikeUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.feature.club.type.DetailTextFieldErrorCode.EMPTY_ERROR
import `in`.koreatech.koin.feature.club.type.DetailTextFieldErrorCode.NON_USERID_ERROR
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import retrofit2.HttpException

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
) : ViewModel(), ContainerHost<ClubDetailState, ClubDetailSideEffect> {
    override val container = container<ClubDetailState, ClubDetailSideEffect>(
        initialState = ClubDetailState()
    )

    private var selectedClubId = 1 // 선택한 Clud 의 id 값

    private val userInfoFlow: StateFlow<User> =
        getUserStatusUseCase()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), User.Anonymous)

    init {
        getUserIdCollect()
        fetchAllData()
    }

    private fun fetchAllData() = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        val clubDetails = loadClubDetails()
        val clubQnasInfo = loadClubQnas()
        reduce { state.copy(isLoading = false, clubDetails = clubDetails, clubQnasInfo = clubQnasInfo) }
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

    private suspend fun loadClubDetails(): ClubDetails? {
        getClubDetailsUseCase(selectedClubId).onSuccess {
            return it
        }
        return null
    }

    private suspend fun loadClubQnas(): ClubQnasInfo? {
        getClubQnasUseCase(selectedClubId).onSuccess {
            return it
        }
        return null
    }

    fun showAddQnaDialog() = intent {
        reduce { state.copy(showAddQnaDialog = true) }
    }
    fun dismissAddQnaDialog() = intent {
        reduce { state.copy(showAddQnaDialog = false, textFieldErrorResId = null) }
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
                    textFieldErrorResId = EMPTY_ERROR.strResId
                )
            }
            return@intent
        }
        state.clubDetails?.let {
            postClubQnaUseCase(
                clubId = selectedClubId,
                parentId = parentId,
                content = content
            ).onFailure { e ->
                if (e !is HttpException) throw e
            }
        }
        reduce { state.copy(showQnasProgressBar = true) }
        val clubQnasInfo = loadClubQnas()
        reduce {
            state.copy(
                isLoading = false,
                clubQnasInfo = clubQnasInfo,
                showAddQnaDialog = false,
                textFieldErrorResId = null,
                showQnasProgressBar = false
            )
        }
    }

    fun addClubQnaAnswer(
        parentId: Int,
        content: String
    ) = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        state.clubDetails?.let {
            postClubQnaUseCase(
                clubId = selectedClubId,
                parentId = parentId,
                content = content
            ).onFailure { e ->
                if (e !is HttpException) throw e
            }
        }
        reduce { state.copy(showQnasProgressBar = true) }
        val clubQnasInfo = loadClubQnas()
        reduce {
            state.copy(
                isLoading = false,
                clubQnasInfo = clubQnasInfo,
                showQnasProgressBar = false
            )
        }
    }

    fun deleteClubQna(
        qnaId: Int
    ) = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        state.clubDetails?.let {
            deleteClubQnaUseCase(
                clubId = selectedClubId,
                qnaId = qnaId
            ).onFailure { e ->
                if (e !is HttpException) throw e
            }
        }
        reduce { state.copy(showQnasProgressBar = true) }
        val clubQnasInfo = loadClubQnas()
        reduce {
            state.copy(
                isLoading = false,
                clubQnasInfo = clubQnasInfo,
                showQnasProgressBar = false
            )
        }
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
                cancelClubLikeUseCase(clubId = selectedClubId)
            } else {
                setClubLikeUseCase(clubId = selectedClubId)
            }
        }
        val clubDetails = loadClubDetails()
        reduce { state.copy(isLoading = false, clubDetails = clubDetails) }
    }

    fun showEmpowermentDialog() = intent {
        reduce { state.copy(showEmpowermentDialog = true) }
    }
    fun dismissEmpowermentDialog() = intent {
        reduce { state.copy(showEmpowermentDialog = false, textFieldErrorResId = null) }
        postSideEffect(ClubDetailSideEffect.ShowEmpowermentSnackBar)
    }

    fun setManagerEmpowerment(newUserId: String) = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        if (newUserId.isEmpty()) {
            reduce { state.copy(isLoading = false, textFieldErrorResId = EMPTY_ERROR.strResId) }
            return@intent
        }
        setClubEmpowermentUseCase(
            clubId = selectedClubId,
            changedManagerId = newUserId
        ).onFailure { e ->
            reduce { state.copy(isLoading = false) }
            if (e is ClubError.NotFoundUserId) {
                reduce { state.copy(textFieldErrorResId = NON_USERID_ERROR.strResId) }
            } else {
                throw e
            }
            return@intent
        }
        reduce { state.copy(showQnasProgressBar = false) }
        val clubDetails = loadClubDetails()
        val clubQnasInfo = loadClubQnas()
        reduce {
            state.copy(
                isLoading = false,
                clubDetails = clubDetails,
                clubQnasInfo = clubQnasInfo,
                showEmpowermentDialog = false,
                textFieldErrorResId = null,
                showQnasProgressBar = false
            )
        }
        postSideEffect(ClubDetailSideEffect.ShowEmpowermentSnackBar)
    }

    fun openUrl(url: String) = intent {
        postSideEffect(ClubDetailSideEffect.OpenUrl(url))
    }
}
