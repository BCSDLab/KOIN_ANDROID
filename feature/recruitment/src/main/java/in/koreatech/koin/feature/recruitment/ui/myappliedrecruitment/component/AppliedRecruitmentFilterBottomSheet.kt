package `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.component.FilterSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentFilterBottomSheetLayout
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
    RecruitmentFilterBottomSheetLayout(
        onDismiss = onDismiss,
        onReset = onReset,
        onApply = onApply,
        contentVerticalArrangement = Arrangement.spacedBy(32.dp)
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
