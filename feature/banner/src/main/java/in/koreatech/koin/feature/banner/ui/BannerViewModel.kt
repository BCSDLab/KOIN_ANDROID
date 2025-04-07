package `in`.koreatech.koin.feature.banner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.abtest.Experiment
import `in`.koreatech.koin.domain.usecase.banner.GetBannersByCategoryUseCase
import `in`.koreatech.koin.domain.usecase.banner.SetBannerRefusalUseCase
import `in`.koreatech.koin.domain.usecase.user.ABTestUseCase
import `in`.koreatech.koin.domain.usecase.version.GetCurrentVersionCodeUseCase
import `in`.koreatech.koin.feature.banner.model.BannerState
import `in`.koreatech.koin.feature.banner.model.toLocalBanner
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class BannerViewModel @Inject constructor(
    private val getBannersByCategoryUseCase: GetBannersByCategoryUseCase,
    private val getCurrentVersionCodeUseCase: GetCurrentVersionCodeUseCase,
    private val saveBannerRefusalUseCase: SetBannerRefusalUseCase,
    private val abTestUseCase: ABTestUseCase
) : ViewModel() {
    private val _bannerState = MutableStateFlow(BannerState())
    val bannerState: StateFlow<BannerState> = _bannerState.asStateFlow()

    init {
        fetchCurrentVersionCode()
        fetchBanners()
    }

    val mainBannerABTestExperimentGroup =
        flow {
            abTestUseCase(Experiment.MAIN_BANNER_UI.experimentTitle).onSuccess {
                emit(it)
            }.onFailure {
                emit(Experiment.MAIN_BANNER_UI.experimentGroups.first())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        ).filterNotNull()

    private fun fetchBanners() = viewModelScope.launch {
        getBannersByCategoryUseCase(MAIN_BANNER_CATEGORY).collectLatest {
            _bannerState.value = _bannerState.value.copy(
                bannerList = it.map { banner -> banner.toLocalBanner() }.toImmutableList(),
                isLoading = false
            )
        }
    }

    private fun fetchCurrentVersionCode() = viewModelScope.launch {
        getCurrentVersionCodeUseCase().onSuccess {
            _bannerState.value = _bannerState.value.copy(
                currentVersionCode = it
            )
        }.onFailure {
            Timber.e(it)
        }
    }

    fun setBannerRefusal() = viewModelScope.launch {
        saveBannerRefusalUseCase()
    }

    companion object {
        const val MAIN_BANNER_CATEGORY = 1
    }
}
