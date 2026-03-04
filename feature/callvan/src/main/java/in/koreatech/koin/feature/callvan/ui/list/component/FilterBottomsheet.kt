package `in`.koreatech.koin.feature.callvan.ui.list.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.DestinationFilterType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.OriginFilterType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.RecruitmentStatusType
import `in`.koreatech.koin.feature.callvan.enums.CallvanFilterType.SortOrderType
import kotlin.collections.map
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallvanFilterBottomSheet(
    onDismissRequest: () -> Unit,
    selectedSortOrderType: SortOrderType,
    selectedRecruitmentStatusType: RecruitmentStatusType,
    selectedOriginType: ImmutableList<OriginFilterType>,
    selectedDestinationType: ImmutableList<DestinationFilterType>,
    onApply: (SortOrderType, RecruitmentStatusType, ImmutableList<OriginFilterType>, ImmutableList<DestinationFilterType>) -> Unit
) {
    var selectedSortOrderType by remember { mutableStateOf(selectedSortOrderType) }
    var selectedRecruitmentStatusType by remember { mutableStateOf(selectedRecruitmentStatusType) }
    var selectedOriginType by remember { mutableStateOf(selectedOriginType) }
    var selectedDestinationType by remember { mutableStateOf(selectedDestinationType) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        containerColor = KoinTheme.colors.neutral0,
        dragHandle = null
    ) {
        FilterBottomSheetContent(
            selectedSortOrderType = selectedSortOrderType,
            selectedRecruitmentStatusType = selectedRecruitmentStatusType,
            selectedOriginType = selectedOriginType,
            selectedDestinationType = selectedDestinationType,

            onSortOrderChange = { selectedSortOrderType = it as SortOrderType },
            onRecruitmentStatusTypeChange = { selectedRecruitmentStatusType = it as RecruitmentStatusType },
            onOriginTypeChange = {
                val newSelectedOrigin = it.map { type -> type as OriginFilterType }
                selectedOriginType = if (
                    selectedOriginType.size == 1 &&
                    selectedOriginType.first() == OriginFilterType.ALL
                ) {
                    (newSelectedOrigin - OriginFilterType.ALL).toPersistentList()
                } else if (OriginFilterType.ALL in newSelectedOrigin) {
                    persistentListOf(OriginFilterType.ALL)
                } else {
                    newSelectedOrigin.toPersistentList()
                }
            },
            onDestinationTypeChange = {
                val newSelectedDestination = it.map { type -> type as DestinationFilterType }
                selectedDestinationType = if (
                    selectedDestinationType.size == 1 &&
                    selectedDestinationType.first() == DestinationFilterType.ALL
                ) {
                    (newSelectedDestination - DestinationFilterType.ALL).toPersistentList()
                } else if (DestinationFilterType.ALL in newSelectedDestination) {
                    persistentListOf(DestinationFilterType.ALL)
                } else {
                    newSelectedDestination.toPersistentList()
                }
            },

            onReset = {
                selectedSortOrderType = SortOrderType.LATEST
                selectedRecruitmentStatusType = RecruitmentStatusType.ALL
                selectedOriginType = persistentListOf(OriginFilterType.ALL)
                selectedDestinationType = persistentListOf(DestinationFilterType.ALL)
            },

            onApplyClick = {
                onApply(
                    selectedSortOrderType,
                    selectedRecruitmentStatusType,
                    selectedOriginType,
                    selectedDestinationType
                )
            },

            onDismissRequest = {
                scope.launch { sheetState.hide() }
                onDismissRequest()
            }
        )
    }
}

