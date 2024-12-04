package `in`.koreatech.bus.screen.timetabledetail.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import `in`.koreatech.bus.component.ShuttleBusOperationChip
import `in`.koreatech.bus.screen.CommonLoadingView
import `in`.koreatech.bus.screen.timetable.type.ShuttleBusOperationType
import `in`.koreatech.bus.screen.timetabledetail.viewmodel.ShuttleTimetableUiState
import `in`.koreatech.bus.viewstate.ShuttleTimetableViewState
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.bus.R
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShuttleTimetableDetailScreenContent(
    timetableUiState: ShuttleTimetableUiState,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {}
) {

    val nodeItemHeightDp = with(rememberTextMeasurer()) {
        with(LocalDensity.current) {
            (measure(
                text = "가",
                style = KoinTheme.typography.medium15
            ).size.height + measure(
                text = "가",
                style = KoinTheme.typography.regular12
            ).size.height).toDp()
        }
    }

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
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    ShuttleBusOperationChip(
                        operationType = ShuttleBusOperationType.CIRCULATION
                    )

                    Text(
                        text = "천안 셔틀 시간표",
                        style = KoinTheme.typography.bold20,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                when (timetableUiState) {
                    is ShuttleTimetableUiState.Success -> {
                        HorizontalDivider(
                            color = KoinTheme.colors.neutral400
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(14.dp)
                                .background(color = KoinTheme.colors.neutral100)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                        ) {
                            ShuttleTimetableNodeItem(
                                nodeItemHeightDp = nodeItemHeightDp,
                                nodes = timetableUiState.timetable.nodes.toPersistentList()
                            )

                            VerticalDivider(
                                modifier = Modifier.fillMaxHeight(),
                                color = KoinTheme.colors.neutral300
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                            ) {
                                timetableUiState.timetable.routes.fastForEach { route ->
                                    ShuttleTimetableRouteItem(
                                        route = route,
                                        nodeItemHeightDp = nodeItemHeightDp,
                                    )
                                }
                            }
                        }
                    }

                    is ShuttleTimetableUiState.Loading -> {
                        CommonLoadingView(
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    is ShuttleTimetableUiState.LoadFailed -> {
                        //
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShuttleTimetableDetailScreenContentPreview() {
    ShuttleTimetableDetailScreenContent(
        timetableUiState = ShuttleTimetableUiState.Success(
            timetable = ShuttleTimetableViewState(emptyList(), emptyList())
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun ShuttleTimetableDetailScreenContentLoadingPreview() {
    ShuttleTimetableDetailScreenContent(
        modifier = Modifier.fillMaxSize(),
        timetableUiState = ShuttleTimetableUiState.Loading
    )
}