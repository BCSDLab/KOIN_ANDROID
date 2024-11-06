package `in`.koreatech.bus.screen.timetable.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.screen.timetable.type.ShuttleBusRouteType
import `in`.koreatech.bus.viewstate.ShuttleRegionViewState
import `in`.koreatech.bus.viewstate.ShuttleTimetableOverviewViewState
import `in`.koreatech.koin.core.designsystem.component.chip.ReadOnlyTextChip
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipGroup
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun ShuttleTimetableScreen(
    modifier: Modifier = Modifier,
    regions: ImmutableList<ShuttleRegionViewState>
) {

    var selectedRouteTypeIndex by rememberSaveable { mutableIntStateOf(ShuttleBusRouteType.ALL.ordinal) }
    val context = LocalContext.current

    Column(
        modifier = modifier
    ) {
        TextChipGroup(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 24.dp),
            titles = ShuttleBusRouteType.entries.map { stringResource(it.titleRes) },
            onChipSelected = { title ->
                selectedRouteTypeIndex = ShuttleBusRouteType.entries.find { context.getString(it.titleRes) == title }?.ordinal ?: 0
            },
            selectedChipIndexes = intArrayOf(selectedRouteTypeIndex)
        )
        regions.forEach {
            ShuttleRegionView(
                modifier = Modifier,
                region = it
            )
            if (it != regions.last()) {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun ShuttleRegionView(
    modifier: Modifier = Modifier,
    region: ShuttleRegionViewState
) {
    KoinSurface(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp).padding(top = 16.dp, bottom = 4.dp)
        ) {
            Text(
                text = region.name,
                style = KoinTheme.typography.bold18,
            )
            region.timetableOverviews.forEach {
                ShuttleRouteItem(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 10.dp),
                    timetableOverview = it
                )
            }
        }
    }
}

@Composable
private fun ShuttleRouteItem(
    modifier: Modifier = Modifier,
    timetableOverview: ShuttleTimetableOverviewViewState
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ReadOnlyTextChip(
                title = stringResource(timetableOverview.routeType.simpleTitleRes),
                containerColor = when (timetableOverview.routeType) {        // TODO : 색상 정리
                    ShuttleBusRouteType.WEEKDAY -> Color(0xFF34ADFF)
                    ShuttleBusRouteType.WEEKEND -> Color(0xFFFFB443)
                    ShuttleBusRouteType.CIRCULATION -> Color(0xFF4ED92C)
                    else -> Color.Transparent
                },
                textStyle = KoinTheme.typography.regular12.copy(color = Color.White)
            )

            Text(
                text = timetableOverview.name,
                style = KoinTheme.typography.medium16,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = timetableOverview.name,
                tint = KoinTheme.colors.neutral400
            )
        }
        if (timetableOverview.description.isNotEmpty())
            Text(
                text = timetableOverview.description,
                style = KoinTheme.typography.regular12,
                color = KoinTheme.colors.neutral500,
            )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShuttleTimetableScreenPreview() {
    ShuttleTimetableScreen(
        modifier = Modifier.fillMaxSize().background(KoinTheme.colors.neutral100),
        regions = persistentListOf(
            ShuttleRegionViewState(
                name = "서울",
                timetableOverviews = listOf(
                    ShuttleTimetableOverviewViewState(
                        routeType = ShuttleBusRouteType.WEEKDAY,
                        name = "서울-대전",
                    ),
                    ShuttleTimetableOverviewViewState(
                        routeType = ShuttleBusRouteType.WEEKEND,
                        name = "서울-대전",
                    ),
                    ShuttleTimetableOverviewViewState(
                        routeType = ShuttleBusRouteType.CIRCULATION,
                        name = "서울-대전",
                    )
                )
            ),
            ShuttleRegionViewState(
                name = "대전",
                timetableOverviews = listOf(
                    ShuttleTimetableOverviewViewState(
                        routeType = ShuttleBusRouteType.WEEKDAY,
                        name = "대전-서울",
                    ),
                    ShuttleTimetableOverviewViewState(
                        routeType = ShuttleBusRouteType.WEEKEND,
                        name = "대전-서울",
                        description = "대전에서 서울로 이동하는 노선입니다."
                    ),
                    ShuttleTimetableOverviewViewState(
                        routeType = ShuttleBusRouteType.CIRCULATION,
                        name = "대전-서울",
                        description = "대전에서 서울로 이동하는 노선입니다."
                    )
                )
            ),
            ShuttleRegionViewState(
                name = "대구",
                timetableOverviews = listOf(
                    ShuttleTimetableOverviewViewState(
                        routeType = ShuttleBusRouteType.WEEKDAY,
                        name = "대구-서울",
                    ),
                    ShuttleTimetableOverviewViewState(
                        routeType = ShuttleBusRouteType.WEEKDAY,
                        name = "대구-서울",
                    ),
                    ShuttleTimetableOverviewViewState(
                        routeType = ShuttleBusRouteType.WEEKEND,
                        name = "대구-서울",
                    )
                )
            )
        )
    )
}