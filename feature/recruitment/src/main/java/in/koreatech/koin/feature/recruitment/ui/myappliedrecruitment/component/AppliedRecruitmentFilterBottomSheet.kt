package `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.component.FilterSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentFilterBottomSheetLayout
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.AppliedFilterSort
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.AppliedFilterState
import `in`.koreatech.koin.feature.recruitment.ui.myappliedrecruitment.model.AppliedFilterStatus
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppliedRecruitmentFilterBottomSheet(
    currentFilter: AppliedFilterState,
    onDismiss: () -> Unit,
    onApply: (AppliedFilterState) -> Unit
) {
    var localStatus by rememberSaveable { mutableStateOf(currentFilter.status) }
    var localSort by rememberSaveable { mutableStateOf(currentFilter.sort) }

    val context = LocalContext.current
    val statusOrder = remember {
        listOf(
            AppliedFilterStatus.ALL to context.getString(R.string.recruitment_filter_status_all),
            AppliedFilterStatus.APPROVED to context.getString(R.string.recruitment_applied_status_approved),
            AppliedFilterStatus.PENDING to context.getString(R.string.recruitment_applied_status_pending),
            AppliedFilterStatus.REJECTED to context.getString(R.string.recruitment_applied_status_rejected)
        )
    }
    val sortOrder = remember {
        listOf(
            AppliedFilterSort.LATEST to context.getString(R.string.recruitment_filter_sort_latest),
            AppliedFilterSort.DEADLINE to context.getString(R.string.recruitment_filter_sort_deadline)
        )
    }
    val statusChips = remember(localStatus) {
        statusOrder.map { (status, label) -> label to (localStatus == status) }.toPersistentList()
    }
    val sortChips = remember(localSort) {
        sortOrder.map { (sort, label) -> label to (localSort == sort) }.toPersistentList()
    }

    RecruitmentFilterBottomSheetLayout(
        onDismiss = onDismiss,
        onReset = {
            localStatus = AppliedFilterStatus.ALL
            localSort = AppliedFilterSort.LATEST
        },
        onApply = { onApply(AppliedFilterState(status = localStatus, sort = localSort)) },
        contentVerticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        FilterSection(
            title = stringResource(R.string.recruitment_applied_filter_status_section),
            chips = statusChips,
            onChipClick = { index -> localStatus = statusOrder[index].first }
        )

        FilterSection(
            title = stringResource(R.string.recruitment_filter_sort),
            chips = sortChips,
            onChipClick = { index -> localSort = sortOrder[index].first }
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
            onApply = {}
        )
    }
}
