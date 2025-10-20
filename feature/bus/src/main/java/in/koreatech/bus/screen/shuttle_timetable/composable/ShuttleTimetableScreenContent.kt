package `in`.koreatech.bus.screen.shuttle_timetable.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import `in`.koreatech.bus.component.CommonFailureView
import `in`.koreatech.bus.component.ShuttleBusOperationChip
import `in`.koreatech.bus.component.WrongInformationText
import `in`.koreatech.bus.mock.shuttleTimetableUiStateMock1
import `in`.koreatech.bus.mock.shuttleTimetableUiStateMock2
import `in`.koreatech.bus.screen.shuttle_timetable.composable.loading.ShuttleTimetableLoading
import `in`.koreatech.bus.screen.shuttle_timetable.viewmodel.ShuttleTimetableUiState
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.core.designsystem.component.tab.KoinTabRow
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.getMeasuredKoreanHeightDp
import `in`.koreatech.koin.feature.bus.R
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShuttleTimetableScreenContent(
    timetableUiState: ShuttleTimetableUiState,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {}
) {
    val nodeItemHeightDp =
        KoinTheme.typography.medium15.getMeasuredKoreanHeightDp() + KoinTheme.typography.regular12.getMeasuredKoreanHeightDp()

    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    KoinSurface(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            KoinTopAppBar(
                title = stringResource(R.string.title_bus_timetable),
                onNavigationIconClick = onNavigationIconClick
            )

            Column(
                modifier =
                Modifier
                    .fillMaxSize()
            ) {
                when (timetableUiState) {
                    is ShuttleTimetableUiState.Success -> {
                        val eventValue = "${stringResource(
                            timetableUiState.timetable.routeType.simpleTitleRes
                        )}_${timetableUiState.timetable.routeName}"
                        Column(
                            modifier =
                            Modifier
                                .padding(horizontal = 24.dp, vertical = 16.dp)
                        ) {
                            ShuttleBusOperationChip(
                                operationType = timetableUiState.timetable.routeType
                            )

                            Text(
                                text = stringResource(R.string.timetable, timetableUiState.timetable.routeName),
                                style = KoinTheme.typography.bold20,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }

                        if (timetableUiState.timetable.showTabs()) {
                            KoinTabRow(
                                titles =
                                listOf(
                                    stringResource(R.string.tab_title_going),
                                    stringResource(R.string.tab_title_return)
                                ),
                                selectedTabIndex = pagerState.currentPage,
                                onTabSelected = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(it)
                                    }
                                }
                            )
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.Top,
                                flingBehavior =
                                PagerDefaults.flingBehavior(
                                    state = pagerState,
                                    snapPositionalThreshold = .1f
                                )
                            ) { page ->
                                Column(
                                    modifier =
                                    Modifier.fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    Box(
                                        modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(14.dp)
                                            .background(color = KoinTheme.colors.neutral100)
                                    )
                                    Row(
                                        modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .height(IntrinsicSize.Min)
                                    ) {
                                        ShuttleTimetableNodeItem(
                                            nodeItemHeightDp = nodeItemHeightDp,
                                            nodes =
                                            if (page == 0) {
                                                timetableUiState.timetable.nodeInfo.toPersistentList()
                                            } else {
                                                timetableUiState.timetable.nodeInfo.reversed().toPersistentList()
                                            }
                                        )

                                        VerticalDivider(
                                            modifier = Modifier.fillMaxHeight(),
                                            color = KoinTheme.colors.neutral300
                                        )

                                        Row(
                                            modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .horizontalScroll(rememberScrollState())
                                        ) {
                                            ShuttleTimetableRouteItem(
                                                route =
                                                if (page == 0) {
                                                    timetableUiState.timetable.routeInfo[page]
                                                } else {
                                                    timetableUiState.timetable.routeInfo[page].copy(
                                                        arrivalTimes = timetableUiState.timetable.routeInfo[page].arrivalTimes.reversed()
                                                    )
                                                },
                                                nodeItemHeightDp = nodeItemHeightDp
                                            )

                                            /** 시간표가 화면을 못 채울 때, 상단의 회색 배경을 채우기 위함 (프리뷰 참조)
                                             * 더 좋은 방법을 모르겠음... */
                                            Spacer(
                                                modifier =
                                                Modifier.weight(1f).height(
                                                    KoinTheme.typography.regular14.getMeasuredKoreanHeightDp() * 2
                                                ).background(KoinTheme.colors.neutral100)
                                            )
                                        }
                                    }
                                    WrongInformationText(
                                        modifier = Modifier.padding(top = 16.dp, start = 24.dp),
                                        loggingEventValue = eventValue
                                    )
                                    Spacer(modifier = Modifier.height(120.dp))
                                }
                            }

                            LaunchedEffect(pagerState.currentPage) {
                                EventLogger.logCampusClickEvent(
                                    when (pagerState.currentPage) {
                                        0 -> "go_to_school"
                                        1 -> "go_home"
                                        else -> "unknown"
                                    },
                                    eventValue
                                )
                            }
                        } else {
                            Column(
                                modifier =
                                Modifier.fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                HorizontalDivider(
                                    color = KoinTheme.colors.neutral400
                                )

                                Box(
                                    modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(14.dp)
                                        .background(color = KoinTheme.colors.neutral100)
                                )
                                Row(
                                    modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Min)
                                ) {
                                    ShuttleTimetableNodeItem(
                                        nodeItemHeightDp = nodeItemHeightDp,
                                        nodes = timetableUiState.timetable.nodeInfo.toPersistentList()
                                    )

                                    VerticalDivider(
                                        modifier = Modifier.fillMaxHeight(),
                                        color = KoinTheme.colors.neutral300
                                    )

                                    Row(
                                        modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .horizontalScroll(rememberScrollState())
                                    ) {
                                        timetableUiState.timetable.routeInfo.fastForEach { route ->
                                            ShuttleTimetableRouteItem(
                                                route = route,
                                                nodeItemHeightDp = nodeItemHeightDp
                                            )
                                        }

                                        /** 시간표가 화면을 못 채울 때, 상단의 회색 배경을 채우기 위함 (프리뷰 참조)
                                         * 더 좋은 방법을 모르겠음... */
                                        Spacer(
                                            modifier =
                                            Modifier.weight(1f).height(
                                                KoinTheme.typography.regular14.getMeasuredKoreanHeightDp() * 2
                                            ).background(KoinTheme.colors.neutral100)
                                        )
                                    }
                                }
                                WrongInformationText(
                                    modifier = Modifier.padding(top = 16.dp, start = 24.dp),
                                    loggingEventValue = eventValue
                                )
                                Spacer(modifier = Modifier.height(120.dp))
                            }
                        }
                    }

                    is ShuttleTimetableUiState.Loading -> {
                        ShuttleTimetableLoading(
                            modifier =
                            Modifier
                                .padding(horizontal = 24.dp, vertical = 16.dp)
                        )
                    }

                    is ShuttleTimetableUiState.LoadFailed -> {
                        CommonFailureView(
                            modifier =
                            Modifier
                                .padding(top = 200.dp)
                                .fillMaxSize()
                        )
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
}

@Preview(showBackground = true)
@Composable
private fun ShuttleTimetableScreenContentPreview() {
    ShuttleTimetableScreenContent(
        timetableUiState = shuttleTimetableUiStateMock1
    )
}

@Preview(showBackground = true)
@Composable
private fun ShuttleTimetableScreenContent2Preview() {
    ShuttleTimetableScreenContent(
        timetableUiState = shuttleTimetableUiStateMock2
    )
}

@Preview(showBackground = true)
@Composable
private fun ShuttleTimetableScreenContentLoadingPreview() {
    ShuttleTimetableScreenContent(
        modifier = Modifier.fillMaxSize(),
        timetableUiState = ShuttleTimetableUiState.Loading
    )
}
