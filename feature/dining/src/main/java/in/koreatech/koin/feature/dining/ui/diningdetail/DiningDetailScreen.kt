package `in`.koreatech.koin.feature.dining.ui.diningdetail

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button as UiButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.ItemContent
import com.kakao.sdk.template.model.ItemInfo
import com.kakao.sdk.template.model.Link
import `in`.koreatech.koin.core.abtest.ExperimentGroup
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.camera.ui.KoinCamera
import `in`.koreatech.koin.core.designsystem.component.chip.TextChip2
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipDefaults
import `in`.koreatech.koin.core.designsystem.component.snackbar.KoinSnackbarHost
import `in`.koreatech.koin.core.designsystem.component.snackbar.rememberKoinSnackbarHostState
import `in`.koreatech.koin.core.designsystem.component.tab.KoinTabRow
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar2
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.core.nestedscroll.rememberKoinNestedScrollHeaderState
import `in`.koreatech.koin.core.onboarding.ArrowDirection
import `in`.koreatech.koin.core.onboarding.OnboardingType
import `in`.koreatech.koin.core.onboarding.rememberOnboardingManager
import `in`.koreatech.koin.core.util.KoinCoilImageLoader
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningPlace
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.model.dining.DiningWithOperationTime
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.feature.dining.R
import `in`.koreatech.koin.feature.dining.component.BulletText
import `in`.koreatech.koin.feature.dining.component.DiningDateItem
import `in`.koreatech.koin.feature.dining.component.DiningItem
import `in`.koreatech.koin.feature.dining.component.bottomsheet.DiningBottomSheet
import `in`.koreatech.koin.feature.dining.component.dialog.DiningImageDialog
import `in`.koreatech.koin.feature.dining.constants.PARAMS_DATE
import `in`.koreatech.koin.feature.dining.constants.PARAMS_PLACE
import `in`.koreatech.koin.feature.dining.constants.PARAMS_TYPE
import `in`.koreatech.koin.feature.dining.mapper.toDining
import `in`.koreatech.koin.feature.dining.ui.diningdetail.scroll.DiningNestedScrollConnection
import java.io.File
import java.util.Date
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiningDetailScreen(
    viewModel: DiningViewModel = hiltViewModel(),
    initialPage: Int = -1,
    onTopbarBackClick: () -> Unit = {},
    onTopbarActionClick: () -> Unit = {}
) {
    val userState by viewModel.userState.collectAsState()

    val diningState by viewModel.collectAsState()
    val diningReportState = diningState.diningReportState
    val scope = rememberCoroutineScope()

    val snackbarHostState = rememberKoinSnackbarHostState()

    val reportAbTest by viewModel.diningSoldOutABTestExperimentGroup.collectAsState()

    val view = LocalView.current
    val activity = LocalActivity.current
    val context = LocalContext.current

    LaunchedEffect(Unit) { // userState NPE error in viewModel init{}; Flow is null
        viewModel.getDining()
        snapshotFlow { userState }
            .collect { state ->
                if (!state.isAnonymous) {
                    viewModel.getShowBottomSheetValue()
                    viewModel.getNotificationPermissionInfo()
                }
            }
    }

    if (!view.isInEditMode) {
        SideEffect {
            activity?.window?.let {
                WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = true
            }
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        handleSideEffect(
            sideEffect = sideEffect,
            context = context,
            scope = scope,
            fetchDining = { viewModel.fetchDining(it) },
            showSnackbar = { snackbarHostState.showSnackbar(it) }
        )
    }

    val sheetState = rememberModalBottomSheetState()
    val navigator = rememberNavigator()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        viewModel.getNotificationPermissionInfo()
    }

    val dining = remember(diningState.dining) { diningState.dining.map(DiningWithOperationTime::toDining).toImmutableList() }

    LaunchedEffect(diningState.showBottomSheet) {
        if (diningState.showBottomSheet) {
            sheetState.show()
        }
    }

    if (sheetState.isVisible) {
        ModalBottomSheet(
            contentWindowInsets = { WindowInsets(0, 0, 0, 0) },
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch { sheetState.hide() }
            },
            dragHandle = {}, // to delete drag Handle
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            DiningBottomSheet(
                soldOutChecked = diningState.isSoldOutSubscribed,
                imageUploadChecked = diningState.isDiningImageSubscribed,
                onDismiss = { scope.launch { sheetState.hide() } },
                onPositive = {
                    if (!userState.isAnonymous) {
                        navigator.navigateToNotificationSetting(context).let {
                            launcher.launch(it)
                        }
                    }
                },
                onSoldOutChange = viewModel::changeIsSoldOutSubscribed,
                onImageUploadChange = viewModel::changeIsDiningImageSubscribed
            )
        }
    }

    if (diningReportState.isDialogVisible) {
        DiningReportDialog(
            state = diningReportState,
            operatingPlaces = diningState.dining.getOperatingPlaces().toImmutableList(),
            onPlaceSelect = viewModel::updateSoldOutReportSelectedPlace,
            onImageUpload = viewModel::uploadSoldOutImage,
            onImageDelete = viewModel::deleteSoldOutImage,
            onReport = viewModel::onSoldOutReport,
            onDismissRequest = {
                viewModel.updateShowDiningSoldOutReportDialog(false)
                viewModel.resetDiningSoldOutState()
            }
        )
    }

    Scaffold(
        containerColor = KoinTheme.colors.neutral0,
        snackbarHost = {
            KoinSnackbarHost(
                hostState = snackbarHostState
            )
        },
        topBar = {
            KoinTopAppBar2(
                title = {
                    Text(
                        text = stringResource(R.string.dining_appbar_title),
                        style = RebrandKoinTheme.typography.medium18
                    )
                },
                actions = {
                    if (reportAbTest == ExperimentGroup.DINING_SOLDOUT_A) {
                        Icon(
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(24.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    viewModel.requestSoldOutReport()
                                },
                            imageVector = ImageVector.vectorResource(R.drawable.ic_dining_soldout_report),
                            tint = RebrandKoinTheme.colors.primary500,
                            contentDescription = stringResource(R.string.dining_sold_out_report_title)
                        )
                    }

                    Icon(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(24.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                EventLogger.logClickEvent(
                                    EventAction.CAMPUS,
                                    AnalyticsConstant.Label.CAFETERIA_INFO,
                                    "학생식당정보"
                                )
                                onTopbarActionClick()
                            },
                        imageVector = ImageVector.vectorResource(R.drawable.ic_notice),
                        contentDescription = ""
                    )
                },
                onNavigationIconClick = onTopbarBackClick
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.navigationBars)
    ) { contentPadding ->
        DiningDetailScreenImpl(
            diningList = dining,
            contentPadding = contentPadding,
            selectedDate = TimeUtil.stringToDateYYMMDD(diningState.selectedDate),
            reportAbTest = reportAbTest,
            isDiningRefreshing = diningState.isDiningRefreshing,
            initialPage = if (initialPage != -1) initialPage else viewModel.getInitialPage(),
            refreshDining = viewModel::refreshDining,
            onDateClick = viewModel::setSelectedDate,
            onReport = viewModel::requestSoldOutReport
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiningDetailScreenImpl(
    diningList: ImmutableList<Dining>,
    contentPadding: PaddingValues,
    selectedDate: Date,
    reportAbTest: String?,
    modifier: Modifier = Modifier,
    isDiningRefreshing: Boolean = false,
    initialPage: Int = 0,
    refreshDining: () -> Unit = {},
    onDateClick: (Date) -> Unit = {},
    onReport: (DiningPlace) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val onboardingManager = rememberOnboardingManager()
    var showToolTip by remember { mutableStateOf(false) }
    LaunchedEffect(onboardingManager) {
        showToolTip = onboardingManager.getShouldOnboard(OnboardingType.DINING_SHARE)
    }

    val tabSize = 3
    val tabList = DiningType.entries.take(tabSize).map { it.typeKorean }

    val pagerState = rememberPagerState(initialPage = initialPage) { tabList.size }

    val breakfastScrollState = rememberScrollState()
    val lunchScrollState = rememberScrollState()
    val dinnerScrollState = rememberScrollState()

    val isWeekend = remember(selectedDate) {
        TimeUtil.let { it.isWeekend(it.dateFormatToYYYYMMDD(selectedDate)) }
    }
    val currentDate = remember { TimeUtil.getCurrentTime() }
    val dates = remember(currentDate) {
        buildList {
            add(currentDate)
            repeat(3) {
                add(0, TimeUtil.getPreviousDayDate(first()))
            }
            repeat(3) {
                add(TimeUtil.getNextDayDate(last()))
            }
        }
    }
    val selectedPosition = remember(selectedDate, currentDate) {
        TimeUtil.getDateDifferenceInDays(selectedDate, currentDate) + 3
    }

    var showImageDialog by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf("") }

    var isUserScrolling by remember { mutableStateOf(true) }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.isScrollInProgress }
            .collect { isProgress ->
                if (isProgress) {
                    if (isUserScrolling) {
                        EventLogger.logScrollEvent(
                            EventAction.CAMPUS,
                            AnalyticsConstant.Label.MENU_TIME,
                            tabList[pagerState.currentPage]
                        )
                    } else {
                        isUserScrolling = true
                    }
                }
            }
    }

    if (showImageDialog) {
        DiningImageDialog(
            imageModel = ImageRequest.Builder(context)
                .data(selectedImage)
                .build(),
            onDismiss = { showImageDialog = false }
        )
    }

    val maxToolbarHeight = 105.dp

    val headerState = rememberKoinNestedScrollHeaderState(
        headerCollapsedHeight = 0.dp,
        headerExpandedHeight = maxToolbarHeight
    )
    // toolbar 높이 애니메이션 보존: currentHeaderHeightDp()를 target으로 삼고 tween(50)으로 감싼다.
    // snapOffset은 snapTo(즉각)이므로 animateDpAsState가 없으면 50ms tween 효과가 사라진다.
    val toolbarHeightTarget by headerState.currentHeaderHeightDp()
    val animatedToolbarHeight by animateDpAsState(
        targetValue = toolbarHeightTarget,
        animationSpec = tween(durationMillis = 50)
    )

    val currentScrollState = remember {
        derivedStateOf {
            when (tabList[pagerState.currentPage]) {
                DiningType.Breakfast.typeKorean -> breakfastScrollState
                DiningType.Lunch.typeKorean -> lunchScrollState
                DiningType.Dinner.typeKorean -> dinnerScrollState
                else -> breakfastScrollState
            }
        }
    }

    val nestedScrollConnection = remember(headerState, scope, currentScrollState) {
        DiningNestedScrollConnection(
            headerState = headerState,
            coroutineScope = scope,
            currentScrollState = currentScrollState
        )
    }

    Box(
        modifier = modifier
            .padding(contentPadding)
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .height(animatedToolbarHeight)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = -(maxToolbarHeight - animatedToolbarHeight) / 2),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                dates.forEachIndexed { index, date ->
                    DiningDateItem(
                        modifier = Modifier
                            .requiredHeight(maxToolbarHeight)
                            .padding(top = 24.dp, bottom = 16.dp),
                        date = date,
                        isSelected = selectedPosition == index,
                        onClick = onDateClick
                    )
                }
            }
            KoinTabRow(
                selectedTabIndex = pagerState.currentPage,
                onTabSelected = {
                    EventLogger.logClickEvent(
                        EventAction.CAMPUS,
                        AnalyticsConstant.Label.MENU_TIME,
                        tabList[it]
                    )
                    isUserScrolling = false
                    scope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                },
                indicatorColor = RebrandKoinTheme.colors.primary500,
                selectedTextColor = RebrandKoinTheme.colors.primary500,
                titles = tabList.map { it }
            )
            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = KoinTheme.colors.neutral200),
                state = pagerState,
                verticalAlignment = Alignment.Top
            ) { page ->
                val state = rememberPullToRefreshState()
                PullToRefreshBox(
                    isRefreshing = isDiningRefreshing,
                    onRefresh = refreshDining,
                    state = state,
                    indicator = {
                        Indicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            isRefreshing = isDiningRefreshing,
                            containerColor = KoinTheme.colors.neutral0,
                            color = KoinTheme.colors.neutral800,
                            state = state
                        )
                    }
                ) {
                    if (isDiningRefreshing) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .zIndex(2f)
                                .background(color = KoinTheme.colors.neutral200),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    val diningFilterList by remember(diningList, page) {
                        derivedStateOf {
                            when (tabList[page]) {
                                DiningType.Breakfast.typeKorean -> diningList.filter { it.type == DiningType.Breakfast.typeEnglish }
                                DiningType.Lunch.typeKorean -> diningList.filter { it.type == DiningType.Lunch.typeEnglish }
                                DiningType.Dinner.typeKorean -> diningList.filter { it.type == DiningType.Dinner.typeEnglish }
                                else -> listOf()
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .verticalScroll(
                                when (tabList[page]) { // Can't use currentScrollState.value; because all pages have to give each other scroll state, not same currentScrollState
                                    DiningType.Breakfast.typeKorean -> breakfastScrollState
                                    DiningType.Lunch.typeKorean -> lunchScrollState
                                    DiningType.Dinner.typeKorean -> dinnerScrollState
                                    else -> breakfastScrollState
                                }
                            )
                            .padding(vertical = 16.dp)
                            .navigationBarsPadding(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        diningFilterList.forEachIndexed { index, dining ->
                            Box(
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                DiningItem(
                                    modifier = Modifier.padding(horizontal = 24.dp),
                                    dining = dining,
                                    isWeekend = isWeekend,
                                    onImageClick = {
                                        EventLogger.logClickEvent(
                                            EventAction.CAMPUS,
                                            AnalyticsConstant.Label.MENU_IMAGE,
                                            DiningUtil.getKoreanName(dining.type) + "_" + dining.place
                                        )
                                        selectedImage = dining.imageUrl
                                        showImageDialog = true
                                    },
                                    onShareClick = {
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
                                        } else {
                                            Toast.makeText(context, context.getString(R.string.kakao_share_unable), Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    reportIcon = {
                                        if (reportAbTest == ExperimentGroup.DINING_SOLDOUT_B) {
                                            Icon(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .clickable(
                                                        indication = null,
                                                        interactionSource = remember { MutableInteractionSource() }
                                                    ) {
                                                        onReport(DiningPlace.entries.first { it.place == dining.place })
                                                    },
                                                imageVector = ImageVector.vectorResource(R.drawable.ic_dining_soldout_report),
                                                tint = RebrandKoinTheme.colors.primary500,
                                                contentDescription = stringResource(R.string.dining_sold_out_report_title)
                                            )
                                        }
                                    }
                                )
                                if (index == 0 && showToolTip) {
                                    val infiniteTransition = rememberInfiniteTransition()
                                    val offsetY by infiniteTransition.animateValue(
                                        initialValue = 15.dp,
                                        targetValue = 20.dp,
                                        typeConverter = Dp.VectorConverter,
                                        animationSpec = infiniteRepeatable(
                                            animation = tween(1000, easing = LinearEasing),
                                            repeatMode = RepeatMode.Reverse
                                        )
                                    )
                                    Box(
                                        modifier = Modifier.offset(y = -(offsetY))
                                    ) {
                                        with(onboardingManager) {
                                            ShowOnboardingTooltipIfNeeded(
                                                type = OnboardingType.DINING_SHARE,
                                                arrowDirection = ArrowDirection.TOP
                                            ) {
                                                Spacer(modifier = Modifier.fillMaxWidth())
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Text(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            text = stringResource(R.string.caution_dining_changeable),
                            style = KoinTheme.typography.medium13,
                            color = KoinTheme.colors.neutral400
                        )
                        Spacer(Modifier.height(75.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiningReportDialog(
    state: DiningReportState,
    operatingPlaces: ImmutableList<DiningPlace>,
    modifier: Modifier = Modifier,
    onPlaceSelect: (DiningPlace) -> Unit = {},
    onImageUpload: (Long, String, String, Uri) -> Unit = { _, _, _, _ -> },
    onImageDelete: () -> Unit = {},
    onReport: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val places = remember { DiningPlace.entries.filter { it != DiningPlace.Campus2 } }.toImmutableList()
    val currentType = remember {
        DiningUtil.getCurrentType().let {
            if (it == DiningType.NextBreakfast) DiningType.Dinner else it
        }
    }

    BasicAlertDialog(
        modifier = modifier
            .clip(RebrandKoinTheme.shapes.small)
            .background(RebrandKoinTheme.colors.neutral0)
            .padding(vertical = 16.dp, horizontal = 20.dp),
        onDismissRequest = onDismissRequest
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.dining_sold_out_report_title, currentType.typeKorean),
                    style = RebrandKoinTheme.typography.medium18.copy(fontWeight = FontWeight.SemiBold)
                )

                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(1.dp)
                        .noRippleClickable(onClick = onDismissRequest),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_close_round),
                    contentDescription = null,
                    tint = RebrandKoinTheme.colors.neutral800
                )
            }

            DiningReportDialogSelection(
                places = places,
                operatingPlaces = operatingPlaces,
                reportedPlaces = state.reportedPlace,
                selectedPlace = state.diningPlace,
                onPlaceSelect = onPlaceSelect
            )

            DiningReportDialogUpload(
                isImageUploading = state.isImageUploading,
                imageUrl = state.imageUrl,
                onImageUploadRequest = {
                    scope.launch(Dispatchers.IO) {
                        handleImage(context, it, onImageUpload)
                    }
                },
                onImageDelete = onImageDelete
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                UiButton(
                    contentPadding = PaddingValues(vertical = 12.dp, horizontal = 48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RebrandKoinTheme.colors.primary500,
                        contentColor = RebrandKoinTheme.colors.neutral0,
                        disabledContainerColor = RebrandKoinTheme.colors.neutral300,
                        disabledContentColor = RebrandKoinTheme.colors.neutral0
                    ),
                    shape = RebrandKoinTheme.shapes.small,
                    onClick = onReport,
                    enabled = state.isReportable
                ) {
                    Text(
                        text = stringResource(R.string.dining_sold_out_report_submit),
                        style = RebrandKoinTheme.typography.medium15.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun DiningReportDialogSelection(
    places: ImmutableList<DiningPlace>,
    operatingPlaces: ImmutableList<DiningPlace>,
    reportedPlaces: ImmutableList<DiningPlace>,
    selectedPlace: DiningPlace?,
    onPlaceSelect: (DiningPlace) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectableTextChipColor = TextChipDefaults.chipColors(
        selectedContainerColor = RebrandKoinTheme.colors.neutral0,
        selectedContentColor = RebrandKoinTheme.colors.primary500,
        unselectedContainerColor = RebrandKoinTheme.colors.neutral0,
        unselectedContentColor = RebrandKoinTheme.colors.neutral500
    )

    val unselectableTextChipColor = TextChipDefaults.chipColors(
        selectedContainerColor = RebrandKoinTheme.colors.neutral0,
        selectedContentColor = RebrandKoinTheme.colors.primary500,
        unselectedContainerColor = RebrandKoinTheme.colors.neutral0,
        unselectedContentColor = RebrandKoinTheme.colors.neutral300
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.dining_sold_out_report_course_title),
            style = RebrandKoinTheme.typography.medium15.copy(fontWeight = FontWeight.SemiBold)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            itemVerticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            places.fastForEach { place ->
                val isAlreadySoldOut = reportedPlaces.contains(place) || place !in operatingPlaces

                key(place) {
                    TextChip2(
                        title = place.place,
                        showClickRipple = !isAlreadySoldOut,
                        isSelected = !isAlreadySoldOut && selectedPlace == place,
                        chipColors = if (isAlreadySoldOut) unselectableTextChipColor else selectableTextChipColor,
                        border = TextChipDefaults.chipBorder(
                            selectedBorderStroke = BorderStroke(1.dp, RebrandKoinTheme.colors.primary500),
                            unselectedBorderStroke = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral300)
                        ),
                        onSelect = {
                            if (place in operatingPlaces) onPlaceSelect(place)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DiningReportDialogUpload(
    isImageUploading: Boolean,
    imageUrl: String?,
    onImageUploadRequest: (Uri?) -> Unit,
    onImageDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isCameraVisible by remember { mutableStateOf(false) }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        isCameraVisible = isGranted
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.dining_sold_out_report_photo_title),
            style = RebrandKoinTheme.typography.medium15.copy(fontWeight = FontWeight.SemiBold)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(15f)
                    .aspectRatio(3f / 4f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RebrandKoinTheme.shapes.small)
                        .background(RebrandKoinTheme.colors.neutral200)
                        .clickable {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                isCameraVisible = true
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        contentDescription = null,
                        tint = RebrandKoinTheme.colors.neutral500,
                        imageVector = ImageVector.vectorResource(R.drawable.ic_dining_soldout_report_camera)
                    )

                    Text(
                        text = stringResource(R.string.dining_sold_out_report_photo_action),
                        style = RebrandKoinTheme.typography.regular14,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                }

                if (isImageUploading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .size(24.dp)
                            .zIndex(2f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                imageUrl?.let {
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RebrandKoinTheme.shapes.small)
                            .zIndex(2f),
                        imageLoader = KoinCoilImageLoader.getImageLoader(context),
                        model = it,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .size(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .zIndex(3f)
                            .offset(x = 4.dp, y = (-4).dp)
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(RebrandKoinTheme.colors.primary500)
                            .padding(3.dp)
                    ) {
                        Icon(
                            modifier = Modifier
                                .fillMaxSize()
                                .noRippleClickable(onClick = onImageDelete),
                            imageVector = ImageVector.vectorResource(
                                R.drawable.ic_close_round
                            ),
                            contentDescription = null,
                            tint = RebrandKoinTheme.colors.neutral0
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(17f)
                    .padding(8.dp)
            ) {
                CompositionLocalProvider(
                    LocalTextStyle provides RebrandKoinTheme.typography.regular12,
                    LocalContentColor provides RebrandKoinTheme.colors.neutral500
                ) {
                    stringArrayResource(R.array.dining_sold_out_report_photo_guides).forEach { guide ->
                        BulletText(guide)
                    }
                }
            }
        }
    }

    if (isCameraVisible) {
        Dialog(
            onDismissRequest = { isCameraVisible = false },
            properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
        ) {
            KoinCamera(
                modifier = Modifier.fillMaxSize(),
                onCapture = { uri, _ ->
                    isCameraVisible = false
                    val contentUri = uri?.path?.let { path ->
                        FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            File(path)
                        )
                    }
                    onImageUploadRequest(contentUri)
                }
            )
        }
    }
}

private fun handleImage(
    context: Context,
    uri: Uri?,
    uploadImage: (Long, String, String, Uri) -> Unit
) {
    if (uri == null) return
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        if (!cursor.moveToFirst()) return@use

        val fileNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val fileSizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

        if (fileNameIndex == -1 || fileSizeIndex == -1) return@use

        val fileName = cursor.getString(fileNameIndex)
        val fileSize = cursor.getLong(fileSizeIndex)
        val fileType = context.contentResolver.getType(uri) ?: "image/${fileName.substringAfterLast(".")}"

        uploadImage(fileSize, fileType, fileName, uri)
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

private fun handleSideEffect(
    sideEffect: DiningSideEffect,
    context: Context,
    scope: CoroutineScope,
    fetchDining: (forceRefresh: Boolean) -> Unit,
    showSnackbar: suspend (String) -> Unit
) {
    when (sideEffect) {
        is DiningSideEffect.FetchDining -> {
            fetchDining(sideEffect.forceRefresh)
        }

        DiningSideEffect.DiningSoldOutUploadFailed -> {
            scope.launch { showSnackbar(context.getString(R.string.dining_sold_out_image_upload_failed)) }
        }

        is DiningSideEffect.DiningSoldOutReportSuccess -> {
            scope.launch {
                showSnackbar(context.getString(R.string.dining_sold_out_report_success_format, sideEffect.place.place))
            }
        }

        DiningSideEffect.DiningSoldOutReportFailed -> {
            scope.launch { showSnackbar(context.getString(R.string.dining_sold_out_report_failed)) }
        }

        DiningSideEffect.DiningSoldOutReportDateNotAllowed -> {
            scope.launch { showSnackbar(context.getString(R.string.dining_sold_out_report_date_not_allowed)) }
        }

        DiningSideEffect.DiningSoldOutReportInvalidImage -> {
            scope.launch { showSnackbar(context.getString(R.string.dining_sold_out_report_invalid_image)) }
        }

        DiningSideEffect.DiningSoldOutReportNotFoundDining -> {
            scope.launch { showSnackbar(context.getString(R.string.dining_sold_out_report_not_found_dining)) }
        }

        DiningSideEffect.DiningSoldOutReportAlreadySoldOut -> {
            scope.launch { showSnackbar(context.getString(R.string.dining_sold_out_report_already_sold_out)) }
        }

        DiningSideEffect.DiningSoldOutReportAlreadySubmitted -> {
            scope.launch { showSnackbar(context.getString(R.string.dining_sold_out_report_already_submitted)) }
        }

        DiningSideEffect.DiningSoldOutNotOperationTime -> {
            scope.launch { showSnackbar(context.getString(R.string.dining_sold_out_report_not_operation_time)) }
        }

        DiningSideEffect.DiningSoldOutNoDiningToReport -> {
            scope.launch { showSnackbar(context.getString(R.string.dining_sold_out_report_no_dining_to_report)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DiningScreenPreview() {
    DiningDetailScreenImpl(
        diningList = persistentListOf(
            Dining(
                id = 0,
                date = "2025.05.17",
                type = "BREAKFAST",
                place = "A코너",
                priceCard = "1000",
                priceCash = "1000",
                kcal = "786",
                menu = listOf("밥", "국", "김치", "아침"),
                imageUrl = "https://image.utoimage.com/preview/cp872722/2022/12/202212008462_500.jpg",
                createdAt = "2025.05.17",
                updatedAt = "2025.05.17",
                soldOutAt = "",
                changedAt = "2025.05.17"
            ),
            Dining(
                id = 0,
                date = "2025.05.17",
                type = "BREAKFAST",
                place = "B코너",
                priceCard = "1000",
                priceCash = "1000",
                kcal = "786",
                menu = listOf("밥", "국", "김치", "아침"),
                imageUrl = "",
                createdAt = "2025.05.17",
                updatedAt = "2025.05.17",
                soldOutAt = "",
                changedAt = "2025.05.17"
            ),
            Dining(
                id = 0,
                date = "2025.05.17",
                type = "아침",
                place = "LUNCH",
                priceCard = "1000",
                priceCash = "1000",
                kcal = "786",
                menu = listOf("밥", "국", "김치", "점심"),
                imageUrl = "",
                createdAt = "2025.05.17",
                updatedAt = "2025.05.17",
                soldOutAt = "",
                changedAt = "2025.05.17"
            ),
            Dining(
                id = 0,
                date = "2025.05.17",
                type = "DINNER",
                place = "A코너",
                priceCard = "1000",
                priceCash = "1000",
                kcal = "786",
                menu = listOf("밥", "국", "김치", "저녁"),
                imageUrl = "",
                createdAt = "2025.05.17",
                updatedAt = "2025.05.17",
                soldOutAt = "",
                changedAt = "2025.05.17"
            )
        ),
        contentPadding = PaddingValues(),
        selectedDate = TimeUtil.getNextDayDate(TimeUtil.getCurrentTime()),
        reportAbTest = null
    )
}
