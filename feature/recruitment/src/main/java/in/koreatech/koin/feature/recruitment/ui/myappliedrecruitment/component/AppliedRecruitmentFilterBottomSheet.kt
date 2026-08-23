package `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.AppliedFilterSort
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.AppliedFilterState
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.AppliedFilterStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppliedRecruitmentFilterBottomSheet(
    currentFilter: AppliedFilterState,
    onDismiss: () -> Unit,
    onFilterChange: (AppliedFilterState) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = RebrandKoinTheme.colors.neutral0,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = null
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 8.dp, top = 12.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.recruitment_filter),
                    style = RebrandKoinTheme.typography.bold18,
                    color = RebrandKoinTheme.colors.primary500
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        painter = painterResource(R.drawable.ic_recruitment_close),
                        contentDescription = null,
                        tint = RebrandKoinTheme.colors.neutral700,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                FilterSection(
                    title = stringResource(R.string.recruitment_applied_filter_status_section),
                    chips = listOf(
                        stringResource(R.string.recruitment_filter_status_all) to (currentFilter.status == AppliedFilterStatus.ALL),
                        stringResource(R.string.recruitment_applied_status_approved) to (currentFilter.status == AppliedFilterStatus.APPROVED),
                        stringResource(R.string.recruitment_applied_status_pending) to (currentFilter.status == AppliedFilterStatus.PENDING),
                        stringResource(R.string.recruitment_applied_status_rejected) to (currentFilter.status == AppliedFilterStatus.REJECTED)
                    ),
                    onChipClick = { index ->
                        val newStatus = when (index) {
                            0 -> AppliedFilterStatus.ALL
                            1 -> AppliedFilterStatus.APPROVED
                            2 -> AppliedFilterStatus.PENDING
                            else -> AppliedFilterStatus.REJECTED
                        }
                        onFilterChange(currentFilter.copy(status = newStatus))
                    }
                )

                FilterSection(
                    title = stringResource(R.string.recruitment_filter_sort),
                    chips = listOf(
                        stringResource(R.string.recruitment_filter_sort_latest) to (currentFilter.sort == AppliedFilterSort.LATEST),
                        stringResource(R.string.recruitment_filter_sort_deadline) to (currentFilter.sort == AppliedFilterSort.DEADLINE)
                    ),
                    onChipClick = { index ->
                        val newSort = if (index == 0) AppliedFilterSort.LATEST else AppliedFilterSort.DEADLINE
                        onFilterChange(currentFilter.copy(sort = newSort))
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onReset,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonColors(
                        containerColor = RebrandKoinTheme.colors.neutral0,
                        contentColor = RebrandKoinTheme.colors.neutral700,
                        disabledContainerColor = RebrandKoinTheme.colors.neutral300,
                        disabledContentColor = RebrandKoinTheme.colors.neutral600
                    ),
                    border = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral400),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.recruitment_filter_reset),
                            style = RebrandKoinTheme.typography.bold16
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_recruitment_uim_process),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                FilledButton(
                    text = stringResource(R.string.recruitment_filter_apply),
                    onClick = onApply,
                    textStyle = RebrandKoinTheme.typography.bold16,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonColors(
                        containerColor = RebrandKoinTheme.colors.primary500,
                        contentColor = RebrandKoinTheme.colors.neutral0,
                        disabledContainerColor = RebrandKoinTheme.colors.neutral300,
                        disabledContentColor = RebrandKoinTheme.colors.neutral600
                    ),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    modifier = Modifier
                        .weight(2f)
                        .height(48.dp)
                )
            }
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    chips: List<Pair<String, Boolean>>,
    onChipClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = RebrandKoinTheme.typography.bold16,
            color = RebrandKoinTheme.colors.neutral700
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            chips.forEachIndexed { index, (label, isSelected) ->
                FilterChip(
                    text = label,
                    isSelected = isSelected,
                    onClick = { onChipClick(index) }
                )
            }
        }
    }
}

@Composable
private fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) RebrandKoinTheme.colors.primary500 else RebrandKoinTheme.colors.neutral300
    val textColor = if (isSelected) RebrandKoinTheme.colors.primary600 else RebrandKoinTheme.colors.neutral500
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(RebrandKoinTheme.colors.neutral0)
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = RebrandKoinTheme.typography.bold14,
            color = textColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AppliedRecruitmentFilterBottomSheetPreview() {
    RebrandKoinTheme {
        AppliedRecruitmentFilterBottomSheet(
            currentFilter = AppliedFilterState(),
            onDismiss = {},
            onFilterChange = {},
            onReset = {},
            onApply = {}
        )
    }
}
