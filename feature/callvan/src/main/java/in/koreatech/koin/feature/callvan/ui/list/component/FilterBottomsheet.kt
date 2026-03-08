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
import kotlin.collections.map
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
    var selectedSortType by remember { mutableStateOf(selectedSortType) }
    var selectedStatusesType by remember { mutableStateOf(selectedStatusesType) }
    var selectedArrivalsType by remember { mutableStateOf(selectedArrivalsType) }
    var selectedDeparturesType by remember { mutableStateOf(selectedDeparturesType) }

    CallvanBottomSheet(
        title = stringResource(R.string.filter_container),
        onDismiss = onDismissRequest,
        showCloseButton = true
    ) {
        FilterBottomSheetContent(
            selectedSortType = selectedSortType,
            selectedStatusesType = selectedStatusesType,
            selectedDeparturesType = selectedDeparturesType,
            selectedArrivalsType = selectedArrivalsType,
            onSortTypeChange = { selectedSortType = it as SortType },
            onStatusesTypeChange = { selectedStatusesType = it as StatusesType },
            onArrivalsTypeChange = {
                val newSelected = it.map { type -> type as ArrivalsFilterType }
                selectedArrivalsType = if (
                    selectedArrivalsType.size == 1 &&
                    selectedArrivalsType.first() == ArrivalsFilterType.All
                ) {
                    (newSelected - ArrivalsFilterType.All).toPersistentList()
                } else if (ArrivalsFilterType.All in newSelected) {
                    persistentListOf(ArrivalsFilterType.All)
                } else {
                    newSelected.toPersistentList()
                }
            },
            onDeparturesTypeChange = {
                val newSelected = it.map { type -> type as DeparturesFilterType }
                selectedDeparturesType = if (
                    selectedDeparturesType.size == 1 &&
                    selectedDeparturesType.first() == DeparturesFilterType.All
                ) {
                    (newSelected - DeparturesFilterType.All).toPersistentList()
                } else if (DeparturesFilterType.All in newSelected) {
                    persistentListOf(DeparturesFilterType.All)
                } else {
                    newSelected.toPersistentList()
                }
            },
            onReset = {
                selectedSortType = SortType.LatestDesc
                selectedStatusesType = StatusesType.All
                selectedDeparturesType = persistentListOf(DeparturesFilterType.All)
                selectedArrivalsType = persistentListOf(ArrivalsFilterType.All)
            },
            onApplyClick = {
                onApply(
                    selectedSortType,
                    selectedStatusesType,
                    selectedDeparturesType,
                    selectedArrivalsType
                )
                onDismissRequest()
            }
        )
    }
}

@Composable
fun FilterBottomSheetContent(
    selectedSortType: SortType,
    selectedStatusesType: StatusesType,
    selectedDeparturesType: ImmutableList<DeparturesFilterType>,
    selectedArrivalsType: ImmutableList<ArrivalsFilterType>,
    onSortTypeChange: (CallvanFilterType) -> Unit,
    onStatusesTypeChange: (CallvanFilterType) -> Unit,
    onDeparturesTypeChange: (ImmutableList<CallvanFilterType>) -> Unit,
    onArrivalsTypeChange: (ImmutableList<CallvanFilterType>) -> Unit,
    onReset: () -> Unit,
    onApplyClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
    ) {
        Column(
            modifier = Modifier
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
                selectedItem = selectedSortType,
                onItemSelected = onSortTypeChange
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
                selectedItem = selectedStatusesType,
                onItemSelected = onStatusesTypeChange
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
                selectedItems = selectedDeparturesType,
                onItemSelected = onDeparturesTypeChange
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
                selectedItems = selectedArrivalsType,
                onItemSelected = onArrivalsTypeChange
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
                onClick = onReset,
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
                onClick = onApplyClick,
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
fun FilterSection(
    title: String,
    items: ImmutableList<CallvanFilterType>,
    selectedItem: CallvanFilterType,
    onItemSelected: (CallvanFilterType) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
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
private const val AT_LEAST_COUNT = 1

@Composable
fun FilterDuplicateSection(
    title: String,
    items: ImmutableList<CallvanFilterType>,
    selectedItems: ImmutableList<CallvanFilterType>,
    onItemSelected: (ImmutableList<CallvanFilterType>) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
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
                        onItemSelected(
                            if (item in selectedItems) {
                                if (selectedItems.size > AT_LEAST_COUNT) {
                                    (selectedItems - item).toPersistentList()
                                } else {
                                    return@FilterBottomSheetItem
                                }
                            } else {
                                (selectedItems + item).toPersistentList()
                            }
                        )
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
            selectedSortType = SortType.LatestDesc,
            selectedStatusesType = StatusesType.All,
            selectedDeparturesType = persistentListOf(DeparturesFilterType.All),
            selectedArrivalsType = persistentListOf(ArrivalsFilterType.All),
            onSortTypeChange = {},
            onStatusesTypeChange = {},
            onDeparturesTypeChange = {},
            onArrivalsTypeChange = {},
            onReset = {},
            onApplyClick = {}
        )
    }
}
