package `in`.koreatech.koin.feature.callvan.ui.list.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import kotlinx.collections.immutable.toPersistentList

private const val MINIMUM_SELECTION_COUNT = 1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismissRequest: () -> Unit,
    initialListType: ListType,
    initialSortType: SortType,
    initialStatusesType: StatusesType,
    initialArrivalsType: ImmutableList<ArrivalsFilterType>,
    initialDeparturesType: ImmutableList<DeparturesFilterType>,
    onApply: (ListType, SortType, StatusesType, ImmutableList<DeparturesFilterType>, ImmutableList<ArrivalsFilterType>) -> Unit
) {
    var currentListType by remember(initialListType) { mutableStateOf(initialListType) }
    var currentSortType by remember(initialSortType) { mutableStateOf(initialSortType) }
    var currentStatusesType by remember(initialStatusesType) { mutableStateOf(initialStatusesType) }
    var currentArrivalsType by remember(initialArrivalsType) { mutableStateOf(initialArrivalsType) }
    var currentDeparturesType by remember(initialDeparturesType) { mutableStateOf(initialDeparturesType) }

    CallvanBottomSheet(
        title = stringResource(R.string.filter_container),
        onDismiss = onDismissRequest,
        showCloseButton = true
    ) {
        FilterBottomSheetContent(
            state = FilterBottomSheetState(
                selectedListType = currentListType,
                selectedSortType = currentSortType,
                selectedStatusesType = currentStatusesType,
                selectedDeparturesType = currentDeparturesType,
                selectedArrivalsType = currentArrivalsType
            ),
            actions = FilterBottomSheetActions(
                onListTypeChange = { currentListType = it },
                onSortTypeChange = { currentSortType = it },
                onStatusesTypeChange = { currentStatusesType = it },
                onArrivalsTypeChange = { newSelected ->
                    currentArrivalsType = if (
                        currentArrivalsType.size == 1 &&
                        currentArrivalsType.first() == ArrivalsFilterType.All
                    ) {
                        (newSelected - ArrivalsFilterType.All).toPersistentList()
                    } else if (ArrivalsFilterType.All in newSelected) {
                        persistentListOf(ArrivalsFilterType.All)
                    } else {
                        newSelected.toPersistentList()
                    }
                },
                onDeparturesTypeChange = { newSelected ->
                    currentDeparturesType = if (
                        currentDeparturesType.size == 1 &&
                        currentDeparturesType.first() == DeparturesFilterType.All
                    ) {
                        (newSelected - DeparturesFilterType.All).toPersistentList()
                    } else if (DeparturesFilterType.All in newSelected) {
                        persistentListOf(DeparturesFilterType.All)
                    } else {
                        newSelected.toPersistentList()
                    }
                },
                onReset = {
                    currentListType = ListType.All
                    currentSortType = SortType.LatestDesc
                    currentStatusesType = StatusesType.All
                    currentDeparturesType = persistentListOf(DeparturesFilterType.All)
                    currentArrivalsType = persistentListOf(ArrivalsFilterType.All)
                },
                onApplyClick = {
                    onApply(
                        currentListType,
                        currentSortType,
                        currentStatusesType,
                        currentDeparturesType,
                        currentArrivalsType
                    )
                    onDismissRequest()
                }
            )
        )
    }
}

