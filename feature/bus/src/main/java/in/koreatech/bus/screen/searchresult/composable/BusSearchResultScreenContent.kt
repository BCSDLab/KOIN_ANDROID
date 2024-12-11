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
import androidx.compose.runtime.saveable.rememberSaveable
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
import `in`.koreatech.bus.mock.busSearchResultsMock
import `in`.koreatech.bus.screen.CommonLoadingView
import `in`.koreatech.bus.screen.search.composable.BusSearchConditionSelectDialog
import `in`.koreatech.bus.screen.searchresult.viewmodel.BusSearchResultUiState
import `in`.koreatech.bus.state.ImmutableLocalTime
import `in`.koreatech.bus.util.formatDateValue
import `in`.koreatech.bus.util.formatDepartureTime
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDate
import java.time.LocalTime

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
    currentTime: ImmutableLocalTime,
    modifier: Modifier = Modifier,
    selectedDateIndex: Int,
    selectedDaytimeIndex: Int,
    selectedHourIndex: Int,
    selectedMinuteIndex: Int,
    onNavigationIconClick: () -> Unit = {},
    onMinDepartureTimeSetToNow: () -> Unit = {},
    onCompleteMinDepartureTime: (dateIndex: Int, daytimeIndex: Int, hourIndex: Int, minuteIndex: Int) -> Unit = { _, _, _, _ -> },
) {
    var showSelectDialog by rememberSaveable { mutableStateOf(false) }
    val departureTime by remember(selectedDateIndex, selectedDaytimeIndex, selectedHourIndex, selectedMinuteIndex) { mutableStateOf(formatDepartureTime(
        dateList[selectedDateIndex],
        daytimeList[selectedDaytimeIndex],
        hourList[selectedHourIndex],
        minuteList[selectedMinuteIndex]
    )) }

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
                        append(departureTime)
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
            is BusSearchResultUiState.Success -> LazyColumn {
                items(searchResultUiState.results) { result ->
                    BusSearchResultItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 8.dp),
                        result = result,
                        currentTime = currentTime,
                        showBeforeTime = selectedDateIndex == 0
                    )
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
        searchResultUiState = BusSearchResultUiState.Success(busSearchResultsMock),
        dateList = List(365) { LocalDate.now().plusDays(it.toLong()) }.map { it.formatDateValue() }.toImmutableList(),
        daytimeList = listOf("오전", "오후").toImmutableList(),
        hourList = (1..12).map { it.toString() }.toImmutableList(),
        minuteList = (0..59).map { it.toString() }.toImmutableList(),
        currentTime = ImmutableLocalTime(LocalTime.now()),
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
        dateList = List(365) { LocalDate.now().plusDays(it.toLong()) }.map { it.formatDateValue() }.toImmutableList(),
        daytimeList = listOf("오전", "오후").toImmutableList(),
        hourList = (1..12).map { it.toString() }.toImmutableList(),
        minuteList = (0..59).map { it.toString() }.toImmutableList(),
        currentTime = ImmutableLocalTime(LocalTime.now()),
        selectedDateIndex = 0,
        selectedDaytimeIndex = 0,
        selectedHourIndex = 0,
        selectedMinuteIndex = 0,
        departure = "교대역",
        arrival = "코리아텍"
    )
}