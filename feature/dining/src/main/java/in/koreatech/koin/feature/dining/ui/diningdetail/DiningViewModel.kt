package `in`.koreatech.koin.feature.dining.ui.diningdetail

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.abtest.Experiment
import `in`.koreatech.koin.core.network.service.NetworkConnectivityService
import `in`.koreatech.koin.core.onboarding.OnboardingManager
import `in`.koreatech.koin.core.onboarding.OnboardingType
import `in`.koreatech.koin.domain.error.dining.KoinDiningException
import `in`.koreatech.koin.domain.model.dining.DiningPlace
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.domain.model.upload.PreSignedUrlDomain
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.coopshop.SyncCoopShopUseCase
import `in`.koreatech.koin.domain.usecase.dining.GetNotOperationFilteredDiningUseCase
import `in`.koreatech.koin.domain.usecase.dining.ReportDiningSoldOutUseCase
import `in`.koreatech.koin.domain.usecase.dining.SyncDiningUseCase
import `in`.koreatech.koin.domain.usecase.notification.DeleteNotificationSubscriptionUseCase
import `in`.koreatech.koin.domain.usecase.notification.GetNotificationPermissionInfoUseCase
import `in`.koreatech.koin.domain.usecase.notification.UpdateNotificationSubscriptionDetailUseCase
import `in`.koreatech.koin.domain.usecase.notification.UpdateNotificationSubscriptionUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.UploadImageUseCase
import `in`.koreatech.koin.domain.usecase.user.ABTestUseCase
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
import kotlinx.coroutines.flow.flow
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
    private val syncCoopShopUseCase: SyncCoopShopUseCase,
    private val abTestUseCase: ABTestUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val reportDiningSoldOutUseCase: ReportDiningSoldOutUseCase
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

    // AB Test will remove after experiment complete.
    // So, I didn't add to DiningState and separate it
    val diningSoldOutABTestExperimentGroup = flow {
        abTestUseCase(Experiment.DINING_SOLDOUT.experimentTitle).onSuccess {
            emit(it)
        }.onFailure {
            emit(Experiment.DINING_SOLDOUT.experimentGroups.first())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

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

    fun requestSoldOutReport(place: DiningPlace? = null) = intent {
        val operatingPlaces = state.dining.getOperatingPlaces()
        if (operatingPlaces.isEmpty() || (place != null && place !in operatingPlaces)) {
            postSideEffect(DiningSideEffect.DiningSoldOutNotOperationTime)
            return@intent
        }
        reduce {
            state.copy(
                diningReportState = state.diningReportState.copy(
                    isDialogVisible = true,
                    diningPlace = place ?: state.diningReportState.diningPlace
                )
            )
        }
    }

    fun updateShowDiningSoldOutReportDialog(showDiningSoldOutReportDialog: Boolean) = intent {
        reduce { state.copy(diningReportState = state.diningReportState.copy(isDialogVisible = showDiningSoldOutReportDialog)) }
    }

    fun resetDiningSoldOutState() = intent {
        reduce { state.copy(diningReportState = DiningReportState()) }
    }

    fun updateSoldOutReportSelectedPlace(place: DiningPlace) = intent {
        reduce { state.copy(diningReportState = state.diningReportState.copy(diningPlace = place)) }
    }

    fun uploadSoldOutImage(
        fileSize: Long,
        fileType: String,
        fileName: String,
        imageUri: Uri
    ) = intent {
        reduce { state.copy(diningReportState = state.diningReportState.copy(isImageUploading = true)) }
        uploadImageUseCase(
            domain = PreSignedUrlDomain.COOP,
            contentLength = fileSize,
            contentType = fileType,
            fileName = fileName,
            imageUri = imageUri.toString()
        ).onSuccess {
            reduce {
                state.copy(diningReportState = state.diningReportState.copy(imageUrl = it, isImageUploading = false))
            }
        }.onFailure {
            reduce {
                state.copy(diningReportState = state.diningReportState.copy(isDialogVisible = false, isImageUploading = false))
            }
            postSideEffect(DiningSideEffect.DiningSoldOutUploadFailed)
        }
    }

    fun deleteSoldOutImage() = intent {
        reduce {
            state.copy(diningReportState = state.diningReportState.copy(imageUrl = null))
        }
    }

    fun onSoldOutReport() = intent {
        val currentType = DiningUtil.getCurrentType().let {
            if (it == DiningType.NextBreakfast) DiningType.Dinner else it
        }

        val selectedDining = state.dining.firstOrNull { it.type == currentType.typeEnglish && state.diningReportState.diningPlace?.place == it.place }

        if (selectedDining == null) {
            reduce {
                state.copy(diningReportState = state.diningReportState.copy(isDialogVisible = false))
            }
            postSideEffect(DiningSideEffect.DiningSoldOutReportNotFoundDining)
            return@intent
        }

        if (!selectedDining.isOperating()) {
            reduce {
                state.copy(diningReportState = state.diningReportState.copy(isDialogVisible = false))
            }
            postSideEffect(DiningSideEffect.DiningSoldOutNotOperationTime)
            return@intent
        }

        if (state.diningReportState.imageUrl == null) {
            reduce {
                state.copy(diningReportState = state.diningReportState.copy(isDialogVisible = false))
            }
            postSideEffect(DiningSideEffect.DiningSoldOutReportInvalidImage)
            return@intent
        }

        reportDiningSoldOutUseCase(selectedDining.id, state.diningReportState.imageUrl!!).onSuccess {
            reduce {
                state.copy(diningReportState = DiningReportState())
            }
            postSideEffect(DiningSideEffect.DiningSoldOutReportSuccess(state.diningReportState.diningPlace!!))
        }.onFailure { exception ->
            reduce {
                state.copy(diningReportState = state.diningReportState.copy(isDialogVisible = false))
            }
            when (exception) {
                is KoinDiningException.DiningReportDateNotAllowedException ->
                    postSideEffect(DiningSideEffect.DiningSoldOutReportDateNotAllowed)

                is KoinDiningException.InvalidReportImageException ->
                    postSideEffect(DiningSideEffect.DiningSoldOutReportInvalidImage)

                is KoinDiningException.NotFoundDiningException ->
                    postSideEffect(DiningSideEffect.DiningSoldOutReportNotFoundDining)

                is KoinDiningException.DiningAlreadySoldOutException ->
                    postSideEffect(DiningSideEffect.DiningSoldOutReportAlreadySoldOut)

                is KoinDiningException.DiningReportAlreadySubmittedException ->
                    postSideEffect(DiningSideEffect.DiningSoldOutReportAlreadySubmitted)

                else -> postSideEffect(DiningSideEffect.DiningSoldOutReportFailed)
            }
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
