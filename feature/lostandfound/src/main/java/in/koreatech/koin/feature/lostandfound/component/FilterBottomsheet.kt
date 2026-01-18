package `in`.koreatech.koin.feature.lostandfound.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import kotlinx.collections.immutable.persistentListOf


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LostAndFoundFilterBottomSheet(
    onDismissRequest: () -> Unit,
    onApply: (String, String, String, String) -> Unit
) {

    val detailOption = stringResource(R.string.filter_list_all)

    var selectedListType by remember { mutableStateOf(detailOption) }
    var selectedCategory by remember { mutableStateOf(detailOption) }
    var selectedItemType by remember { mutableStateOf(detailOption) }
    var selectedStatus by remember { mutableStateOf(detailOption) }


    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = KoinTheme.colors.neutral0,
        dragHandle = null
    ) {
        FilterBottomSheetContent(
            selectedListType = selectedListType,
            selectedCategory = selectedCategory,
            selectedItemType = selectedItemType,
            selectedStatus = selectedStatus,

            onListTypeChange = { selectedListType = it },
            onCategoryChange = { selectedCategory = it },
            onItemTypeChange = { selectedItemType = it },
            onStatusChange = { selectedStatus = it },

            onReset = {
                selectedListType = detailOption
                selectedCategory = detailOption
                selectedItemType = detailOption
                selectedStatus = detailOption
            },

            onApplyClick = {
                onApply(
                    selectedListType,
                    selectedCategory,
                    selectedItemType,
                    selectedStatus
                )
            },

            onDismissRequest = onDismissRequest
        )
    }
}

@Composable
fun FilterBottomSheetContent(
    selectedListType: String,
    selectedCategory: String,
    selectedItemType: String,
    selectedStatus: String,

    onListTypeChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onItemTypeChange: (String) -> Unit,
    onStatusChange: (String) -> Unit,

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
                color = KoinTheme.colors.primary500,
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
                title = stringResource(R.string.filter_list_index),
                items = persistentListOf(
                    stringResource(R.string.filter_list_all),
                    stringResource(R.string.filter_list_my_post)
            ),
                selectedItem = selectedListType,
                onItemSelected = onListTypeChange
            )
            HorizontalDivider(color = KoinTheme.colors.neutral300)
            FilterSection(
                title = stringResource(R.string.filter_list_category),
                items = persistentListOf(
                    stringResource(R.string.filter_list_all),
                    stringResource(R.string.filter_list_find),
                    stringResource(R.string.filter_list_lost)
                ),
                selectedItem = selectedCategory,
                onItemSelected = onCategoryChange
            )
            HorizontalDivider(color = KoinTheme.colors.neutral300)
            FilterSection(
                title = stringResource(R.string.filter_list_type),
                items = persistentListOf(
                    stringResource(R.string.filter_list_all),
                    stringResource(R.string.filter_list_card),
                    stringResource(R.string.filter_list_id_card),
                    stringResource(R.string.filter_list_wallet),
                    stringResource(R.string.filter_list_electronic),
                    stringResource(R.string.filter_list_other)
                ),
                selectedItem = selectedItemType,
                onItemSelected = onItemTypeChange
            )
            HorizontalDivider(color = KoinTheme.colors.neutral300)
            FilterSection(
                title = stringResource(R.string.filter_list_condition),
                items = persistentListOf(
                    stringResource(R.string.filter_list_all),
                    stringResource(R.string.filter_list_finding),
                    stringResource(R.string.filter_list_found)
                ),
                selectedItem = selectedStatus,
                onItemSelected = onStatusChange
            )
            HorizontalDivider(color = KoinTheme.colors.neutral300, thickness = 1.dp)
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
                    imageVector = ImageVector.vectorResource(R.drawable.uim_process),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = KoinTheme.colors.neutral500
                )
            }
            Button(
                onClick = onApplyClick,
                shape = KoinTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = KoinTheme.colors.primary500),
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
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = title,
            style = KoinTheme.typography.bold16,
            color = KoinTheme.colors.neutral800,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        val chunkedItems = remember(items) {items.chunked(3)}
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            chunkedItems.forEach { rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowItems.forEach { item ->
                        FilterChipCustom(
                            text = item,
                            isSelected = item == selectedItem,
                            onClick = { onItemSelected(item) }
                        )
                    }
                }
            }
        }
    }
}