@Composable
fun FilterBottomSheetContent(
    selectedSortOrderType: SortOrderType,
    selectedRecruitmentStatusType: RecruitmentStatusType,
    selectedOriginType: ImmutableList<OriginFilterType>,
    selectedDestinationType: ImmutableList<DestinationFilterType>,
    onSortOrderChange: (CallvanFilterType) -> Unit,
    onRecruitmentStatusTypeChange: (CallvanFilterType) -> Unit,
    onOriginTypeChange: (ImmutableList<CallvanFilterType>) -> Unit,
    onDestinationTypeChange: (ImmutableList<CallvanFilterType>) -> Unit,
    onReset: () -> Unit,
    onApplyClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 32.dp, bottom = 12.dp, end = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.filter_container),
                style = KoinTheme.typography.bold18,
                color = RebrandKoinTheme.colors.primary500,
                modifier = Modifier.align(Alignment.CenterStart)
            )
            IconButton(
                onClick = onDismissRequest,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bottomsheet_close),
                    contentDescription = stringResource(R.string.bottom_sheet_close)
                )
            }
        }

        HorizontalDivider(color = KoinTheme.colors.neutral300)

        Column(
            modifier = Modifier
                .padding(top = 12.dp, start = 32.dp, bottom = 12.dp, end = 12.dp)
        ) {
            FilterSection(
                title = stringResource(R.string.filter_list_sort_order),
                items = persistentListOf(
                    SortOrderType.LATEST,
                    SortOrderType.DEPARTURE
                ),
                selectedItem = selectedSortOrderType,
                onItemSelected = onSortOrderChange
            )
            HorizontalDivider(color = KoinTheme.colors.neutral300)
            FilterSection(
                title = stringResource(R.string.filter_list_recruitment_status),
                items = persistentListOf(
                    RecruitmentStatusType.ALL,
                    RecruitmentStatusType.RECRUITING,
                    RecruitmentStatusType.COMPLETED
                ),
                selectedItem = selectedRecruitmentStatusType,
                onItemSelected = onRecruitmentStatusTypeChange
            )
            HorizontalDivider(color = KoinTheme.colors.neutral300)
            FilterDuplicateSection(
                title = stringResource(R.string.filter_list_origin),
                items = persistentListOf(
                    OriginFilterType.ALL,
                    OriginFilterType.SCHOOL,
                    OriginFilterType.TERMINAL,
                    OriginFilterType.DOWNTOWN,
                    OriginFilterType.SINGYERI,
                    OriginFilterType.OCHANG,
                    OriginFilterType.CHEONAN,
                    OriginFilterType.ASAN
                ),
                selectedItems = selectedOriginType,
                onItemSelected = onOriginTypeChange
            )
            HorizontalDivider(color = KoinTheme.colors.neutral300)
            FilterDuplicateSection(
                title = stringResource(R.string.filter_list_destination),
                items = persistentListOf(
                    DestinationFilterType.ALL,
                    DestinationFilterType.SCHOOL,
                    DestinationFilterType.TERMINAL,
                    DestinationFilterType.DOWNTOWN,
                    DestinationFilterType.SINGYERI,
                    DestinationFilterType.OCHANG,
                    DestinationFilterType.CHEONAN,
                    DestinationFilterType.ASAN
                ),
                selectedItems = selectedDestinationType,
                onItemSelected = onDestinationTypeChange
            )
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
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
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
                onClick = {
                    onApplyClick()
                    onDismissRequest()
                },
                shape = KoinTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = RebrandKoinTheme.colors.primary500),
                modifier = Modifier
                    .weight(2f)
                    .height(48.dp)
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
        val chunkedItems = remember(items) { items.chunked(4) }
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            chunkedItems.forEach { rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowItems.forEach { item ->
                        FilterChipCustom(
                            text = stringResource(item.stringRes),
                            isSelected = item == selectedItem,
                            onClick = { onItemSelected(item) }
                        )
                    }
                }
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
        Text(
            text = title,
            style = KoinTheme.typography.bold16,
            color = KoinTheme.colors.neutral800,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items.forEach { item ->
                FilterChipCustom(
                    text = stringResource(item.stringRes),
                    isSelected = item in selectedItems,
                    onClick = {
                        onItemSelected(
                            if (item in selectedItems) {
                                if (selectedItems.size > AT_LEAST_COUNT) {
                                    (selectedItems - item).toPersistentList()
                                } else {
                                    return@FilterChipCustom
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
