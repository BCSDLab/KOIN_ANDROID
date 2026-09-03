package `in`.koreatech.koin.feature.recruitment.ui.main.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentLocation
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentStatus
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentFilterState
import kotlinx.collections.immutable.persistentListOf

@Composable
fun RecruitmentAppliedFilterChipGroup(
    filterState: RecruitmentFilterState,
    onRemoveStatus: () -> Unit,
    onRemoveCategory: (RecruitmentCategory) -> Unit,
    onRemoveLocation: () -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filterState.selectedStatus?.let { status ->
            key(status) {
                AppliedFilterChip(labelRes = status.labelRes, onRemove = onRemoveStatus)
            }
        }
        filterState.selectedCategories.forEach { category ->
            key(category) {
                AppliedFilterChip(labelRes = category.labelRes) { onRemoveCategory(category) }
            }
        }
        filterState.selectedLocation?.let { location ->
            key(location) {
                AppliedFilterChip(labelRes = location.labelRes, onRemove = onRemoveLocation)
            }
        }
    }
}

@Composable
private fun AppliedFilterChip(
    @StringRes labelRes: Int,
    onRemove: () -> Unit
) {
    RecruitmentChip(
        text = stringResource(labelRes),
        colors = RecruitmentChipDefaults.colors(
            containerColor = RebrandKoinTheme.colors.primary500,
            contentColor = RebrandKoinTheme.colors.neutral0
        ),
        textStyle = RebrandKoinTheme.typography.medium14,
        contentPadding = PaddingValues(start = 14.dp, end = 10.dp, top = 7.dp, bottom = 7.dp),
        trailingIcon = ImageVector.vectorResource(R.drawable.ic_recruitment_close),
        trailingIconContentDescription = stringResource(
            R.string.recruitment_applied_filter_remove_content_description
        ),
        onTrailingIconClick = onRemove
    )
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
                selectedLocation = RecruitmentLocation.ONLINE
            ),
            onRemoveStatus = {},
            onRemoveCategory = {},
            onRemoveLocation = {}
        )
    }
}
