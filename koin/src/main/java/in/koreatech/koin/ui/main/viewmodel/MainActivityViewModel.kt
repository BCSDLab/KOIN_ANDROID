package `in`.koreatech.koin.ui.main.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.usecase.banner.CheckBannerRefusalUseCase
import `in`.koreatech.koin.domain.usecase.dining.GetDiningUseCase
import `in`.koreatech.koin.domain.usecase.notification.GetNotificationsFlowUseCase
import `in`.koreatech.koin.domain.usecase.user.UpdateDeviceTokenUseCase
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.domain.util.ext.arrange
import `in`.koreatech.koin.domain.util.ext.typeFilter
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val updateDeviceTokenUseCase: UpdateDeviceTokenUseCase,
    private val getDiningUseCase: GetDiningUseCase,
    private val checkBannerRefusalUseCase: CheckBannerRefusalUseCase,
    private val getNotificationsFlowUseCase: GetNotificationsFlowUseCase
) : BaseViewModel() {
    private val _selectedPosition = MutableStateFlow(0)
    private val _diningData = MutableStateFlow<List<Dining>>(listOf())
    private val _selectedType = MutableStateFlow(DiningUtil.getCurrentType())

    private val _isBannerRefusal = MutableStateFlow<Boolean?>(null)
    val isBannerRefusal: StateFlow<Boolean?> get() = _isBannerRefusal

    val hasUnreadNotification: StateFlow<Boolean> = getNotificationsFlowUseCase()
        .map { notifications -> notifications.any { !it.isRead } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    init {
        checkBannerRefusal()
        updateDining()
    }

    fun updateDeviceToken() {
        viewModelScope.launch {
            try {
                updateDeviceTokenUseCase()
            } catch (e: Exception) {
                Timber.e("Failed Update Fcm Token : ${e.message}")
            }
        }
    }

    fun updateDining() {
        viewModelScope.launchWithLoading {
            getDiningUseCase(TimeUtil.dateFormatToYYMMDD(DiningUtil.getCurrentDate()))
                .onSuccess {
                    if (it.isNotEmpty()) {
                        _selectedType.value = DiningUtil.getCurrentType()
                    }
                    _diningData.value = it.typeFilter(DiningUtil.getCurrentType()).arrange()
                    _selectedPosition.value = 0
                    _isLoading.value = false
                }
                .onFailure {
                    _diningData.value = listOf()
                    _isLoading.value = false
                }
        }
    }

    private fun checkBannerRefusal() {
        viewModelScope.launchWithLoading {
            checkBannerRefusalUseCase().let {
                _isBannerRefusal.value = it
            }
        }
    }
}
