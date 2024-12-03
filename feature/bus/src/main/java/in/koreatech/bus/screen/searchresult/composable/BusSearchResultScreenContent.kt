package `in`.koreatech.bus.screen.searchresult.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.screen.CommonLoadingView
import `in`.koreatech.bus.screen.search.composable.BusSearchConditionSelectDialog
import `in`.koreatech.bus.screen.searchresult.viewmodel.BusSearchResultUiState
import `in`.koreatech.bus.screen.timetable.type.BusType
import `in`.koreatech.bus.util.formatDepartureTime
import `in`.koreatech.bus.viewstate.BusDepartureInfoViewState
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BusSearchResultScreenContent(
    departure: String,
    arrival: String,
    searchResultUiState: BusSearchResultUiState,
    dateList: ImmutableList<String>,
    daytimeList: ImmutableList<String>,
    hourList: ImmutableList<String>,
    minuteList: ImmutableList<String>,
    selectedDateIndex: Int,
    selectedDaytimeIndex: Int,
    selectedHourIndex: Int,
    selectedMinuteIndex: Int,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    onMinDepartureTimeSetToNow: () -> Unit = {},
    onCompleteMinDepartureTime: (dateIndex: Int, daytimeIndex: Int, hourIndex: Int, minuteIndex: Int) -> Unit = { _, _, _, _ -> },

    ) {
    var showSelectDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        KoinTopAppBar(
            title = stringResource(R.string.search_result_direction_title, departure, arrival),
            onNavigationIconClick = onNavigationIconClick
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.noRippleClickable {
                    showSelectDialog = true
                }, verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(formatDepartureTime(
                            date = dateList[selectedDateIndex],
                            daytime = daytimeList[selectedDaytimeIndex],
                            hour = hourList[selectedHourIndex],
                            minute = minuteList[selectedMinuteIndex]
                        ))
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        ) {
                            append(" " + stringResource(R.string.departure))
                        }
                    }, style = KoinTheme.typography.bold16,
                    color = KoinTheme.colors.info700
                )
                Icon(
                    modifier = Modifier.padding(start = 4.dp),
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.set_time_content_description),
                    tint = KoinTheme.colors.neutral500
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        when (searchResultUiState) {
            is BusSearchResultUiState.Success -> {
                LazyColumn {
                    items(searchResultUiState.departureInfos) { info ->
                        BusSearchResultItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp, vertical = 8.dp),
                            info = info
                        )
                    }
                }
            }
            is BusSearchResultUiState.Loading -> {
                CommonLoadingView(modifier = Modifier.fillMaxSize())
            }
            is BusSearchResultUiState.LoadFailed -> {
                // TODO : 에러 화면, Pull to refresh
            }
        }
    }

    if (showSelectDialog) {
        BusSearchConditionSelectDialog(
            modifier = Modifier.background(
                color = Color.White,
                shape = RoundedCornerShape(4.dp)
            ),
            onDismissRequest = { showSelectDialog = false },
            onDepartureNow = {
                onMinDepartureTimeSetToNow()
                showSelectDialog = false
            },
            onComplete = { date, daytime, hour, minute ->
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
        searchResultUiState = previewSearchResultUiState,
        dateList = buildList {
            val today = LocalDateTime.now()

            add("오늘")
            add("내일")
            for (i in 2 until 365) {
                val date = today.plusDays(i.toLong())
                val formattedDate = date.format(
                    DateTimeFormatter.ofPattern("M월 d일(E)", Locale.KOREA)
                ).replace("요일", "")
                add(formattedDate)
            }
        }.toImmutableList(),
        daytimeList = listOf("오전", "오후").toImmutableList(),
        hourList = (1..12).map { it.toString() }.toImmutableList(),
        minuteList = (0..59).map { it.toString() }.toImmutableList(),
        selectedDateIndex = 0,
        selectedDaytimeIndex = 0,
        selectedHourIndex = 0,
        selectedMinuteIndex = 0,
        departure = "코리아텍",
        arrival = "청주"
    )
}

@Preview(showBackground = true)
@Composable
private fun BusSearchResultScreenLoadingPreview() {
    BusSearchResultScreenContent(
        modifier = Modifier.fillMaxSize(),
        searchResultUiState = BusSearchResultUiState.Loading,
        dateList = buildList {
            val today = LocalDateTime.now()

            add("오늘")
            add("내일")
            for (i in 2 until 365) {
                val date = today.plusDays(i.toLong())
                val formattedDate = date.format(
                    DateTimeFormatter.ofPattern("M월 d일(E)", Locale.KOREA)
                ).replace("요일", "")
                add(formattedDate)
            }
        }.toImmutableList(),
        daytimeList = listOf("오전", "오후").toImmutableList(),
        hourList = (1..12).map { it.toString() }.toImmutableList(),
        minuteList = (0..59).map { it.toString() }.toImmutableList(),
        selectedDateIndex = 0,
        selectedDaytimeIndex = 0,
        selectedHourIndex = 0,
        selectedMinuteIndex = 0,
        departure = "교대역",
        arrival = "코리아텍"
    )
}

private val previewSearchResultUiState = BusSearchResultUiState.Success(listOf(
    BusDepartureInfoViewState(
        type = BusType.SHUTTLE,
        departureHour = 9,
        departureMinute = 0,
        remainingTime = 0
    ),
    BusDepartureInfoViewState(
        type = BusType.EXPRESS,
        departureHour = 9,
        departureMinute = 10,
        remainingTime = 10
    ),
    BusDepartureInfoViewState(
        type = BusType.CITY,
        departureHour = 9,
        departureMinute = 20,
        remainingTime = 20
    ),
    BusDepartureInfoViewState(
        type = BusType.SHUTTLE,
        departureHour = 9,
        departureMinute = 30,
        remainingTime = 30
    ),
    BusDepartureInfoViewState(
        type = BusType.EXPRESS,
        departureHour = 9,
        departureMinute = 40,
        remainingTime = 40
    ),
    BusDepartureInfoViewState(
        type = BusType.CITY,
        departureHour = 9,
        departureMinute = 50,
        remainingTime = 50
    ),
    BusDepartureInfoViewState(
        type = BusType.SHUTTLE,
        departureHour = 10,
        departureMinute = 0,
        remainingTime = 60
    ),
    BusDepartureInfoViewState(
        type = BusType.EXPRESS,
        departureHour = 10,
        departureMinute = 10,
        remainingTime = 70
    ),
    BusDepartureInfoViewState(
        type = BusType.CITY,
        departureHour = 10,
        departureMinute = 20,
        remainingTime = 80
    ),
    BusDepartureInfoViewState(
        type = BusType.SHUTTLE,
        departureHour = 10,
        departureMinute = 30,
        remainingTime = 90
    ),
    BusDepartureInfoViewState(
        type = BusType.EXPRESS,
        departureHour = 10,
        departureMinute = 40,
        remainingTime = 100
    ),
    BusDepartureInfoViewState(
        type = BusType.CITY,
        departureHour = 10,
        departureMinute = 50,
        remainingTime = 110
    ),
    BusDepartureInfoViewState(
        type = BusType.SHUTTLE,
        departureHour = 11,
        departureMinute = 0,
        remainingTime = 120
    )
))