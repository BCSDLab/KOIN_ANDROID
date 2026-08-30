package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruitmentDropdown(
    text: String,
    items: ImmutableList<String>,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    isPlaceholder: Boolean = false,
    onExpandedChange: (Boolean) -> Unit = {},
    onItemSelected: (Int) -> Unit = {}
) {
    val rotateDegree: Float by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "recruitment_dropdown_arrow_rotation"
    )

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .border(1.dp, RebrandKoinTheme.colors.neutral200, RebrandKoinTheme.shapes.small)
                .background(RebrandKoinTheme.colors.neutral0, RebrandKoinTheme.shapes.small)
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = RebrandKoinTheme.typography.regular14,
                color = if (isPlaceholder) RebrandKoinTheme.colors.neutral400 else RebrandKoinTheme.colors.neutral800,
                maxLines = 1
            )
            Icon(
                modifier = Modifier.rotate(rotateDegree),
                imageVector = ImageVector.vectorResource(R.drawable.ic_keyboard_arrow_down),
                contentDescription = null,
                tint = RebrandKoinTheme.colors.neutral500
            )
        }

        ExposedDropdownMenu(
            modifier = Modifier.wrapContentSize(),
            expanded = isExpanded,
            onDismissRequest = { onExpandedChange(false) },
            containerColor = RebrandKoinTheme.colors.neutral0
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    Text(
                        text = item,
                        style = RebrandKoinTheme.typography.regular14,
                        color = RebrandKoinTheme.colors.neutral800,
                        modifier = Modifier
                            .fillMaxWidth()
                            .noRippleClickable {
                                onItemSelected(index)
                                onExpandedChange(false)
                            }
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun RecruitmentDropdownPreview() {
    RebrandKoinTheme {
        RecruitmentDropdown(
            text = "공모전",
            items = persistentListOf("공모전", "대외활동", "스터디", "프로젝트", "기타"),
            isExpanded = false,
            isPlaceholder = true
        )
    }
}
