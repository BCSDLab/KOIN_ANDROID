package `in`.koreatech.bus.screen.timetable.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import `in`.koreatech.bus.screen.timetable.type.BusType
import `in`.koreatech.bus.screen.timetable.viewmodel.BusTimetableViewModel
import `in`.koreatech.koin.core.designsystem.component.tab.KoinTabRow
import `in`.koreatech.koin.core.designsystem.component.text.LeadingIconText
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun BusTimetableScreen(
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    viewModel: BusTimetableViewModel = hiltViewModel(),
    previewTab: BusType = BusType.SHUTTLE
) {

    var selectedTimetableTypeTab by rememberSaveable { mutableStateOf(BusType.SHUTTLE) }
    val busTypeHeadTitle = when(selectedTimetableTypeTab) {
        BusType.SHUTTLE -> stringResource(R.string.shuttle_timetable)
        BusType.EXPRESS -> stringResource(R.string.express_timetable)
        BusType.CITY -> stringResource(R.string.city_timetable)
    }

    val shuttleRegions by viewModel.shuttleRegions.collectAsStateWithLifecycle()
    val expressTimetable by viewModel.expressTimetable.collectAsStateWithLifecycle()
    val cityTimetable by viewModel.cityTimetable.collectAsStateWithLifecycle()

    val shouldShowNotice by viewModel.shouldShowNotice.collectAsStateWithLifecycle()
    val notice by viewModel.notice.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
    ) {
        KoinTopAppBar(
            title = stringResource(R.string.title_bus_timetable),
            onNavigationIconClick = onNavigationIconClick
        )

        LazyColumn {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = busTypeHeadTitle,
                        style = KoinTheme.typography.bold20
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LeadingIconText(
                        text = stringResource(R.string.request_for_incorrect_information),
                        iconRes = R.drawable.ic_caution
                    )
                    if (shouldShowNotice || LocalInspectionMode.current) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .background(
                                    color = KoinTheme.colors.info100,
                                    shape = RoundedCornerShape(8.dp)
                                ).padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = notice,
                                style = KoinTheme.typography.medium14,
                                color = KoinTheme.colors.primary500,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Icon(
                                modifier = Modifier.padding(start = 4.dp).size(16.dp).noRippleClickable {
                                    viewModel.closeNotice()
                                },
                                imageVector = Icons.Rounded.Close,
                                contentDescription = notice,
                                tint = KoinTheme.colors.neutral300
                            )
                        }
                    }
                }
            }

            stickyHeader {
                KoinTabRow(
                    titles = BusType.entries.map { stringResource(it.titleRes) },
                    selectedTabIndex = selectedTimetableTypeTab.ordinal,
                    onTabSelected = { selectedTimetableTypeTab = BusType.entries[it] }
                )
            }

            item {
                if (LocalInspectionMode.current)
                    selectedTimetableTypeTab = previewTab

                when(selectedTimetableTypeTab) {
                    BusType.SHUTTLE -> {
                        ShuttleTimetableScreen(
                            modifier = Modifier.fillMaxSize().background(KoinTheme.colors.neutral100),
                            regions = shuttleRegions.toPersistentList()
                        )
                    }
                    BusType.EXPRESS -> {
                        ExpressTimetableScreen(
                            modifier = Modifier.fillMaxSize().background(KoinTheme.colors.neutral100),
                            timetable = expressTimetable
                        )
                    }
                    BusType.CITY -> {
                        CityTimetableScreen(
                            modifier = Modifier.fillMaxSize().background(KoinTheme.colors.neutral100),
                            timetable = cityTimetable
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun BusTimetableShuttleScreenPreview() {
    BusTimetableScreen(
        modifier = Modifier.fillMaxSize(),
        previewTab = BusType.SHUTTLE
    )
}
@Preview(showBackground = true)
@Composable
private fun BusTimetableExpressScreenPreview() {
    BusTimetableScreen(
        modifier = Modifier.fillMaxSize(),
        previewTab = BusType.EXPRESS
    )
}
@Preview(showBackground = true)
@Composable
private fun BusTimetableCityScreenPreview() {
    BusTimetableScreen(
        modifier = Modifier.fillMaxSize(),
        previewTab = BusType.CITY
    )
}