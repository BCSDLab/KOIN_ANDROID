package `in`.koreatech.koin.feature.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.callvan.GetRecruitingCallvanCountUseCase
import `in`.koreatech.koin.domain.usecase.dining.GetDiningWithOperationTimeUseCase
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
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDiningWithOperationTimeUseCase: GetDiningWithOperationTimeUseCase,
    private val getStoreCountUseCase: GetStoreCountUseCase,
    private val getStoreEventCountUseCase: GetStoreEventCountUseCase,
    private val getRecruitingCallvanCountUseCase: GetRecruitingCallvanCountUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel(), ContainerHost<HomeState, HomeSideEffect> {
    override val container = container<HomeState, HomeSideEffect>(HomeState()) {
        getDining()
        getStoreCount()
        getStoreEventCount()
        getRecruitingCallvanCount()
        getUserName()
        getWeather()
    }

    private fun getWeather() = intent {
        getWeatherUseCase().onSuccess {
            reduce { state.copy(weather = it.toLocalWeather()) }
        }.onFailure {
            Timber.e(it)
        }
    }

    private fun getDining() = intent {
        getDiningWithOperationTimeUseCase(TimeUtil.dateFormatToYYMMDD(DiningUtil.getCurrentDate())).onSuccess { data ->
            reduce {
                state.copy(diningData = data.filter { it.type == DiningUtil.getCurrentType().typeEnglish }.map { it.toDiningPagerDataList() }.toImmutableList())
            }
        }.onFailure {
            Timber.e(it)
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
}
