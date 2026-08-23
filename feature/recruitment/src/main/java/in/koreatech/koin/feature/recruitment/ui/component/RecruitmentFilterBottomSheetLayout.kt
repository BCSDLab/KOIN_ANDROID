package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Vertical
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruitmentFilterBottomSheetLayout(
    onDismiss: () -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
    contentVerticalArrangement: Vertical = Arrangement.spacedBy(16.dp),
    content: @Composable () -> Unit
) {
    RecruitmentBottomSheet(onDismiss = onDismiss) {
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
                verticalArrangement = contentVerticalArrangement
            ) {
                content()
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
fun FilterSection(
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
fun FilterChip(
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
private fun RecruitmentFilterBottomSheetLayoutPreview() {
    RebrandKoinTheme {
        RecruitmentFilterBottomSheetLayout(
            onDismiss = {},
            onReset = {},
            onApply = {}
        ) {
            FilterSection(
                title = "상태",
                chips = listOf("전체" to true, "모집 중" to false, "모집 완료" to false),
                onChipClick = {}
            )
            FilterSection(
                title = "정렬",
                chips = listOf("최신순" to true, "마감 임박" to false),
                onChipClick = {}
            )
        }
    }
}
