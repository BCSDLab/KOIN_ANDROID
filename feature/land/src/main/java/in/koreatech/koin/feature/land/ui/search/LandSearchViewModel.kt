package `in`.koreatech.koin.feature.land.ui.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.land.GetLandListUseCase
import `in`.koreatech.koin.feature.land.model.toUiModels
import `in`.koreatech.koin.feature.land.navigation.LandSideEffect
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
internal class LandSearchViewModel @Inject constructor(
    private val getLandListUseCase: GetLandListUseCase
) : ViewModel(), ContainerHost<LandSearchState, LandSideEffect> {
    override val container = container<LandSearchState, LandSideEffect>(LandSearchState()) {
        getLandListUseCase()
            .onSuccess { lands ->
                reduce {
                    state.copy(
                        allLands = lands.toUiModels(),
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

    fun onQueryChange(query: String) = blockingIntent {
        reduce {
            state.copy(query = query)
        }
    }

    fun onLandClick(landId: Int) = intent {
        val land = state.allLands.firstOrNull { it.id == landId } ?: return@intent
        postSideEffect(LandSideEffect.NavigateToDetail(land))
    }

    fun onBackClick() = intent {
        postSideEffect(LandSideEffect.NavigateBack)
    }
}
