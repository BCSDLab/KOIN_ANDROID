package `in`.koreatech.koin.feature.land.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.land.GetLandListUseCase
import `in`.koreatech.koin.feature.land.model.toUiModels
import `in`.koreatech.koin.feature.land.navigation.LandSideEffect
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
internal class LandViewModel @Inject constructor(
    private val getLandListUseCase: GetLandListUseCase
) : ViewModel(), ContainerHost<LandState, LandSideEffect> {
    override val container = container<LandState, LandSideEffect>(LandState(isLoading = true)) {
        getLandListUseCase()
            .onSuccess { lands ->
                reduce {
                    state.copy(
                        lands = lands.toUiModels(),
                        isLoading = false,
                        hasLoadError = false
                    )
                }
            }
            .onFailure {
                reduce {
                    state.copy(isLoading = false, hasLoadError = true)
                }
            }
    }

    fun selectFilter(filter: LandFilter) = intent {
        reduce {
            state.copy(filter = filter, selectedLandId = null)
        }
    }

    fun selectLand(landId: Int?) = intent {
        if (state.selectedLandId == landId) return@intent
        if (landId != null && state.lands.none { it.id == landId && state.filter.matches(it) }) return@intent
        reduce {
            state.copy(selectedLandId = landId)
        }
    }

    fun onDetailClick(landId: Int) = intent {
        val land = state.lands.firstOrNull { it.id == landId } ?: return@intent
        postSideEffect(LandSideEffect.NavigateToDetail(land))
    }

    fun onBackClick() = intent {
        if (state.selectedLandId != null) {
            reduce { state.copy(selectedLandId = null) }
        } else {
            postSideEffect(LandSideEffect.NavigateBack)
        }
    }
}
