package `in`.koreatech.koin.feature.lostandfound.ui.list.component

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
import androidx.compose.material3.rememberModalBottomSheetState
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
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.AuthorFilterType
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.CategoryFilterType
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.FoundFilterType
import `in`.koreatech.koin.feature.lostandfound.enums.LostAndFoundFilterType.LostOrFoundFilterType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LostAndFoundFilterBottomSheet(
    onDismissRequest: () -> Unit,
    selectedAuthorType: AuthorFilterType,
    selectedLostOrFoundType: LostOrFoundFilterType,
    selectedCategoryType: CategoryFilterType,
    selectedFoundType: FoundFilterType,
    onApply: (AuthorFilterType, LostOrFoundFilterType, CategoryFilterType, FoundFilterType) -> Unit
) {
    var selectedAuthorType by remember { mutableStateOf(selectedAuthorType) }
    var selectedLostOrFoundType by remember { mutableStateOf(selectedLostOrFoundType) }
    var selectedCategoryType by remember { mutableStateOf(selectedCategoryType) }
    var selectedFoundType by remember { mutableStateOf(selectedFoundType) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        containerColor = KoinTheme.colors.neutral0,
        dragHandle = null
    ) {
        FilterBottomSheetContent(
            selectedAuthorType = selectedAuthorType,
            selectedLostOrFoundType = selectedLostOrFoundType,
            selectedCategoryType = selectedCategoryType,
            selectedFoundType = selectedFoundType,

            onAuthorTypeChange = { selectedAuthorType = it as AuthorFilterType },
            onLostOrFoundTypeChange = { selectedLostOrFoundType = it as LostOrFoundFilterType },
            onCategoryTypeChange = { selectedCategoryType = it as CategoryFilterType },
            onFoundTypeChange = { selectedFoundType = it as FoundFilterType },

            onReset = {
                selectedAuthorType = AuthorFilterType.ALL
                selectedLostOrFoundType = LostOrFoundFilterType.ALL
                selectedCategoryType = CategoryFilterType.ALL
                selectedFoundType = FoundFilterType.ALL
            },

            onApplyClick = {
                onApply(
                    selectedAuthorType,
                    selectedLostOrFoundType,
                    selectedCategoryType,
                    selectedFoundType
                )
            },

            onDismissRequest = onDismissRequest
        )
    }
}

@Composable
fun FilterBottomSheetContent(
    selectedAuthorType: AuthorFilterType,
    selectedLostOrFoundType: LostOrFoundFilterType,
    selectedCategoryType: CategoryFilterType,
    selectedFoundType: FoundFilterType,
    onAuthorTypeChange: (LostAndFoundFilterType) -> Unit,
    onLostOrFoundTypeChange: (LostAndFoundFilterType) -> Unit,
    onCategoryTypeChange: (LostAndFoundFilterType) -> Unit,
    onFoundTypeChange: (LostAndFoundFilterType) -> Unit,
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
                    AuthorFilterType.ALL,
                    AuthorFilterType.MY
                ),
                selectedItem = selectedAuthorType,
                onItemSelected = onAuthorTypeChange
            )
            HorizontalDivider(color = KoinTheme.colors.neutral300)
            FilterSection(
                title = stringResource(R.string.filter_list_category),
                items = persistentListOf(
                    LostOrFoundFilterType.ALL,
                    LostOrFoundFilterType.FIND,
                    LostOrFoundFilterType.LOST
                ),
                selectedItem = selectedLostOrFoundType,
                onItemSelected = onLostOrFoundTypeChange
            )
            HorizontalDivider(color = KoinTheme.colors.neutral300)
            FilterSection(
                title = stringResource(R.string.filter_list_type),
                items = persistentListOf(
                    CategoryFilterType.ALL,
                    CategoryFilterType.CARD,
                    CategoryFilterType.ID,
                    CategoryFilterType.WALLET,
                    CategoryFilterType.ELECTRONIC,
                    CategoryFilterType.OTHER
                ),
                selectedItem = selectedCategoryType,
                onItemSelected = onCategoryTypeChange
            )
            HorizontalDivider(color = KoinTheme.colors.neutral300)
            FilterSection(
                title = stringResource(R.string.filter_list_condition),
                items = persistentListOf(
                    FoundFilterType.ALL,
                    FoundFilterType.FINDING,
                    FoundFilterType.FOUND
                ),
                selectedItem = selectedFoundType,
                onItemSelected = onFoundTypeChange
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
                    EventLogger.logCampusClickEvent(
                        AnalyticsConstant.Label.LostAndFound.LOST_ITEM_FILTER_APPLY,
                        "분실물"
                    )
                    onApplyClick()
                    onDismissRequest()
                },
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
    items: ImmutableList<LostAndFoundFilterType>,
    selectedItem: LostAndFoundFilterType,
    onItemSelected: (LostAndFoundFilterType) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = title,
            style = KoinTheme.typography.bold16,
            color = KoinTheme.colors.neutral800,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        val chunkedItems = remember(items) { items.chunked(3) }
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            chunkedItems.forEach { rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
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
