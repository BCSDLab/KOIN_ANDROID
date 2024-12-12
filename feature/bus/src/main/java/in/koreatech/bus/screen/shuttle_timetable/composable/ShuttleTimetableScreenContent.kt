package `in`.koreatech.bus.screen.shuttle_timetable.composable

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import `in`.koreatech.bus.component.ShuttleBusOperationChip
import `in`.koreatech.bus.component.CommonLoadingView
import `in`.koreatech.bus.screen.shuttle_timetable.viewmodel.ShuttleTimetableUiState
import `in`.koreatech.bus.mock.shuttleTimetableUiStateMock
import `in`.koreatech.bus.component.CommonFailureView
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.getMeasuredKoreanHeightDp
import `in`.koreatech.koin.feature.bus.R
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShuttleTimetableScreenContent(
    timetableUiState: ShuttleTimetableUiState,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {}
) {

    val nodeItemHeightDp =
        KoinTheme.typography.medium15.getMeasuredKoreanHeightDp() + KoinTheme.typography.regular12.getMeasuredKoreanHeightDp()

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

                when (timetableUiState) {
                    is ShuttleTimetableUiState.Success -> {
                        Column(
                            modifier = Modifier
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
                                nodes = timetableUiState.timetable.nodeInfo.toPersistentList()
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
                                timetableUiState.timetable.routeInfo.fastForEach { route ->
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
                            modifier = Modifier.padding(top = 200.dp).fillMaxSize()
                        )
                    }

                    is ShuttleTimetableUiState.LoadFailed -> {
                        CommonFailureView(
                            modifier = Modifier.padding(top = 200.dp).fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShuttleTimetableScreenContentPreview() {
    ShuttleTimetableScreenContent(
        timetableUiState = shuttleTimetableUiStateMock
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