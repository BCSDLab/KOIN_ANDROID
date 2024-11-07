package `in`.koreatech.bus.screen.search.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.koreatech.bus.screen.search.viewmodel.BusSearchResultViewModel
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BusSearchResultScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    viewModel: BusSearchResultViewModel = hiltViewModel()
) {

    var showSelectDialog by remember { mutableStateOf(false) }

    val departureTimeText by viewModel.departureTimeText.collectAsStateWithLifecycle()

    LazyColumn (
        modifier = modifier
    ) {
        item {
            KoinTopAppBar(
                title = "한기대 → 천안터미널", // TODO : 방향
                onNavigationIconClick = onNavigationIconClick
            )
        }

        stickyHeader {
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
                            append(departureTimeText)
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
                viewModel.setDepartureTimeToNow()
                showSelectDialog = false
            },
            onComplete = { date, daytime, hour, minute ->
                viewModel.setDepartureTime(date, daytime, hour, minute)
                showSelectDialog = false
            }, dateList = viewModel.dateList.toImmutableList(),
            daytimeList = viewModel.daytimeList.toImmutableList(),
            hourList = viewModel.hourList.toImmutableList(),
            minuteList = viewModel.minuteList.toImmutableList(),
            selectedDateIndex = viewModel.selectedDateIndex,
            selectedDaytimeIndex = viewModel.selectedDaytimeIndex,
            selectedHourIndex = viewModel.selectedHourIndex,
            selectedMinuteIndex = viewModel.selectedMinuteIndex
        )
    }
}