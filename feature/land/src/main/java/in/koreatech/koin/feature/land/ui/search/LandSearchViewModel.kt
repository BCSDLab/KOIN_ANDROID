package `in`.koreatech.koin.feature.land.ui.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.analytics.RealEstateAnalyticsConstant
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
        EventLogger.logClickEvent(
            EventAction.DA1,
            if (state.query.isBlank()) {
                RealEstateAnalyticsConstant.ROOM_LIST_DETAIL_ENTRY_CLICK
            } else {
                RealEstateAnalyticsConstant.ROOM_LIST_SEARCH_SUGGESTION_CLICK
            },
            land.name
        )
        postSideEffect(LandSideEffect.NavigateToDetail(land))
    }

    fun onSearchSubmit() = intent {
        if (state.query.isBlank()) return@intent
        EventLogger.logCustomEvent(
            EventAction.DA1.value,
            RealEstateAnalyticsConstant.CATEGORY_SUBMIT,
            RealEstateAnalyticsConstant.ROOM_LIST_SEARCH_SUBMIT,
            RealEstateAnalyticsConstant.VALUE_SEARCH
        )
    }

    fun onBackClick() = intent {
        postSideEffect(LandSideEffect.NavigateBack)
    }
}
