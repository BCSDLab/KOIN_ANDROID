package `in`.koreatech.koin.ui.dining.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.LikeActionType
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.dining.GetDiningUseCase
import `in`.koreatech.koin.domain.usecase.dining.ToggleLikeDiningUseCase
import `in`.koreatech.koin.domain.usecase.onboarding.dining.GetShouldShowNotificationOnBoarding
import `in`.koreatech.koin.domain.usecase.onboarding.dining.UpdateShouldShowNotificationOnBoarding
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.domain.util.onSuccess
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DiningViewModel @Inject constructor(
    private val getDiningUseCase: GetDiningUseCase,
    private val getShouldShowNotiOnBoardingUseCase: GetShouldShowNotificationOnBoarding,
    private val updateShouldShowNotiOnBoardingUseCase: UpdateShouldShowNotificationOnBoarding,
    private val toggleLikeDiningUseCase: ToggleLikeDiningUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : BaseViewModel() {

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> get() = _userState

    private val _showDiningNotificationOnBoarding = MutableStateFlow(false)
    val showDiningNotificationOnBoarding: StateFlow<Boolean> get() = _showDiningNotificationOnBoarding

    private val _selectedDate = MutableStateFlow(TimeUtil.dateFormatToYYMMDD(DiningUtil.getCurrentDate()))
    val selectedDate: StateFlow<String> get() = _selectedDate

    private val _dining =
        MutableStateFlow<List<Dining>>(emptyList())
    val dining: StateFlow<List<Dining>> get() = _dining

    init {
        getUser()
    }

    fun shouldShowNotificationOnBoarding() {
        viewModelScope.launchWithLoading {
            getShouldShowNotiOnBoardingUseCase()
                .onSuccess {
                    _showDiningNotificationOnBoarding.value = it
                }
                .onFailure {

                }
        }
    }

    fun updateShouldShowNotificationOnBoarding(shouldShow: Boolean = false) {
        viewModelScope.launchWithLoading {
            updateShouldShowNotiOnBoardingUseCase(shouldShow)
            _showDiningNotificationOnBoarding.value = shouldShow
        }
    }

    fun setSelectedDate(date: Date) {
        _selectedDate.value = TimeUtil.dateFormatToYYMMDD(date)
    }

    fun getDining(
        date: String = selectedDate.value
    ) {
        if (isLoading.value == false) {
            viewModelScope.launchWithLoading {
                getDiningUseCase(date)
                    .onSuccess {
                        _dining.value = it
                    }
                    .onFailure {

                    }
            }
        }
    }

    suspend fun toggleLikeDining(d: Dining): LikeActionType {
        return if (isLoggedIn().not())
            LikeActionType.LOGIN_REQUIRED
        else
            viewModelScope.async {
                var actionType: LikeActionType? = null
                toggleLikeDiningUseCase(d)
                    .onSuccess { action ->
                        actionType = action
                    }.onFailure {

                    }
                actionType!!
            }.await()

    }

    fun getUser() {
        viewModelScope.launch {
            getUserInfoUseCase()
                .onSuccess {
                    _userState.value = it
                }
        }
    }

    private fun isLoggedIn(): Boolean {
        return userState.value != null && userState.value?.isAnonymous == false
    }
}