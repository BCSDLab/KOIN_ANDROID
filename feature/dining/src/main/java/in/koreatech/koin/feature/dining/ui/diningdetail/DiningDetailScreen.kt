package `in`.koreatech.koin.feature.dining.ui.diningdetail

import android.content.Context
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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.request.ImageRequest
import `in`.koreatech.koin.core.abtest.ExperimentGroup
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.tab.KoinTabRow
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.navigation.utils.rememberNavigator
import `in`.koreatech.koin.core.onboarding.ArrowDirection
import `in`.koreatech.koin.core.onboarding.OnboardingType
import `in`.koreatech.koin.core.onboarding.rememberOnboardingManager
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.feature.dining.R
import `in`.koreatech.koin.feature.dining.component.DiningDateItem
import `in`.koreatech.koin.feature.dining.component.DiningItem
import `in`.koreatech.koin.feature.dining.component.DiningItemOriginal
import `in`.koreatech.koin.feature.dining.component.bottomsheet.DiningBottomSheet
import `in`.koreatech.koin.feature.dining.component.dialog.DiningImageDialog
import `in`.koreatech.koin.feature.dining.ui.diningdetail.scroll.diningScrollConnection
import java.util.Date
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiningDetailScreen(
    viewModel: DiningViewModel = hiltViewModel(),
    initialPage: Int = -1,
    onTopbarBackClick: () -> Unit = {},
    onTopbarActionClick: () -> Unit = {}
) {
    val userState by viewModel.userState.collectAsState()

    val selectedDate by viewModel.selectedDate.collectAsState()

    val showBottomSheet by viewModel.showBottomSheet.collectAsState()

    val isSoldOutSubscribed by viewModel.isSoldOutSubscribed.collectAsState()

    val isDiningImageSubscribed by viewModel.isDiningImageSubscribed.collectAsState()

    val diningList by viewModel.dining.collectAsState()

    val abTestExperimentGroup by viewModel.abTestExperimentGroup.collectAsState()

    LaunchedEffect(userState) { // userState NPE error in viewModel init{}; Flow is null
        viewModel.getShowBottomSheetValue()
        viewModel.getNotificationPermissionInfo()
    }

    Scaffold(
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.dining_appbar_title),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = KoinTheme.colors.primary500,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
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
                        painter = painterResource(R.drawable.ic_notice),
                        contentDescription = ""
                    )
                },
                onNavigationIconClick = onTopbarBackClick
            )
        }
    ) { contentPadding ->
        DiningDetailScreenImpl(
            diningList = diningList,
            contentPadding = contentPadding,
            selectedDate = TimeUtil.stringToDateYYMMDD(selectedDate),
            showBottomSheet = showBottomSheet,
            experimentGroup = abTestExperimentGroup,
            userState = userState,
            initialPage = if (initialPage != -1) initialPage else viewModel.getInitialPage(),
            isSoldOutSubscribed = isSoldOutSubscribed,
            isDiningImageSubscribed = isDiningImageSubscribed,
            onDateClick = viewModel::setSelectedDate,
            changeSoldOutSubscribe = viewModel::changeIsSoldOutSubscribed,
            changeDiningImageSubscribe = viewModel::changeIsDiningImageSubscribed,
            getNotificationPermitInfo = viewModel::getNotificationPermissionInfo,
            onShareClick = viewModel::shareDining
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiningDetailScreenImpl(
    diningList: List<Dining>,
    contentPadding: PaddingValues,
    selectedDate: Date,
    showBottomSheet: Boolean,
    experimentGroup: String,
    userState: User,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    isSoldOutSubscribed: Boolean = false,
    isDiningImageSubscribed: Boolean = false,
    initialPage: Int = 0,
    onDateClick: (Date) -> Unit = {},
    changeSoldOutSubscribe: (Boolean) -> Unit = {},
    changeDiningImageSubscribe: (Boolean) -> Unit = {},
    getNotificationPermitInfo: () -> Unit = {},
    onShareClick: (Dining, Context) -> Unit = { _, _ -> }
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val onboardingManager = rememberOnboardingManager()
    var showToolTip by remember { mutableStateOf(false) }
    LaunchedEffect(onboardingManager) {
        showToolTip = onboardingManager.getShouldOnboard(OnboardingType.DINING_SHARE)
    }

    val navigator = rememberNavigator()

    val tabSize = 3
    val tabList = DiningType.entries.take(tabSize).map { it.typeKorean }

    val pagerState = rememberPagerState(initialPage = initialPage) { tabList.size }

    val breakfastScrollState = rememberScrollState()
    val lunchScrollState = rememberScrollState()
    val dinnerScrollState = rememberScrollState()

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

    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            sheetState.show()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        getNotificationPermitInfo()
    }

    if (sheetState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch { sheetState.hide() }
            },
            dragHandle = {}, // to delete drag Handle
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            DiningBottomSheet(
                soldOutChecked = isSoldOutSubscribed,
                imageUploadChecked = isDiningImageSubscribed,
                onDismiss = { scope.launch { sheetState.hide() } },
                onPositive = {
                    if (!userState.isAnonymous) {
                        navigator.navigateToNotificationSetting(context).let {
                            launcher.launch(it)
                        }
                    }
                },
                onSoldOutChange = changeSoldOutSubscribe,
                onImageUploadChange = changeDiningImageSubscribe
            )
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
    val minToolbarHeight = 0.dp
    val maxToolbarHeightPx = with(density) { maxToolbarHeight.toPx() }
    val minToolbarHeightPx = with(density) { minToolbarHeight.toPx() }

    val toolbarOffsetPx = remember { mutableFloatStateOf(0f) }

    val toolbarHeight = lerp(
        maxToolbarHeight,
        minToolbarHeight,
        -toolbarOffsetPx.floatValue / (maxToolbarHeightPx - minToolbarHeightPx)
    )
    val animatedToolbarHeight by animateDpAsState(
        targetValue = toolbarHeight,
        animationSpec = tween(durationMillis = 50)
    )

    val nestedScrollConnection = remember {
        diningScrollConnection(
            currentScrollState = currentScrollState,
            toolbarOffsetPx = toolbarOffsetPx,
            maxToolbarHeightPx = maxToolbarHeightPx,
            minToolbarHeightPx = minToolbarHeightPx
        )
    }

    Column(
        modifier = modifier
            .padding(contentPadding)
            .consumeWindowInsets(contentPadding)
            .systemBarsPadding()
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
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
            titles = tabList.map { it }
        )
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .background(color = KoinTheme.colors.neutral200),
            state = pagerState,
            verticalAlignment = Alignment.Top
        ) { page ->
            val diningFilterList by remember(diningList, page) {
                derivedStateOf {
                    when (tabList[page]) {
                        DiningType.Breakfast.typeKorean -> diningList.filter { it.type == DiningType.Breakfast.typeEnglish }
                        DiningType.Lunch.typeKorean -> diningList.filter { it.type == DiningType.Breakfast.typeEnglish }
                        DiningType.Dinner.typeKorean -> diningList.filter { it.type == DiningType.Breakfast.typeEnglish }
                        else -> listOf()
                    }
                }
            }
            Column(
                modifier = Modifier
                    .verticalScroll(currentScrollState.value)
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                diningFilterList.forEachIndexed { index, dining ->
                    Box(
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        DiningItemByABTest(
                            experimentGroup = experimentGroup,
                            dining = dining,
                            context = context,
                            onImageClick = {
                                selectedImage = dining.imageUrl
                                showImageDialog = true
                            },
                            onShareClick = {
                                onShareClick(dining, context)
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
            }
        }
    }
}

@Composable
private fun DiningItemByABTest(
    experimentGroup: String,
    dining: Dining,
    context: Context,
    onImageClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
) {
    when (experimentGroup) {
        ExperimentGroup.SHARE_NEW -> {
            DiningItem(
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                dining = dining,
                context = context,
                onImageClick = onImageClick,
                onShareClick = onShareClick
            )
        }
        ExperimentGroup.SHARE_ORIGINAL -> {
            DiningItemOriginal(
                dining = dining,
                context = context,
                onImageClick = onImageClick,
                onShareClick = onShareClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DiningScreenPreview() {
    DiningDetailScreenImpl(
        diningList = listOf(
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
        userState = User.Anonymous,
        contentPadding = PaddingValues(),
        context = LocalContext.current,
        selectedDate = TimeUtil.getNextDayDate(TimeUtil.getCurrentTime()),
        showBottomSheet = false,
        experimentGroup = ExperimentGroup.SHARE_NEW
    )
}
