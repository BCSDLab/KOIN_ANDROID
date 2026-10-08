package `in`.koreatech.koin.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.network.service.NetworkConnectivityService
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.callvan.GetRecruitingCallvanCountUseCase
import `in`.koreatech.koin.domain.usecase.dining.GetDiningWithOperationTimeUseCase
import `in`.koreatech.koin.domain.usecase.home.SyncHomeUseCase
import `in`.koreatech.koin.domain.usecase.notification.GetNotificationsFlowUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreCountUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreEventCountUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.domain.usecase.weather.GetWeatherUseCase
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.feature.home.mapper.toDiningPagerDataList
import `in`.koreatech.koin.feature.home.model.toLocalWeather
import javax.inject.Inject
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
@Suppress("LongParameterList")
class HomeViewModel @Inject constructor(
    private val getDiningWithOperationTimeUseCase: GetDiningWithOperationTimeUseCase,
    private val getStoreCountUseCase: GetStoreCountUseCase,
    private val getStoreEventCountUseCase: GetStoreEventCountUseCase,
    private val getRecruitingCallvanCountUseCase: GetRecruitingCallvanCountUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getNotificationsFlowUseCase: GetNotificationsFlowUseCase,
    private val syncHomeUseCase: SyncHomeUseCase,
    private val networkConnectivityService: NetworkConnectivityService
) : ViewModel(), ContainerHost<HomeState, HomeSideEffect> {
    override val container = container<HomeState, HomeSideEffect>(HomeState()) {
        syncHome()
        getDining()
        getStoreCount()
        getStoreEventCount()
        getRecruitingCallvanCount()
        getUserName()
        getWeather()
        observeNotifications()
    }

    private fun syncHome() {
        if (!networkConnectivityService.isConnected()) return
        viewModelScope.launch {
            syncHomeUseCase().onFailure { Timber.e(it) }
        }
    }

    private fun getWeather() = intent {
        getWeatherUseCase().onSuccess {
            reduce { state.copy(weather = it.toLocalWeather()) }
        }.onFailure {
            Timber.e(it)
        }
    }

    private fun getDining() = intent {
        getDiningWithOperationTimeUseCase(TimeUtil.dateFormatToYYMMDD(DiningUtil.getCurrentDate()))
            .catch { Timber.e(it) }
            .collect { data ->
                reduce {
                    state.copy(diningData = data.filter { it.type == DiningUtil.getCurrentType().typeEnglish }.map { it.toDiningPagerDataList() }.toImmutableList())
                }
            }
    }

    private fun getUserName() = intent {
        getUserInfoUseCase().onSuccess { user ->
            when (user) {
                is User.Student -> reduce { state.copy(username = user.name ?: state.username) }
                is User.General -> reduce { state.copy(username = user.name) }
                User.Anonymous -> {
                    // Do nothing
                }
            }
        }.onFailure {
            Timber.e(it)
        }
    }

    private fun getStoreCount() = intent {
        getStoreCountUseCase().onSuccess {
            reduce {
                state.copy(openShopCount = it.openCount, shopCount = it.totalCount)
            }
        }.onFailure {
            Timber.e(it)
        }
    }

    private fun getStoreEventCount() = intent {
        getStoreEventCountUseCase().onSuccess {
            reduce {
                state.copy(shopEventForKoin = it)
            }
        }.onFailure {
            Timber.e(it)
        }
    }

    private fun getRecruitingCallvanCount() = intent {
        getRecruitingCallvanCountUseCase().onSuccess {
            reduce {
                state.copy(callVanRecruitCount = it)
            }
        }.onFailure {
            Timber.e(it)
        }
    }

    private fun observeNotifications() = intent {
        getNotificationsFlowUseCase()
            .catch { Timber.e(it) }
            .collect { notifications ->
                reduce { state.copy(isNewNotificationReceived = notifications.any { !it.isRead }) }
            }
    }
}
