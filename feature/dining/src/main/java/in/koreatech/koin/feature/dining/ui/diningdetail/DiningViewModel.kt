package `in`.koreatech.koin.feature.dining.ui.diningdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.network.service.NetworkConnectivityService
import `in`.koreatech.koin.core.onboarding.OnboardingManager
import `in`.koreatech.koin.core.onboarding.OnboardingType
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.coopshop.SyncCoopShopUseCase
import `in`.koreatech.koin.domain.usecase.dining.GetNotOperationFilteredDiningUseCase
import `in`.koreatech.koin.domain.usecase.dining.SyncDiningUseCase
import `in`.koreatech.koin.domain.usecase.notification.DeleteNotificationSubscriptionUseCase
import `in`.koreatech.koin.domain.usecase.notification.GetNotificationPermissionInfoUseCase
import `in`.koreatech.koin.domain.usecase.notification.UpdateNotificationSubscriptionDetailUseCase
import `in`.koreatech.koin.domain.usecase.notification.UpdateNotificationSubscriptionUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.feature.dining.navigation.INIT_DATE
import java.util.Date
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

@HiltViewModel
class DiningViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNotOperationFilteredDiningUseCase: GetNotOperationFilteredDiningUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val onboardingManager: OnboardingManager,
    private val getNotificationPermissionInfoUseCase: GetNotificationPermissionInfoUseCase,
    private val updateNotificationSubscriptionUseCase: UpdateNotificationSubscriptionUseCase,
    private val updateNotificationSubscriptionDetailUseCase: UpdateNotificationSubscriptionDetailUseCase,
    private val deleteNotificationSubscriptionUseCase: DeleteNotificationSubscriptionUseCase,
    private val networkConnectivityService: NetworkConnectivityService,
    private val syncDiningUseCase: SyncDiningUseCase,
    private val syncCoopShopUseCase: SyncCoopShopUseCase
) : ViewModel(), ContainerHost<DiningState, DiningSideEffect> {

    private val initDate = savedStateHandle.get<String>(INIT_DATE)
        .takeUnless { it.isNullOrBlank() }
        ?: TimeUtil.dateFormatToYYMMDD(DiningUtil.getCurrentDate())

    override val container = container<DiningState, DiningSideEffect>(DiningState(selectedDate = initDate)) {
        syncDining()
        syncCoopShop()
    }

    private val _userState: StateFlow<User> = getUserStatusUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = User.Anonymous
    )
    val userState: StateFlow<User> get() = _userState

    private var fetchDiningJob: Job? = null

    private fun syncDining() {
        if (!networkConnectivityService.isConnected()) return
        viewModelScope.launch {
            syncDiningUseCase().onFailure { Timber.e("Dining sync failed: $it") }
        }
    }

    private fun syncCoopShop() {
        if (!networkConnectivityService.isConnected()) return
        viewModelScope.launch {
            syncCoopShopUseCase().onFailure { Timber.e("CoopShop sync failed: $it") }
        }
    }

    fun setSelectedDate(date: Date) {
        val formattedDate = TimeUtil.dateFormatToYYMMDD(date)
        intent {
            reduce { state.copy(selectedDate = formattedDate) }
            postSideEffect(DiningSideEffect.FetchDining(false))
        }
    }

    fun refreshDining() {
        intent {
            reduce { state.copy(isDiningRefreshing = true) }
            postSideEffect(DiningSideEffect.FetchDining(networkConnectivityService.isConnected()))
        }
    }

    fun getDining() = intent {
        postSideEffect(DiningSideEffect.FetchDining(false))
    }

    fun fetchDining(forceRefresh: Boolean = false) {
        fetchDiningJob?.cancel()
        fetchDiningJob = intent {
            reduce { state.copy(isLoading = true) }
            getNotOperationFilteredDiningUseCase(state.selectedDate, forceRefresh).catch {
                reduce { state.copy(dining = persistentListOf(), isLoading = false, isDiningRefreshing = false) }
            }.collectLatest { result ->
                reduce {
                    state.copy(
                        dining = result.toImmutableList(),
                        isLoading = false,
                        isDiningRefreshing = false
                    )
                }
            }
        }
    }

    fun getInitialPage(): Int = getDiningTabByType(DiningUtil.getCurrentType())

    private fun getDiningTabByType(type: DiningType): Int {
        return when (type) {
            DiningType.Breakfast -> 0
            DiningType.Lunch -> 1
            DiningType.Dinner -> 2
            DiningType.NextBreakfast -> 0
        }
    }

    fun getShowBottomSheetValue() {
        if (userState.value.isAnonymous) return
        intent {
            if (onboardingManager.getShouldOnboard(OnboardingType.DINING_NOTIFICATION)) {
                reduce { state.copy(showBottomSheet = true) }
                onboardingManager.updateShouldOnboard(OnboardingType.DINING_NOTIFICATION, false)
            }
        }
    }

    fun getNotificationPermissionInfo() {
        if (userState.value.isAnonymous) return
        intent {
            getNotificationPermissionInfoUseCase().onSuccess { info ->
                var soldOutSubscribed = state.isSoldOutSubscribed
                var diningImageSubscribed = state.isDiningImageSubscribed
                info.subscribes.forEach {
                    when (it.type) {
                        SubscribesType.DINING_SOLD_OUT -> soldOutSubscribed = it.isPermit
                        SubscribesType.DINING_IMAGE_UPLOAD -> diningImageSubscribed = it.isPermit
                        SubscribesType.NOTHING -> Unit
                        else -> Unit
                    }
                }
                reduce {
                    state.copy(
                        isSoldOutSubscribed = soldOutSubscribed,
                        isDiningImageSubscribed = diningImageSubscribed
                    )
                }
            }
        }
    }

    fun changeIsSoldOutSubscribed(boolean: Boolean) = intent {
        reduce { state.copy(isSoldOutSubscribed = boolean) }
        if (userState.value.isAnonymous) return@intent
        val result = if (boolean) {
            updateNotificationSubscriptionUseCase(SubscribesType.DINING_SOLD_OUT).mapCatching {
                updateNotificationSubscriptionDetailUseCase(SubscribesDetailType.BREAKFAST).getOrThrow()
                updateNotificationSubscriptionDetailUseCase(SubscribesDetailType.LUNCH).getOrThrow()
                updateNotificationSubscriptionDetailUseCase(SubscribesDetailType.DINNER).getOrThrow()
            }
        } else {
            deleteNotificationSubscriptionUseCase(SubscribesType.DINING_SOLD_OUT)
        }
        result.onFailure {
            reduce { state.copy(isSoldOutSubscribed = !boolean) }
        }
    }

    fun changeIsDiningImageSubscribed(boolean: Boolean) = intent {
        reduce { state.copy(isDiningImageSubscribed = boolean) }
        if (userState.value.isAnonymous) return@intent
        val result = if (boolean) {
            updateNotificationSubscriptionUseCase(SubscribesType.DINING_IMAGE_UPLOAD)
        } else {
            deleteNotificationSubscriptionUseCase(SubscribesType.DINING_IMAGE_UPLOAD)
        }
        result.onFailure {
            reduce { state.copy(isDiningImageSubscribed = !boolean) }
        }
    }
}
