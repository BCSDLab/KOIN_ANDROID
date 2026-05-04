package `in`.koreatech.koin.feature.callvan.ui.list.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanBottomSheet
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.ArrivalsFilterType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.DeparturesFilterType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.ListType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.SortType
import `in`.koreatech.koin.feature.callvan.ui.list.model.CallvanFilterType.StatusesType
import `in`.koreatech.koin.feature.callvan.ui.list.model.FilterBottomSheetActions
import `in`.koreatech.koin.feature.callvan.ui.list.model.FilterBottomSheetState
import `in`.koreatech.koin.feature.callvan.ui.list.model.FilterSectionItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    state: FilterBottomSheetState,
    actions: FilterBottomSheetActions,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    CallvanBottomSheet(
        title = stringResource(R.string.filter_container),
        onDismiss = onDismissRequest,
        showCloseButton = true
    ) {
        FilterBottomSheetContent(
            modifier = modifier,
            state = state,
            actions = actions
        )
    }
}

@Composable
private fun FilterBottomSheetContent(
    state: FilterBottomSheetState,
    actions: FilterBottomSheetActions,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val maxHeight = remember(configuration.screenHeightDp) {
        (configuration.screenHeightDp * 0.7f).dp
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = maxHeight)
            .padding(bottom = 20.dp)
    ) {
        val scrollState = rememberScrollState()
        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset = if (available.y < 0) available else Offset.Zero
            }
        }
        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .nestedScroll(nestedScrollConnection)
                .verticalScroll(scrollState)
                .padding(top = 12.dp, start = 32.dp, bottom = 12.dp, end = 12.dp)
        ) {
            val context = LocalContext.current
            val sections = remember(state) {
                listOf(
                    FilterSectionItem(
                        title = context.getString(R.string.filter_list_list_type),
                        items = persistentListOf(ListType.All, ListType.My, ListType.Joined),
                        selectedItems = persistentListOf(state.selectedListType)
                    ),
                    FilterSectionItem(
                        title = context.getString(R.string.filter_list_sort_order),
                        items = persistentListOf(SortType.LatestDesc, SortType.LatestAsc, SortType.DepartureDesc, SortType.DepartureAsc),
                        selectedItems = persistentListOf(state.selectedSortType)
                    ),
                    FilterSectionItem(
                        title = context.getString(R.string.filter_list_recruitment_status),
                        items = persistentListOf(StatusesType.All, StatusesType.Recruiting, StatusesType.Closed, StatusesType.Completed),
                        selectedItems = persistentListOf(state.selectedStatusesType)
                    ),
                    FilterSectionItem(
                        title = context.getString(R.string.filter_list_origin),
                        items = persistentListOf(
                            DeparturesFilterType.All, DeparturesFilterType.FrontGate,
                            DeparturesFilterType.BackGate, DeparturesFilterType.TennisCourt,
                            DeparturesFilterType.DormitoryMain, DeparturesFilterType.DormitorySub,
                            DeparturesFilterType.Terminal, DeparturesFilterType.Station,
                            DeparturesFilterType.AsanStation
                        ),
                        selectedItems = state.selectedDeparturesType,
                        hint = context.getString(R.string.filter_list_other_place_hint)
                    ),
                    FilterSectionItem(
                        title = context.getString(R.string.filter_list_destination),
                        items = persistentListOf(
                            ArrivalsFilterType.All, ArrivalsFilterType.FrontGate,
                            ArrivalsFilterType.BackGate, ArrivalsFilterType.TennisCourt,
                            ArrivalsFilterType.DormitoryMain, ArrivalsFilterType.DormitorySub,
                            ArrivalsFilterType.Terminal, ArrivalsFilterType.Station,
                            ArrivalsFilterType.AsanStation
                        ),
                        selectedItems = state.selectedArrivalsType,
                        hint = context.getString(R.string.filter_list_other_place_hint)
                    )
                )
            }

            sections.forEachIndexed { index, section ->
                key(section.title) {
                    FilterSection(
                        title = section.title,
                        items = section.items,
                        selectedItems = section.selectedItems,
                        onItemClicked = actions.onItemClicked,
                        hint = section.hint
                    )
                    if (index <= sections.lastIndex) {
                        HorizontalDivider(color = RebrandKoinTheme.colors.neutral300)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 32.dp, bottom = 12.dp, end = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = actions.onReset,
                shape = RebrandKoinTheme.shapes.medium,
                border = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral300),
                modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = RebrandKoinTheme.colors.neutral0)
            ) {
                Text(
                    text = stringResource(R.string.filter_list_reset),
                    color = RebrandKoinTheme.colors.neutral600,
                    style = RebrandKoinTheme.typography.bold16
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_process),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = RebrandKoinTheme.colors.neutral500
                )
            }
            Button(
                onClick = actions.onApplyClick,
                shape = RebrandKoinTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = RebrandKoinTheme.colors.primary500),
                modifier = Modifier.weight(2f).height(48.dp)
            ) {
                Text(
                    text = stringResource(R.string.filter_list_adapt),
                    color = RebrandKoinTheme.colors.neutral0,
                    style = RebrandKoinTheme.typography.bold16
                )
            }
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    items: ImmutableList<CallvanFilterType>,
    selectedItems: ImmutableList<CallvanFilterType>,
    onItemClicked: (CallvanFilterType) -> Unit,
    hint: String? = null
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = title,
                style = RebrandKoinTheme.typography.bold16,
                color = RebrandKoinTheme.colors.neutral800
            )
            if (hint != null) {
                Text(
                    text = hint,
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral500
                )
            }
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 5
        ) {
            items.forEach { item ->
                key(item) {
                    FilterBottomSheetItem(
                        text = stringResource(item.stringRes),
                        isSelected = item in selectedItems,
                        onClick = { onItemClicked(item) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterBottomSheetContentPreview() {
    RebrandKoinTheme {
        FilterBottomSheetContent(
            state = FilterBottomSheetState(
                selectedSortType = SortType.LatestDesc,
                selectedStatusesType = StatusesType.All,
                selectedDeparturesType = persistentListOf(DeparturesFilterType.All),
                selectedArrivalsType = persistentListOf(ArrivalsFilterType.All)
            ),
            actions = FilterBottomSheetActions(
                onItemClicked = {},
                onReset = {},
                onApplyClick = {}
            )
        )
    }
}
