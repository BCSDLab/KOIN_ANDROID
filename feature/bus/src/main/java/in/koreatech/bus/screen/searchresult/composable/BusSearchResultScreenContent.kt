package `in`.koreatech.bus.screen.searchresult.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import `in`.koreatech.bus.component.CommonEmptyView
import `in`.koreatech.bus.component.CommonFailureView
import `in`.koreatech.bus.mock.busSearchResultsMock
import `in`.koreatech.bus.screen.search.composable.BusSearchConditionSelectDialog
import `in`.koreatech.bus.screen.searchresult.composable.loading.BusSearchResultLoadingItem
import `in`.koreatech.bus.screen.searchresult.viewmodel.BusSearchResultUiState
import `in`.koreatech.bus.state.ImmutableLocalTime
import `in`.koreatech.bus.type.BusType
import `in`.koreatech.bus.type.PlaceType
import `in`.koreatech.bus.util.formatDateValue
import `in`.koreatech.bus.util.formatDepartureTime
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BusSearchResultScreenContent(
    departure: PlaceType,
    arrival: PlaceType,
    searchResultUiState: BusSearchResultUiState,
    dateList: ImmutableList<String>,
    daytimeList: ImmutableList<String>,
    hourList: ImmutableList<String>,
    minuteList: ImmutableList<String>,
    currentTime: ImmutableLocalTime,
    modifier: Modifier = Modifier,
    selectedDateIndex: Int,
    selectedDaytimeIndex: Int,
    selectedHourIndex: Int,
    selectedMinuteIndex: Int,
    onNavigationIconClick: () -> Unit = {},
    onMinDepartureTimeSetToNow: () -> Unit = {},
    onCompleteMinDepartureTime: (dateIndex: Int, daytimeIndex: Int, hourIndex: Int, minuteIndex: Int) -> Unit = { _, _, _, _ -> },
    selectedBusType: BusType,
    onBusTypeChange: (BusType) -> Unit = {}
) {
    val context = LocalContext.current
    var showSelectDialog by rememberSaveable { mutableStateOf(false) }
    val departureTime by remember(
        selectedDateIndex,
        selectedDaytimeIndex,
        selectedHourIndex,
        selectedMinuteIndex
    ) {
        mutableStateOf(
            formatDepartureTime(
                dateList[selectedDateIndex],
                daytimeList[selectedDaytimeIndex],
                hourList[selectedHourIndex],
                minuteList[selectedMinuteIndex]
            )
        )
    }

    val initialDepartureTimeTextSize = 16.sp
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var departureTimeTextSize by remember { mutableStateOf(initialDepartureTimeTextSize) }

    Column(
        modifier = modifier
    ) {
        KoinTopAppBar(
            title =
            stringResource(
                R.string.search_result_direction_title,
                stringResource(departure.titleRes),
                stringResource(arrival.titleRes)
            ),
            onNavigationIconClick = onNavigationIconClick
        )

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Row(
                            modifier =
                            Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    isDropdownExpanded = !isDropdownExpanded
                                }
                                .background(
                                    color = KoinTheme.colors.neutral50,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text =
                                stringResource(selectedBusType.titleRes) +
                                    if (selectedBusType != BusType.ALL) {
                                        stringResource(
                                            R.string.bus
                                        )
                                    } else {
                                        ""
                                    },
                                style = KoinTheme.typography.medium14,
                                color = KoinTheme.colors.neutral800,
                                modifier = Modifier.padding(start = 8.dp),
                                maxLines = 1
                            )

                            Icon(
                                modifier = Modifier.padding(start = 4.dp),
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = stringResource(R.string.select_bus_type_content_description)
                            )
                        }

                        DropdownMenu(
                            modifier = Modifier,
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false },
                            shape = RoundedCornerShape(12.dp),
                            containerColor = KoinTheme.colors.neutral50
                        ) {
                            BusType.entries.fastForEach { busType ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text =
                                            stringResource(busType.titleRes) +
                                                if (busType != BusType.ALL) {
                                                    stringResource(
                                                        R.string.bus
                                                    )
                                                } else {
                                                    ""
                                                },
                                            style = KoinTheme.typography.medium14
                                        )
                                    },
                                    onClick = {
                                        EventLogger.logCampusClickEvent(
                                            "search_result_bus_type",
                                            context.getString(busType.titleRes)
                                        )
                                        isDropdownExpanded = false
                                        onBusTypeChange(busType)
                                    }
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier =
                    Modifier.noRippleClickable {
                        EventLogger.logCampusClickEvent(
                            "search_result_departure_time",
                            "출발 시각 설정"
                        )
                        showSelectDialog = true
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.padding(start = 4.dp),
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.set_time_content_description),
                        tint = KoinTheme.colors.neutral500
                    )
                    Text(
                        text =
                        buildAnnotatedString {
                            append(departureTime)
                            withStyle(
                                style =
                                SpanStyle(
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                            ) {
                                append(" " + stringResource(R.string.departure))
                            }
                        },
                        style = KoinTheme.typography.bold16,
                        color = KoinTheme.colors.info700,
                        fontSize = departureTimeTextSize,
                        maxLines = 1,
                        onTextLayout = {
                            if (it.didOverflowWidth) {
                                departureTimeTextSize = (departureTimeTextSize.value - .25f).sp
                            }
                        },
                        softWrap = false
                    )
                }
            }
        }

        when (searchResultUiState) {
            is BusSearchResultUiState.Success ->
                LazyColumn {
                    items(searchResultUiState.results) { result ->
                        BusSearchResultItem(
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp, vertical = 10.dp),
                            result = result,
                            currentTime = currentTime,
                            showBeforeTime = (
                                selectedDateIndex == 0 &&
                                    result.departureTime.isAfter(
                                        currentTime.localTime
                                    )
                                )
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
                    }
                }

            is BusSearchResultUiState.ResultEmpty -> {
                CommonEmptyView(modifier = Modifier.fillMaxSize())
            }

            is BusSearchResultUiState.Loading -> {
                repeat(25) {
                    BusSearchResultLoadingItem(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 8.dp)
                    )
                }
            }

            is BusSearchResultUiState.LoadFailed -> {
                CommonFailureView(modifier = Modifier.fillMaxSize())
            }
        }
    }

    if (showSelectDialog) {
        BusSearchConditionSelectDialog(
            modifier =
            Modifier.background(
                color = Color.White,
                shape = RoundedCornerShape(4.dp)
            ),
            onDismissRequest = { showSelectDialog = false },
            onDepartureNow = {
                EventLogger.logCampusClickEvent(
                    "departure_now",
                    "지금 출발"
                )
                onMinDepartureTimeSetToNow()
                showSelectDialog = false
            },
            onComplete = { date, daytime, hour, minute ->
                EventLogger.logCampusClickEvent(
                    "departure_time_setting_done",
                    "시간설정: " +
                        if (selectedDateIndex == date &&
                            selectedDaytimeIndex == daytime &&
                            selectedHourIndex == hour &&
                            selectedMinuteIndex == minute
                        ) {
                            "N"
                        } else {
                            "Y"
                        }
                )
                onCompleteMinDepartureTime(date, daytime, hour, minute)
                showSelectDialog = false
            }, dateList = dateList,
            daytimeList = daytimeList,
            hourList = hourList,
            minuteList = minuteList,
            selectedDateIndex = selectedDateIndex,
            selectedDaytimeIndex = selectedDaytimeIndex,
            selectedHourIndex = selectedHourIndex,
            selectedMinuteIndex = selectedMinuteIndex
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BusSearchResultScreenPreview() {
    BusSearchResultScreenContent(
        modifier = Modifier.fillMaxSize(),
        searchResultUiState = BusSearchResultUiState.Success(busSearchResultsMock),
        dateList =
        List(365) { LocalDate.now().plusDays(it.toLong()) }.map { it.formatDateValue() }
            .toImmutableList(),
        daytimeList = listOf("오전", "오후").toImmutableList(),
        hourList = (1..12).map { it.toString() }.toImmutableList(),
        minuteList = (0..59).map { it.toString() }.toImmutableList(),
        currentTime = ImmutableLocalTime(LocalTime.now()),
        selectedDateIndex = 0,
        selectedDaytimeIndex = 0,
        selectedHourIndex = 0,
        selectedMinuteIndex = 0,
        departure = PlaceType.KOREATECH,
        arrival = PlaceType.STATION,
        selectedBusType = BusType.ALL
    )
}

@Preview(showBackground = true)
@Composable
private fun BusSearchResultScreenLoadingPreview() {
    BusSearchResultScreenContent(
        modifier = Modifier.fillMaxSize(),
        searchResultUiState = BusSearchResultUiState.Loading,
        dateList =
        List(365) { LocalDate.now().plusDays(it.toLong()) }.map { it.formatDateValue() }
            .toImmutableList(),
        daytimeList = listOf("오전", "오후").toImmutableList(),
        hourList = (1..12).map { it.toString() }.toImmutableList(),
        minuteList = (0..59).map { it.toString() }.toImmutableList(),
        currentTime = ImmutableLocalTime(LocalTime.now()),
        selectedDateIndex = 0,
        selectedDaytimeIndex = 0,
        selectedHourIndex = 0,
        selectedMinuteIndex = 0,
        departure = PlaceType.TERMINAL,
        arrival = PlaceType.KOREATECH,
        selectedBusType = BusType.ALL
    )
}
