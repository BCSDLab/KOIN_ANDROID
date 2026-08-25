package `in`.koreatech.koin.feature.recruitment.ui.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentFilterState
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentLocation
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentStatus
import kotlinx.collections.immutable.persistentListOf

private data class AppliedFilterChipData(
    val labelRes: Int,
    val onRemove: () -> Unit
)

@Composable
fun RecruitmentAppliedFilterChipGroup(
    filterState: RecruitmentFilterState,
    onRemoveStatus: () -> Unit,
    onRemoveCategory: (RecruitmentCategory) -> Unit,
    onRemoveLocation: (RecruitmentLocation) -> Unit,
    modifier: Modifier = Modifier
) {
    val chips = buildList {
        filterState.selectedStatus?.let { status ->
            add(AppliedFilterChipData(status.labelRes, onRemoveStatus))
        }
        filterState.selectedCategories.forEach { category ->
            add(AppliedFilterChipData(category.labelRes) { onRemoveCategory(category) })
        }
        filterState.selectedLocations.forEach { location ->
            add(AppliedFilterChipData(location.labelRes) { onRemoveLocation(location) })
        }
    }

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        chips.forEach { chip ->
            RecruitmentChip(
                text = stringResource(chip.labelRes),
                colors = RecruitmentChipDefaults.colors(
                    containerColor = RebrandKoinTheme.colors.primary500,
                    contentColor = RebrandKoinTheme.colors.neutral0
                ),
                textStyle = RebrandKoinTheme.typography.medium14,
                contentPadding = PaddingValues(start = 14.dp, end = 10.dp, top = 7.dp, bottom = 7.dp),
                trailingIcon = painterResource(R.drawable.ic_recruitment_close),
                trailingIconContentDescription = stringResource(
                    R.string.recruitment_applied_filter_remove_content_description
                ),
                onTrailingIconClick = chip.onRemove
            )
        }
    }
}

@Preview
@Composable
private fun RecruitmentAppliedFilterChipGroupPreview() {
    RebrandKoinTheme {
        RecruitmentAppliedFilterChipGroup(
            filterState = RecruitmentFilterState(
                selectedStatus = RecruitmentStatus.RECRUITING,
                selectedCategories = persistentListOf(
                    RecruitmentCategory.CONTEST,
                    RecruitmentCategory.STUDY
                ),
                selectedLocations = persistentListOf(RecruitmentLocation.ONLINE)
            ),
            onRemoveStatus = {},
            onRemoveCategory = {},
            onRemoveLocation = {}
        )
    }
}