@Composable
private fun FilterBottomSheetContent(
    state: FilterBottomSheetState,
    actions: FilterBottomSheetActions
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .verticalScroll(scrollState)
                .padding(top = 12.dp, start = 32.dp, bottom = 12.dp, end = 12.dp)
        ) {
            val sections = listOf(
                FilterSectionItem(
                    title = stringResource(R.string.filter_list_list_type),
                    items = persistentListOf(ListType.All, ListType.My, ListType.Joined),
                    selectedItems = persistentListOf(state.selectedListType),
                    onItemSelected = { actions.onListTypeChange(it.first() as ListType) }
                ),
                FilterSectionItem(
                    title = stringResource(R.string.filter_list_sort_order),
                    items = persistentListOf(SortType.LatestDesc, SortType.LatestAsc, SortType.DepartureDesc, SortType.DepartureAsc),
                    selectedItems = persistentListOf(state.selectedSortType),
                    onItemSelected = { actions.onSortTypeChange(it.first() as SortType) }
                ),
                FilterSectionItem(
                    title = stringResource(R.string.filter_list_recruitment_status),
                    items = persistentListOf(StatusesType.All, StatusesType.Recruiting, StatusesType.Closed, StatusesType.Completed),
                    selectedItems = persistentListOf(state.selectedStatusesType),
                    onItemSelected = { actions.onStatusesTypeChange(it.first() as StatusesType) }
                ),
                FilterSectionItem(
                    title = stringResource(R.string.filter_list_origin),
                    items = persistentListOf(
                        DeparturesFilterType.All, DeparturesFilterType.FrontGate,
                        DeparturesFilterType.BackGate, DeparturesFilterType.TennisCourt,
                        DeparturesFilterType.DormitoryMain, DeparturesFilterType.DormitorySub,
                        DeparturesFilterType.Terminal, DeparturesFilterType.Station,
                        DeparturesFilterType.AsanStation
                    ),
                    selectedItems = state.selectedDeparturesType,
                    onItemSelected = { actions.onDeparturesTypeChange(it.map { item -> item as DeparturesFilterType }.toPersistentList()) },
                    isDuplicateSelectable = true,
                    hint = stringResource(R.string.filter_list_other_place_hint)
                ),
                FilterSectionItem(
                    title = stringResource(R.string.filter_list_destination),
                    items = persistentListOf(
                        ArrivalsFilterType.All, ArrivalsFilterType.FrontGate,
                        ArrivalsFilterType.BackGate, ArrivalsFilterType.TennisCourt,
                        ArrivalsFilterType.DormitoryMain, ArrivalsFilterType.DormitorySub,
                        ArrivalsFilterType.Terminal, ArrivalsFilterType.Station,
                        ArrivalsFilterType.AsanStation
                    ),
                    selectedItems = state.selectedArrivalsType,
                    onItemSelected = { actions.onArrivalsTypeChange(it.map { item -> item as ArrivalsFilterType }.toPersistentList()) },
                    isDuplicateSelectable = true,
                    hint = stringResource(R.string.filter_list_other_place_hint)
                )
            )

            sections.forEachIndexed { index, section ->
                FilterSection(
                    title = section.title,
                    items = section.items,
                    selectedItems = section.selectedItems,
                    onItemSelected = section.onItemSelected,
                    isDuplicateSelectable = section.isDuplicateSelectable,
                    hint = section.hint
                )
                if (index <= sections.lastIndex) {
                    HorizontalDivider(color = RebrandKoinTheme.colors.neutral300)
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
    onItemSelected: (ImmutableList<CallvanFilterType>) -> Unit,
    isDuplicateSelectable: Boolean = false,
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
                FilterBottomSheetItem(
                    text = stringResource(item.stringRes),
                    isSelected = item in selectedItems,
                    onClick = {
                        if (isDuplicateSelectable) {
                            onItemSelected(
                                if (item in selectedItems) {
                                    if (selectedItems.size > MINIMUM_SELECTION_COUNT) {
                                        (selectedItems - item).toPersistentList()
                                    } else {
                                        return@FilterBottomSheetItem
                                    }
                                } else {
                                    (selectedItems + item).toPersistentList()
                                }
                            )
                        } else {
                            onItemSelected(persistentListOf(item))
                        }
                    }
                )
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
                onListTypeChange = {},
                onSortTypeChange = {},
                onStatusesTypeChange = {},
                onDeparturesTypeChange = {},
                onArrivalsTypeChange = {},
                onReset = {},
                onApplyClick = {}
            )
        )
    }
}
