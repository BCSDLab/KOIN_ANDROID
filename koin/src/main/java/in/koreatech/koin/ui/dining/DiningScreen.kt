package `in`.koreatech.koin.ui.dining

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.designsystem.component.tab.KoinTabRow
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.ui.dining.component.DiningDateItem
import `in`.koreatech.koin.ui.dining.component.DiningItem
import `in`.koreatech.koin.ui.dining.viewmodel.DiningViewModel
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiningScreen(
    viewModel: DiningViewModel = hiltViewModel(),
    initialPage: Int = 0
) {
    val userState by viewModel.userState.collectAsState()

    val selectedDate by viewModel.selectedDate.collectAsState()

    val diningList by viewModel.dining.collectAsState()

    Scaffold(
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = "식단",
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = KoinTheme.colors.primary500,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_notice),
                        contentDescription = ""
                    )
                }
            )
        }
    ) { contentPadding ->
        DiningScreenImpl(
            diningList = diningList,
            contentPadding = contentPadding,
            context = LocalContext.current,
            initialPage = initialPage
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiningScreenImpl(
    diningList: List<Dining>,
    contentPadding: PaddingValues,
    context: Context,
    modifier: Modifier = Modifier,
    initialPage: Int = 0,
) {
    val tabSize = 3
    val tabList = DiningType.entries.take(tabSize).map { it.typeKorean }
    var tabRowHeight by remember { mutableStateOf(0.dp) }

    val pagerState = rememberPagerState(initialPage = initialPage) { tabList.size }
    val scope = rememberCoroutineScope()

    val density = LocalDensity.current

    val listState = rememberLazyListState()

    LazyColumn(
        modifier = modifier
            .padding(contentPadding)
            .consumeWindowInsets(contentPadding)
            .systemBarsPadding()
            .fillMaxSize(),
        state = listState
    ) {
        item {
            val dates = mutableListOf<Date>()
            dates.clear()
            val currentDate = TimeUtil.getCurrentTime()
            dates.add(currentDate)
            repeat(3) {
                dates.add(0, TimeUtil.getPreviousDayDate(dates.first()))
            }
            repeat(3) {
                dates.add(TimeUtil.getNextDayDate(dates.last()))
            }
            Row(
                modifier = Modifier
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 24.dp,
                        bottom = 16.dp
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                dates.forEach { date ->
                    DiningDateItem(
                        date = date,
                        isSelected = false
                    )
                }
            }
        }
        stickyHeader {
            KoinTabRow(
                modifier = Modifier.zIndex(2f).onGloballyPositioned {
                    tabRowHeight = with(density) {
                        it.size.height.toDp()
                    }
                },
                selectedTabIndex = pagerState.currentPage,
                onTabSelected = {
                    scope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                },
                titles = tabList.map { it }
            )
        }
        item {
            val deviceHeightDp = LocalConfiguration.current.screenHeightDp.dp - (contentPadding.calculateTopPadding().value.dp + 24.dp) - tabRowHeight
            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize()
                    .height(deviceHeightDp)
                    .background(color = KoinTheme.colors.neutral100)
                    .padding(vertical = 16.dp, horizontal = 24.dp),
                state = pagerState,
                verticalAlignment = Alignment.Top,
            ) { page ->
                when (tabList[page]) {
                    DiningType.Breakfast.typeKorean -> {
                        val breakfastScrollState = rememberScrollState()
                        val isBreakfastScrollable = remember { derivedStateOf { !listState.canScrollForward || breakfastScrollState.value != 0 } }
                        Column(
                            modifier = Modifier
                                .verticalScroll(breakfastScrollState, enabled = isBreakfastScrollable.value),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            diningList.forEach { dining ->
                                if (dining.type == DiningType.Breakfast.typeEnglish) {
                                    DiningItem(
                                        dining = dining,
                                        context = context
                                    )
                                }
                            }
                        }
                    }
                    DiningType.Lunch.typeKorean -> {

                    }
                    DiningType.Dinner.typeKorean -> {

                    }
                }
            }
        }
    }
}

private fun getDiningTabByType(type: DiningType): Int {
    return when (type) {
        DiningType.Breakfast -> 0
        DiningType.Lunch -> 1
        DiningType.Dinner -> 2
        DiningType.NextBreakfast -> 0
    }
}

@Preview(showBackground = true)
@Composable
fun DiningScreenPreview() {
    DiningScreenImpl(
        diningList = listOf(
            Dining(
                id = 0,
                date = "2025.05.17",
                type = "BREAKFAST",
                place = "A코너",
                priceCard = "1000",
                priceCash = "1000",
                kcal = "786",
                menu = listOf("밥","국","김치","아침"),
                imageUrl = "",
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
                menu = listOf("밥","국","김치","아침"),
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
                menu = listOf("밥","국","김치","점심"),
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
                menu = listOf("밥","국","김치","저녁"),
                imageUrl = "",
                createdAt = "2025.05.17",
                updatedAt = "2025.05.17",
                soldOutAt = "",
                changedAt = "2025.05.17"
            )
        ),
        contentPadding = PaddingValues(),
        context = LocalContext.current
    )
}