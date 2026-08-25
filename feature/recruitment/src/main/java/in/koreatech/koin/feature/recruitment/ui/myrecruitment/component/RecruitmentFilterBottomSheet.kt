package `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.component

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
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.component.FilterSection
import `in`.koreatech.koin.feature.recruitment.ui.component.RecruitmentFilterBottomSheetLayout
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentFilterSort
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentFilterState
import `in`.koreatech.koin.feature.recruitment.ui.myrecruitment.model.RecruitmentFilterStatus
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruitmentFilterBottomSheet(
    currentFilter: RecruitmentFilterState,
    onDismiss: () -> Unit,
    onApply: (RecruitmentFilterState) -> Unit
) {
    var localStatus by rememberSaveable { mutableStateOf(currentFilter.status) }
    var localSort by rememberSaveable { mutableStateOf(currentFilter.sort) }

    val context = LocalContext.current
    val statusOrder = remember {
        listOf(
            RecruitmentFilterStatus.ALL to context.getString(R.string.recruitment_filter_status_all),
            RecruitmentFilterStatus.RECRUITING to context.getString(R.string.recruitment_filter_status_recruiting),
            RecruitmentFilterStatus.COMPLETE to context.getString(R.string.recruitment_filter_status_complete)
        )
    }
    val sortOrder = remember {
        listOf(
            RecruitmentFilterSort.LATEST to context.getString(R.string.recruitment_filter_sort_latest),
            RecruitmentFilterSort.DEADLINE to context.getString(R.string.recruitment_filter_sort_deadline)
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
            localStatus = RecruitmentFilterStatus.ALL
            localSort = RecruitmentFilterSort.LATEST
        },
        onApply = { onApply(RecruitmentFilterState(status = localStatus, sort = localSort)) }
    ) {
        FilterSection(
            title = stringResource(R.string.recruitment_filter_status),
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
private fun RecruitmentFilterBottomSheetPreview() {
    RebrandKoinTheme {
        RecruitmentFilterBottomSheet(
            currentFilter = RecruitmentFilterState(),
            onDismiss = {},
            onApply = {}
        )
    }
}
