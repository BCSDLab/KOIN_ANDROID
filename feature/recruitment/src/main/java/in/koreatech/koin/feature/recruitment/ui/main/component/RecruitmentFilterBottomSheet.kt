package `in`.koreatech.koin.feature.recruitment.ui.main.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentCategory
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentLocation
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentStatus
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentFilterState
import `in`.koreatech.koin.feature.recruitment.ui.main.model.RecruitmentSort
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruitmentFilterBottomSheet(
    state: RecruitmentFilterState,
    onStatusClick: (RecruitmentStatus?) -> Unit,
    onSortClick: (RecruitmentSort) -> Unit,
    onCategoryClick: (RecruitmentCategory?) -> Unit,
    onLocationClick: (RecruitmentLocation?) -> Unit,
    onReset: () -> Unit,
    onApplyClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    // 스크림 탭·스와이프는 ModalBottomSheet 가 hide 애니메이션을 끝낸 뒤 onDismissRequest 를 호출하지만,
    // 버튼으로 닫는 경로는 상태가 즉시 바뀌어 애니메이션이 생략되므로 직접 hide 를 기다린다.
    val hideThen: (() -> Unit) -> Unit = { action ->
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) action()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = RebrandKoinTheme.colors.neutral0,
        sheetState = sheetState,
        dragHandle = null
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.recruitment_filter),
                    style = RebrandKoinTheme.typography.bold18,
                    color = RebrandKoinTheme.colors.primary500,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                IconButton(onClick = { hideThen(onDismissRequest) }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_recruitment_close),
                        contentDescription = stringResource(R.string.recruitment_filter_close_content_description),
                        tint = RebrandKoinTheme.colors.neutral600
                    )
                }
            }
            RecruitmentFilterContent(
                state = state,
                onStatusClick = onStatusClick,
                onSortClick = onSortClick,
                onCategoryClick = onCategoryClick,
                onLocationClick = onLocationClick,
                onReset = onReset,
                onApplyClick = { hideThen(onApplyClick) }
            )
        }
    }
}

@Composable
private fun RecruitmentFilterContent(
    state: RecruitmentFilterState,
    onStatusClick: (RecruitmentStatus?) -> Unit,
    onSortClick: (RecruitmentSort) -> Unit,
    onCategoryClick: (RecruitmentCategory?) -> Unit,
    onLocationClick: (RecruitmentLocation?) -> Unit,
    onReset: () -> Unit,
    onApplyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allLabel = stringResource(R.string.recruitment_filter_all)
    val configuration = LocalConfiguration.current
    val maxHeight = remember(configuration.screenHeightDp) {
        (configuration.screenHeightDp * 0.7f).dp
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = maxHeight)
            .padding(bottom = 20.dp)
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .verticalScroll(scrollState)
                .padding(top = 8.dp, start = 24.dp, end = 24.dp)
        ) {
            RecruitmentFilterSection(title = stringResource(R.string.recruitment_filter_section_status)) {
                RecruitmentFilterChip(
                    text = allLabel,
                    isSelected = state.selectedStatus == null,
                    onClick = { onStatusClick(null) }
                )
                RecruitmentStatus.ALL.forEach { status ->
                    key(status) {
                        RecruitmentFilterChip(
                            text = stringResource(status.labelRes),
                            isSelected = state.selectedStatus == status,
                            onClick = { onStatusClick(status) }
                        )
                    }
                }
            }

            RecruitmentFilterSection(title = stringResource(R.string.recruitment_filter_section_sort)) {
                RecruitmentSort.ALL.forEach { sort ->
                    key(sort) {
                        RecruitmentFilterChip(
                            text = stringResource(sort.labelRes),
                            isSelected = state.selectedSort == sort,
                            onClick = { onSortClick(sort) }
                        )
                    }
                }
            }

            RecruitmentFilterSection(title = stringResource(R.string.recruitment_filter_section_category)) {
                RecruitmentFilterChip(
                    text = allLabel,
                    isSelected = state.selectedCategories.isEmpty(),
                    onClick = { onCategoryClick(null) }
                )
                RecruitmentCategory.ALL.forEach { category ->
                    key(category) {
                        RecruitmentFilterChip(
                            text = stringResource(category.labelRes),
                            isSelected = category in state.selectedCategories,
                            onClick = { onCategoryClick(category) }
                        )
                    }
                }
            }

            RecruitmentFilterSection(title = stringResource(R.string.recruitment_location_label)) {
                RecruitmentFilterChip(
                    text = allLabel,
                    isSelected = state.selectedLocations.isEmpty(),
                    onClick = { onLocationClick(null) }
                )
                RecruitmentLocation.ALL.forEach { location ->
                    key(location) {
                        RecruitmentFilterChip(
                            text = stringResource(location.labelRes),
                            isSelected = location in state.selectedLocations,
                            onClick = { onLocationClick(location) }
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 24.dp, end = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onReset,
                shape = RebrandKoinTheme.shapes.medium,
                border = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral300),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = RebrandKoinTheme.colors.neutral0),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text(
                    text = stringResource(R.string.recruitment_filter_reset),
                    color = RebrandKoinTheme.colors.neutral600,
                    style = RebrandKoinTheme.typography.bold16
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_recruitment_reset),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = RebrandKoinTheme.colors.neutral500
                )
            }
            Button(
                onClick = onApplyClick,
                shape = RebrandKoinTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = RebrandKoinTheme.colors.primary500),
                modifier = Modifier
                    .weight(2f)
                    .height(48.dp)
            ) {
                Text(
                    text = stringResource(R.string.recruitment_filter_apply),
                    color = RebrandKoinTheme.colors.neutral0,
                    style = RebrandKoinTheme.typography.bold16
                )
            }
        }
    }
}

@Composable
private fun RecruitmentFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    RecruitmentChip(
        text = text,
        modifier = Modifier
            .padding(end = 8.dp, bottom = 8.dp)
            .semantics { selected = isSelected },
        colors = RecruitmentChipDefaults.selectableColors(isSelected),
        textStyle = RebrandKoinTheme.typography.bold14,
        shape = RoundedCornerShape(24.dp),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) {
                RebrandKoinTheme.colors.primary500
            } else {
                RebrandKoinTheme.colors.neutral300
            }
        ),
        showClickRipple = true,
        onClick = onClick
    )
}

@Composable
private fun RecruitmentFilterSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = title,
            style = RebrandKoinTheme.typography.bold16,
            color = RebrandKoinTheme.colors.neutral800,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        FlowRow(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}

@Preview
@Composable
private fun RecruitmentFilterContentPreview() {
    RebrandKoinTheme {
        RecruitmentFilterContent(
            state = RecruitmentFilterState(),
            onStatusClick = {},
            onSortClick = {},
            onCategoryClick = {},
            onLocationClick = {},
            onReset = {},
            onApplyClick = {}
        )
    }
}
