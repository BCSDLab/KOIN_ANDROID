package `in`.koreatech.bus.screen.timetable.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.component.ShuttleBusOperationChip
import `in`.koreatech.bus.component.WrongInformationText
import `in`.koreatech.bus.mock.shuttleCoursesMock
import `in`.koreatech.bus.state.ShuttleCourseRegionState
import `in`.koreatech.bus.state.ShuttleCourseRouteState
import `in`.koreatech.bus.state.ShuttleCoursesState
import `in`.koreatech.bus.type.ShuttleBusOperationType
import `in`.koreatech.bus.util.formatPeriod
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipGroup
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.core.designsystem.component.text.LeadingIconText
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ShuttleCoursesScreenContent(
    shuttleCourses: ShuttleCoursesState,
    modifier: Modifier = Modifier,
    onItemClicked: (ShuttleCourseRouteState) -> Unit = {},
) {

    var selectedRouteType by rememberSaveable { mutableStateOf(ShuttleBusOperationType.ALL) }
    val context = LocalContext.current

    Column(
        modifier = modifier
    ) {
        TextChipGroup(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 24.dp),
            titles = ShuttleBusOperationType.entries.map { stringResource(it.titleRes) },
            onChipSelected = { title ->
                selectedRouteType = ShuttleBusOperationType.entries.find { context.getString(it.titleRes) == title } ?: ShuttleBusOperationType.ALL
                EventLogger.logCampusClickEvent(
                    "shuttle_bus_route",
                    context.getString(selectedRouteType.titleRes)
                )
            },
            selectedChipIndexes = intArrayOf(selectedRouteType.ordinal),
            showClickRipple = false
        )
        shuttleCourses.courses.forEach { courseEntry ->
            val filteredValue = courseEntry.value.filter { courseRouteState ->
                if (selectedRouteType == ShuttleBusOperationType.ALL) true
                else courseRouteState.type == selectedRouteType
            }

            if (filteredValue.isNotEmpty()) {
                if (courseEntry.key != shuttleCourses.courses.keys.first())
                    Spacer(modifier = Modifier.height(10.dp))

                ShuttleCourseView(
                    modifier = Modifier,
                    region = courseEntry.key,
                    shuttleCourseRoutes = filteredValue.toImmutableList(),
                    onItemClicked = {
                        EventLogger.logCampusClickEvent(
                            "area_specific_route",
                            "${context.getString(it.type.simpleTitleRes)}_${it.routeName}"
                        )
                        onItemClicked(it)
                    }
                )
            }
        }

        Column(modifier = Modifier
            .background(Color.White)
            .padding(top = 8.dp)
            .fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = stringResource(R.string.timetable_semester_information,
                    shuttleCourses.semester.name,
                    shuttleCourses.semester.from.formatPeriod(),
                    shuttleCourses.semester.to.formatPeriod()
                ), style = KoinTheme.typography.regular14,
                color = KoinTheme.colors.neutral500,
            )
            WrongInformationText(
                modifier = Modifier.padding(top = 4.dp, start = 24.dp)
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(120.dp))
        }
    }
}

@Composable
private fun ShuttleCourseView(
    modifier: Modifier = Modifier,
    region: ShuttleCourseRegionState,
    shuttleCourseRoutes: ImmutableList<ShuttleCourseRouteState>,
    onItemClicked: (ShuttleCourseRouteState) -> Unit = {}
) {
    KoinSurface(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp, bottom = 4.dp)
        ) {
            Text(
                text = region.name,
                style = KoinTheme.typography.bold18,
            )
            shuttleCourseRoutes.forEach {
                ShuttleRouteItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onItemClicked(it)
                        }
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    shuttleCourseRoute = it,
                )
            }
        }
    }
}

@Composable
private fun ShuttleRouteItem(
    shuttleCourseRoute: ShuttleCourseRouteState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShuttleBusOperationChip(
                operationType = shuttleCourseRoute.type,
            )

            Text(
                text = shuttleCourseRoute.routeName,
                style = KoinTheme.typography.medium16,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = shuttleCourseRoute.routeName,
                tint = KoinTheme.colors.neutral400
            )
        }
        if (shuttleCourseRoute.subName.isNotEmpty())
            Text(
                text = shuttleCourseRoute.subName,
                style = KoinTheme.typography.regular12,
                color = KoinTheme.colors.neutral500,
            )
    }
}

@Preview(showBackground = true)
@Composable
private fun ShuttleCoursesScreenPreview() {
    ShuttleCoursesScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .background(KoinTheme.colors.neutral100),
        shuttleCourses = shuttleCoursesMock
    )
}