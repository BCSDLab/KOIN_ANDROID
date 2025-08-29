package `in`.koreatech.koin.feature.dining.ui.diningdetail

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.ItemContent
import com.kakao.sdk.template.model.ItemInfo
import com.kakao.sdk.template.model.Link
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.abtest.Experiment
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.onboarding.OnboardingManager
import `in`.koreatech.koin.core.onboarding.OnboardingType
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningPlace
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.dining.GetDiningUseCase
import `in`.koreatech.koin.domain.usecase.notification.DeleteNotificationSubscriptionUseCase
import `in`.koreatech.koin.domain.usecase.notification.GetNotificationPermissionInfoUseCase
import `in`.koreatech.koin.domain.usecase.notification.UpdateNotificationSubscriptionDetailUseCase
import `in`.koreatech.koin.domain.usecase.notification.UpdateNotificationSubscriptionUseCase
import `in`.koreatech.koin.domain.usecase.user.ABTestUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.domain.util.onSuccess
import `in`.koreatech.koin.feature.dining.constants.PARAMS_DATE
import `in`.koreatech.koin.feature.dining.constants.PARAMS_PLACE
import `in`.koreatech.koin.feature.dining.constants.PARAMS_TYPE
import `in`.koreatech.koin.feature.dining.navigation.INIT_DATE
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class DiningViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDiningUseCase: GetDiningUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val abTestUseCase: ABTestUseCase,
    private val onboardingManager: OnboardingManager,
    private val getNotificationPermissionInfoUseCase: GetNotificationPermissionInfoUseCase,
    private val updateNotificationSubscriptionUseCase: UpdateNotificationSubscriptionUseCase,
    private val updateNotificationSubscriptionDetailUseCase: UpdateNotificationSubscriptionDetailUseCase,
    private val deleteNotificationSubscriptionUseCase: DeleteNotificationSubscriptionUseCase
) : BaseViewModel() {

    private val initDate = savedStateHandle.get<String>(INIT_DATE)
        .takeUnless { it.isNullOrBlank() }
        ?: TimeUtil.dateFormatToYYMMDD(DiningUtil.getCurrentDate())

    init {
        getDining(initDate)
    }

    private val _userState: StateFlow<User> = getUserStatusUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = User.Anonymous
    )
    val userState: StateFlow<User> get() = _userState

    private val _selectedDate = MutableStateFlow(initDate)
    val selectedDate: StateFlow<String> get() = _selectedDate

    private val _dining = MutableStateFlow<List<Dining>>(emptyList())
    val dining: StateFlow<List<Dining>> get() = _dining

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> get() = _showBottomSheet

    private val _isSoldOutSubscribed = MutableStateFlow(false)
    val isSoldOutSubscribed: StateFlow<Boolean> get() = _isSoldOutSubscribed

    private val _isDiningImageSubscribed = MutableStateFlow(false)
    val isDiningImageSubscribed: StateFlow<Boolean> get() = _isDiningImageSubscribed

    val abTestExperimentGroup = flow {
        abTestUseCase(Experiment.DINING_SHARE.experimentTitle).onSuccess {
            emit(it)
        }.onFailure {
            emit(Experiment.DINING_SHARE.experimentGroups.first())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Experiment.DINING_SHARE.experimentGroups.first()
    )

    fun setSelectedDate(date: Date) {
        _selectedDate.value = TimeUtil.dateFormatToYYMMDD(date)
        getDining(selectedDate.value)
    }

    private fun getDining(date: String = selectedDate.value) {
        if (isLoading.value == false) {
            viewModelScope.launchWithLoading {
                getDiningUseCase(date)
                    .onSuccess {
                        _dining.value = it.filter { dining ->
                            dining.place == DiningPlace.CornerA.place ||
                                dining.place == DiningPlace.CornerB.place ||
                                dining.place == DiningPlace.CornerC.place
                        }
                    }
                    .onFailure {
                        _dining.value = listOf()
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
        viewModelScope.launch {
            if (onboardingManager.getShouldOnboard(OnboardingType.DINING_NOTIFICATION)) {
                _showBottomSheet.value = true
                onboardingManager.updateShouldOnboard(OnboardingType.DINING_NOTIFICATION, false)
            }
        }
    }

    fun shareDining(dining: Dining, context: Context) {
        EventLogger.logClickEvent(
            EventAction.CAMPUS,
            AnalyticsConstant.Label.MENU_SHARE,
            "공유하기"
        )
        val messageTemplate = createFeedMessageTemplate(dining)

        if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
            ShareClient.instance.shareDefault(
                context,
                messageTemplate
            ) { sharingResult, error ->
                error?.printStackTrace()
                sharingResult?.let {
                    context.startActivity(it.intent)
                }
            }
        }
    }

    private fun createFeedMessageTemplate(dining: Dining): FeedTemplate {
        val executionParams = mapOf(
            PARAMS_DATE to dining.date,
            PARAMS_TYPE to dining.type,
            PARAMS_PLACE to dining.place
        )
        val link = Link(
            androidExecutionParams = executionParams,
            iosExecutionParams = executionParams
        )
        return FeedTemplate(
            content = Content(
                title = "ㅤ",
                imageUrl = dining.imageUrl,
                link = link
            ),
            itemContent = ItemContent(
                profileText = "${
                    if (TimeUtil.isToday(dining.date)) {
                        "오늘"
                    } else if (TimeUtil.isTomorrow(dining.date)) {
                        "내일"
                    } else {
                        TimeUtil.formatDateToKorean(dining.date)
                    }
                } ${DiningUtil.getKoreanName(dining.type)} 식단",
                items = listOf(
                    ItemInfo(
                        item = dining.place,
                        itemOp = dining.menu.joinToString(", ")
                    )
                )
            ),
            buttons = listOf(
                Button("코인에서 식단 전체보기", link)
            )
        )
    }

    fun getNotificationPermissionInfo() {
        if (userState.value.isAnonymous) return
        viewModelScope.launch {
            getNotificationPermissionInfoUseCase().onSuccess { info ->
                info.subscribes.forEach {
                    when (it.type) {
                        SubscribesType.DINING_SOLD_OUT ->
                            _isSoldOutSubscribed.value = it.isPermit
                        SubscribesType.DINING_IMAGE_UPLOAD ->
                            _isDiningImageSubscribed.value = it.isPermit
                        SubscribesType.NOTHING -> Unit
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun onSoldOutSubscribe(boolean: Boolean) {
        if (userState.value.isAnonymous) return
        viewModelScope.launch {
            if (boolean) {
                updateNotificationSubscriptionUseCase(SubscribesType.DINING_SOLD_OUT)
                updateNotificationSubscriptionDetailUseCase(SubscribesDetailType.BREAKFAST)
                updateNotificationSubscriptionDetailUseCase(SubscribesDetailType.LUNCH)
                updateNotificationSubscriptionDetailUseCase(SubscribesDetailType.DINNER)
            } else {
                deleteNotificationSubscriptionUseCase(SubscribesType.DINING_SOLD_OUT)
            }
        }
    }

    private fun onDiningImageSubscribe(boolean: Boolean) {
        if (userState.value.isAnonymous) return
        viewModelScope.launch {
            if (boolean) {
                updateNotificationSubscriptionUseCase(SubscribesType.DINING_IMAGE_UPLOAD)
            } else {
                deleteNotificationSubscriptionUseCase(SubscribesType.DINING_IMAGE_UPLOAD)
            }
        }
    }

    fun changeIsSoldOutSubscribed(boolean: Boolean) {
        _isSoldOutSubscribed.value = boolean
        onSoldOutSubscribe(boolean)
    }

    fun changeIsDiningImageSubscribed(boolean: Boolean) {
        _isDiningImageSubscribed.value = boolean
        onDiningImageSubscribe(boolean)
    }
}
