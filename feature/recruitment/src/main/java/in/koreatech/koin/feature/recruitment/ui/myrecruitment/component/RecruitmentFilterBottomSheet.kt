package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.component.FilterSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentFilterBottomSheetLayout
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentFilterSort
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentFilterState
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentFilterStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruitmentFilterBottomSheet(
    currentFilter: RecruitmentFilterState,
    onDismiss: () -> Unit,
    onFilterChange: (RecruitmentFilterState) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit
) {
    RecruitmentFilterBottomSheetLayout(
        onDismiss = onDismiss,
        onReset = onReset,
        onApply = onApply
    ) {
        FilterSection(
            title = stringResource(R.string.recruitment_filter_status),
            chips = listOf(
                stringResource(R.string.recruitment_filter_status_all) to (currentFilter.status == RecruitmentFilterStatus.ALL),
                stringResource(R.string.recruitment_filter_status_recruiting) to (currentFilter.status == RecruitmentFilterStatus.RECRUITING),
                stringResource(R.string.recruitment_filter_status_complete) to (currentFilter.status == RecruitmentFilterStatus.COMPLETE)
            ),
            onChipClick = { index ->
                val newStatus = when (index) {
                    0 -> RecruitmentFilterStatus.ALL
                    1 -> RecruitmentFilterStatus.RECRUITING
                    else -> RecruitmentFilterStatus.COMPLETE
                }
                onFilterChange(currentFilter.copy(status = newStatus))
            }
        )

        FilterSection(
            title = stringResource(R.string.recruitment_filter_sort),
            chips = listOf(
                stringResource(R.string.recruitment_filter_sort_latest) to (currentFilter.sort == RecruitmentFilterSort.LATEST),
                stringResource(R.string.recruitment_filter_sort_deadline) to (currentFilter.sort == RecruitmentFilterSort.DEADLINE)
            ),
            onChipClick = { index ->
                val newSort = if (index == 0) RecruitmentFilterSort.LATEST else RecruitmentFilterSort.DEADLINE
                onFilterChange(currentFilter.copy(sort = newSort))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun RecruitmentFilterBottomSheetPreview() {
    RebrandKoinTheme {
        RecruitmentFilterBottomSheet(
            currentFilter = RecruitmentFilterState(),
            onDismiss = {},
            onFilterChange = {},
            onReset = {},
            onApply = {}
        )
    }
}
