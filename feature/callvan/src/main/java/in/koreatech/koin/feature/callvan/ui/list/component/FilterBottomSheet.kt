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
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.ArrivalsFilterType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.DeparturesFilterType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.SortType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.StatusesType
import `in`.koreatech.koin.feature.callvan.ui.component.CallvanBottomSheet
import `in`.koreatech.koin.feature.callvan.ui.list.FilterBottomSheetActions
import `in`.koreatech.koin.feature.callvan.ui.list.FilterBottomSheetState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismissRequest: () -> Unit,
    selectedSortType: SortType,
    selectedStatusesType: StatusesType,
    selectedArrivalsType: ImmutableList<ArrivalsFilterType>,
    selectedDeparturesType: ImmutableList<DeparturesFilterType>,
    onApply: (SortType, StatusesType, ImmutableList<DeparturesFilterType>, ImmutableList<ArrivalsFilterType>) -> Unit
) {
    var currentSortType by remember { mutableStateOf(selectedSortType) }
    var currentStatusesType by remember { mutableStateOf(selectedStatusesType) }
    var currentArrivalsType by remember { mutableStateOf(selectedArrivalsType) }
    var currentDeparturesType by remember { mutableStateOf(selectedDeparturesType) }

    CallvanBottomSheet(
        title = stringResource(R.string.filter_container),
        onDismiss = onDismissRequest,
        showCloseButton = true
    ) {
        FilterBottomSheetContent(
            state = FilterBottomSheetState(
                selectedSortType = currentSortType,
                selectedStatusesType = currentStatusesType,
                selectedDeparturesType = currentDeparturesType,
                selectedArrivalsType = currentArrivalsType
            ),
            actions = FilterBottomSheetActions(
                onSortTypeChange = { currentSortType = it },
                onStatusesTypeChange = { currentStatusesType = it },
                onArrivalsTypeChange = { currentArrivalsType = it },
                onDeparturesTypeChange = { currentDeparturesType = it },
                onReset = {
                    currentSortType = SortType.LatestDesc
                    currentStatusesType = StatusesType.All
                    currentDeparturesType = persistentListOf(DeparturesFilterType.All)
                    currentArrivalsType = persistentListOf(ArrivalsFilterType.All)
                },
                onApplyClick = {
                    onApply(
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
fun FilterBottomSheetContent(
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
            FilterSection(
                title = stringResource(R.string.filter_list_sort_order),
                items = persistentListOf(
                    SortType.LatestDesc,
                    SortType.LatestAsc,
                    SortType.DepartureDesc,
                    SortType.DepartureAsc
                ),
                selectedItem = state.selectedSortType,
                onItemSelected = actions.onSortTypeChange
            )
            HorizontalDivider(color = KoinTheme.colors.neutral300)
            FilterSection(
                title = stringResource(R.string.filter_list_recruitment_status),
                items = persistentListOf(
                    StatusesType.All,
                    StatusesType.Recruiting,
                    StatusesType.Closed,
                    StatusesType.Completed
                ),
                selectedItem = state.selectedStatusesType,
                onItemSelected = actions.onStatusesTypeChange
            )
            HorizontalDivider(color = KoinTheme.colors.neutral300)
            FilterDuplicateSection(
                title = stringResource(R.string.filter_list_origin),
                items = persistentListOf(
                    DeparturesFilterType.All, DeparturesFilterType.FrontGate,
                    DeparturesFilterType.BackGate, DeparturesFilterType.TennisCourt,
                    DeparturesFilterType.DormitoryMain, DeparturesFilterType.DormitorySub,
                    DeparturesFilterType.Terminal, DeparturesFilterType.Station,
                    DeparturesFilterType.AsanStation
                ),
                selectedItems = state.selectedDeparturesType,
                allItem = DeparturesFilterType.All,
                onItemSelected = actions.onDeparturesTypeChange
            )
            HorizontalDivider(color = KoinTheme.colors.neutral300)
            FilterDuplicateSection(
                title = stringResource(R.string.filter_list_destination),
                items = persistentListOf(
                    ArrivalsFilterType.All, ArrivalsFilterType.FrontGate,
                    ArrivalsFilterType.BackGate, ArrivalsFilterType.TennisCourt,
                    ArrivalsFilterType.DormitoryMain, ArrivalsFilterType.DormitorySub,
                    ArrivalsFilterType.Terminal, ArrivalsFilterType.Station,
                    ArrivalsFilterType.AsanStation
                ),
                selectedItems = state.selectedArrivalsType,
                allItem = ArrivalsFilterType.All,
                onItemSelected = actions.onArrivalsTypeChange
            )
            HorizontalDivider(color = KoinTheme.colors.neutral300)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 32.dp, bottom = 12.dp, end = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = actions.onReset,
                shape = KoinTheme.shapes.medium,
                border = BorderStroke(1.dp, KoinTheme.colors.neutral300),
                modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = KoinTheme.colors.neutral0)
            ) {
                Text(
                    text = stringResource(R.string.filter_list_reset),
                    color = KoinTheme.colors.neutral600,
                    style = KoinTheme.typography.bold16
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_process),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = KoinTheme.colors.neutral500
                )
            }
            Button(
                onClick = actions.onApplyClick,
                shape = KoinTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = RebrandKoinTheme.colors.primary500),
                modifier = Modifier.weight(2f).height(48.dp)
            ) {
                Text(
                    text = stringResource(R.string.filter_list_adapt),
                    color = KoinTheme.colors.neutral0,
                    style = KoinTheme.typography.bold16
                )
            }
        }
    }
}

@Composable
fun <T : CallvanFilterType> FilterSection(
    title: String,
    items: ImmutableList<T>,
    selectedItem: T,
    modifier: Modifier = Modifier,
    onItemSelected: (T) -> Unit
) {
    Column(modifier = modifier.padding(vertical = 12.dp)) {
        Text(
            text = title,
            style = KoinTheme.typography.bold16,
            color = KoinTheme.colors.neutral800,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 4
        ) {
            items.forEach { item ->
                FilterBottomSheetItem(
                    text = stringResource(item.stringRes),
                    isSelected = item == selectedItem,
                    onClick = { onItemSelected(item) }
                )
            }
        }
    }
}

@Composable
fun <T : CallvanFilterType> FilterDuplicateSection(
    title: String,
    items: ImmutableList<T>,
    selectedItems: ImmutableList<T>,
    allItem: T,
    modifier: Modifier = Modifier,
    onItemSelected: (ImmutableList<T>) -> Unit
) {
    Column(modifier = modifier.padding(vertical = 12.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = KoinTheme.typography.bold16,
                color = KoinTheme.colors.neutral800
            )
            Text(
                text = stringResource(R.string.filter_list_other_place_hint),
                style = KoinTheme.typography.regular12,
                color = KoinTheme.colors.neutral500
            )
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
                        val newSelection = when (item) {
                            allItem -> persistentListOf(allItem)
                            in selectedItems -> {
                                val removed = selectedItems.filter { it != item }.toPersistentList()
                                removed.ifEmpty { persistentListOf(allItem) }
                            }
                            else -> (selectedItems.filter { it != allItem } + item).toPersistentList()
                        }
                        onItemSelected(newSelection)
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
