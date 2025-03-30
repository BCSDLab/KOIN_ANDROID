package `in`.koreatech.koin.feature.banner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.banner.GetBannerCategoriesUseCase
import `in`.koreatech.koin.domain.usecase.banner.GetBannersByCategoryUseCase
import `in`.koreatech.koin.domain.usecase.banner.SetBannerRefusalUseCase
import `in`.koreatech.koin.domain.usecase.version.GetCurrentVersionCodeUseCase
import `in`.koreatech.koin.feature.banner.model.BannerState
import `in`.koreatech.koin.feature.banner.model.toLocalBanner
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class BannerViewModel @Inject constructor(
    private val getBannersByCategoryUseCase: GetBannersByCategoryUseCase,
    private val getBannerCategoryUseCase: GetBannerCategoriesUseCase,
    private val getCurrentVersionCodeUseCase: GetCurrentVersionCodeUseCase,
    private val saveBannerRefusalUseCase: SetBannerRefusalUseCase
) : ViewModel() {
    private val _bannerState = MutableStateFlow(BannerState())
    val bannerState: StateFlow<BannerState> = _bannerState.asStateFlow()

    init {
        fetchCurrentVersionCode()
        fetchBannerCategory()
    }

    private fun fetchBannerCategory() = viewModelScope.launch {
        getBannerCategoryUseCase().collectLatest {
            _bannerState.value = _bannerState.value.copy(
                bannerCategory = it
            )
            _bannerState.value.bannerCategory.forEach { category ->
                fetchBannersByCategory(category.id)
            }
        }
    }

    private fun fetchBannersByCategory(category: Int) = viewModelScope.launch {
        getBannersByCategoryUseCase(category).collectLatest {
            _bannerState.value = _bannerState.value.copy(
                bannerList = it.map { banner -> banner.toLocalBanner() },
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
}
