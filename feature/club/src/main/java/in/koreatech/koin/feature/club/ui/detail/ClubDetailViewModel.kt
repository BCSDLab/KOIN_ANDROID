package `in`.koreatech.koin.feature.club.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

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

    private var selectedClubId = 1 // 연결 이전 hard Coding

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
        reduce { state.copy(showAddQnaDialog = false) }
    }

    fun addClubQna(
        parentId: Int?,
        content: String
    ) = intent {
        if (state.isLoading) return@intent
        reduce { state.copy(isLoading = true) }
        state.clubDetails?.let {
            postClubQnaUseCase(
                clubId = selectedClubId,
                parentId = parentId,
                content = content
            )
        }
        val clubQnasInfo = loadClubQnas()
        reduce { state.copy(isLoading = false, clubQnasInfo = clubQnasInfo) }
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
            )
        }
        val clubQnasInfo = loadClubQnas()
        reduce { state.copy(isLoading = false, clubQnasInfo = clubQnasInfo) }
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
            }
            else {
                setClubLikeUseCase(clubId = selectedClubId)
            }
        }
        val clubDetails = loadClubDetails()
        reduce { state.copy(isLoading = false, clubDetails = clubDetails) }
    }
}
