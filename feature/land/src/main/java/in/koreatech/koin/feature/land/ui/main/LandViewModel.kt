package `in`.koreatech.koin.feature.land.ui.main

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
        if (state.filter == filter) return@intent
        EventLogger.logCustomEvent(
            EventAction.DA1.value,
            RealEstateAnalyticsConstant.CATEGORY_SELECT,
            RealEstateAnalyticsConstant.ROOM_LIST_CONTRACT_TYPE_SELECT,
            when (filter) {
                LandFilter.ALL -> RealEstateAnalyticsConstant.VALUE_FILTER_ALL
                LandFilter.MONTHLY -> RealEstateAnalyticsConstant.VALUE_FILTER_MONTHLY
                LandFilter.CHARTER -> RealEstateAnalyticsConstant.VALUE_FILTER_CHARTER
            }
        )
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

    fun onPreviewClick(landId: Int) = intent {
        val land = state.lands.firstOrNull { it.id == landId && state.filter.matches(it) } ?: return@intent
        EventLogger.logClickEvent(
            EventAction.DA1,
            RealEstateAnalyticsConstant.ROOM_LIST_PREVIEW_ENTRY_CLICK,
            land.name
        )
        reduce { state.copy(selectedLandId = land.id) }
    }

    fun onPreviewCloseClick() = intent {
        if (state.selectedLandId == null) return@intent
        EventLogger.logClickEvent(
            EventAction.DA1,
            RealEstateAnalyticsConstant.ROOM_LIST_PREVIEW_CLOSE_CLICK,
            RealEstateAnalyticsConstant.VALUE_CLOSE
        )
        reduce { state.copy(selectedLandId = null) }
    }

    fun onDetailClick(landId: Int) = intent {
        val land = state.lands.firstOrNull { it.id == landId } ?: return@intent
        EventLogger.logClickEvent(
            EventAction.DA1,
            RealEstateAnalyticsConstant.ROOM_LIST_DETAIL_BUTTON_ENTRY_CLICK,
            land.name
        )
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
