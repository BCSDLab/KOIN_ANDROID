package `in`.koreatech.koin.feature.club.ui.clublist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.club.GetClubsUseCase
import `in`.koreatech.koin.feature.club.model.ClubSort
import `in`.koreatech.koin.feature.club.model.toParcelizeClubItems
import `in`.koreatech.koin.feature.club.navigation.CATEGORY_ID
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ClubListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getClubsUseCase: GetClubsUseCase
) : ViewModel(), ContainerHost<ClubListState, ClubListSideEffect> {
    override val container = container<ClubListState, ClubListSideEffect>(ClubListState(), savedStateHandle) {
        val categoryId = savedStateHandle.get<Int?>(CATEGORY_ID)
        intent {
            reduce {
                state.copy(categoryId = categoryId)
            }
        }
    }

    init {
        intent {
            postSideEffect(ClubListSideEffect.RefreshClubs)
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

    fun getClubs() = viewModelScope.launch {
        intent {
            getClubsUseCase(
                categoryId = state.categoryId,
                sortType = state.sortType.name
            ).onSuccess { clubs ->
                reduce {
                    state.copy(clubs = clubs.toParcelizeClubItems())
                }
            }
        }
    }
}
