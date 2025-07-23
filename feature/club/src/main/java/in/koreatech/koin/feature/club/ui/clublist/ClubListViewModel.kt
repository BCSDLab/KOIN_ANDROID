package `in`.koreatech.koin.feature.club.ui.clublist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.domain.usecase.club.CancelClubLikeUseCase
import `in`.koreatech.koin.domain.usecase.club.GetClubsUseCase
import `in`.koreatech.koin.domain.usecase.club.SearchClubsUseCase
import `in`.koreatech.koin.domain.usecase.club.SetClubLikeUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.feature.club.model.ClubSort
import `in`.koreatech.koin.feature.club.model.toParcelizeClubItems
import `in`.koreatech.koin.feature.club.navigation.CATEGORY_ID
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ClubListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getClubsUseCase: GetClubsUseCase,
    private val setClubLikeUseCase: SetClubLikeUseCase,
    private val cancelClubLikeUseCase: CancelClubLikeUseCase,
    private val searchClubsUseCase: SearchClubsUseCase
) : ViewModel(), ContainerHost<ClubListState, ClubListSideEffect> {
    override val container =
        container<ClubListState, ClubListSideEffect>(ClubListState(), savedStateHandle) {
            val categoryId = savedStateHandle.get<Int?>(CATEGORY_ID)
            intent {
                reduce {
                    state.copy(categoryId = categoryId.takeIf { it != -1 }, isInitialized = true)
                }
            }
        }

    init {
        getUserType()
    }

    private fun getUserType() = viewModelScope.launch {
        getUserInfoUseCase().first.let { user ->
            if (user == null || user.isAnonymous) {
                intent {
                    reduce {
                        state.copy(isAnonymous = true)
                    }
                }
            } else {
                intent {
                    reduce {
                        state.copy(isAnonymous = false)
                    }
                }
            }
        }
    }

    fun updateSearchKeyword(keyword: String) = blockingIntent {
        reduce {
            state.copy(searchKeyword = keyword, shouldExpandSearchBar = true)
        }
    }

    fun updateCategoryId(categoryId: Int?) = intent {
        reduce {
            state.copy(categoryId = categoryId)
        }
        postSideEffect(ClubListSideEffect.RefreshClubs)
    }

    fun updateSortType(sortType: ClubSort) = intent {
        reduce {
            state.copy(sortType = sortType)
        }
        postSideEffect(ClubListSideEffect.RefreshClubs)
    }

    fun updateDropdownExpanded(isExpanded: Boolean) = intent {
        reduce {
            state.copy(isDropdownExpanded = isExpanded)
        }
    }

    fun updateShowClubCreateDialog(shouldShow: Boolean) = blockingIntent {
        reduce {
            state.copy(shouldShowClubCreateDialog = shouldShow)
        }
    }

    fun updateShowLoginDialog(shouldShow: Boolean) = intent {
        reduce {
            state.copy(shouldShowLoginDialog = shouldShow)
        }
    }

    fun navigateToCreateClub() = intent { postSideEffect(ClubListSideEffect.NavigateToCreateClub) }

    fun getClubs() = viewModelScope.launch {
        intent {
            reduce {
                state.copy(isLoading = true)
            }
            getClubsUseCase(
                categoryId = state.categoryId,
                sortType = state.sortType.name,
                isRecruiting = state.isRecruiting,
                query = state.searchKeyword
            ).onSuccess { clubs ->
                reduce {
                    state.copy(clubs = clubs.toParcelizeClubItems(), isLoading = false)
                }
            }.onFailure {
                reduce {
                    state.copy(isLoading = false)
                }
                postSideEffect(ClubListSideEffect.ClubsFetchFailed)
            }
        }
    }

    fun searchClubs(searchKeyword: String) = intent {
        reduce {
            state.copy(suggestions = emptyList(), shouldExpandSearchBar = false, searchKeyword = searchKeyword)
        }
        getClubs()
    }

    fun getSuggestion() = intent {
        searchClubsUseCase(state.searchKeyword).onSuccess {
            reduce {
                state.copy(suggestions = it.keywords.map { it.clubName })
            }
        }.onFailure {
            reduce {
                state.copy(suggestions = emptyList())
            }
        }
    }

    fun updateSearchBarExpand(shouldExpand: Boolean) = intent {
        reduce {
            state.copy(shouldExpandSearchBar = shouldExpand)
        }
        if (!shouldExpand) {
            reduce {
                state.copy(searchKeyword = "", suggestions = emptyList())
            }
        }
    }

    fun changeClubLike(clubId: Int) = viewModelScope.launch {
        intent {
            reduce {
                state.copy(isLoading = true)
            }
            state.clubs.forEach { club ->
                if (club.id == clubId) {
                    if (club.isLiked) {
                        cancelClubLikeUseCase(clubId).onSuccess {
                            reduce {
                                state.copy(
                                    isLoading = false,
                                    clubs = state.clubs.map {
                                        if (it.id == clubId) {
                                            it.copy(
                                                isLiked = !it.isLiked,
                                                likes = it.likes - 1
                                            )
                                        } else {
                                            it
                                        }
                                    }
                                )
                            }
                            EventLogger.logCampusClickEvent(
                                AnalyticsConstant.Label.Club.MAIN_CLUB_LIKE_CANCEL,
                                club.name
                            )
                        }.onFailure {
                            reduce {
                                state.copy(isLoading = false)
                            }
                            postSideEffect(ClubListSideEffect.ClubDislikeFailed)
                        }
                    } else {
                        setClubLikeUseCase(clubId).onSuccess {
                            reduce {
                                state.copy(
                                    isLoading = false,
                                    clubs = state.clubs.map {
                                        if (it.id == clubId) {
                                            it.copy(
                                                isLiked = !it.isLiked,
                                                likes = it.likes + 1
                                            )
                                        } else {
                                            it
                                        }
                                    }
                                )
                            }
                            EventLogger.logCampusClickEvent(
                                AnalyticsConstant.Label.Club.MAIN_CLUB_LIKE,
                                club.name
                            )
                        }.onFailure {
                            reduce {
                                state.copy(isLoading = false)
                            }
                            postSideEffect(ClubListSideEffect.ClubLikeFailed)
                        }
                    }
                    return@intent
                }
            }
        }
    }

    fun updateRecruiting(isRecruiting: Boolean) = intent {
        reduce {
            state.copy(
                isRecruiting = isRecruiting,
                sortType = if (isRecruiting) ClubSort.RECRUITMENT_UPDATED_DESC else ClubSort.CREATED_AT_ASC // Reset sort type
            )
        }
        postSideEffect(ClubListSideEffect.RefreshClubs)
    }
}
