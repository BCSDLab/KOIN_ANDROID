package `in`.koreatech.koin.feature.recruitment.ui.recruitmentapply.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

private val RowShape = RoundedCornerShape(16.dp)
private val RowHeight = 40.dp
private val RowPadding = PaddingValues(top = 8.dp, end = 12.dp, bottom = 8.dp, start = 12.dp)

@Composable
fun RecruitmentSkillFieldRow(
    value: String,
    onValueChange: (String) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "기술 또는 자격증을 입력해주세요."
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(RowHeight)
            .border(1.dp, RebrandKoinTheme.colors.neutral200, RowShape)
            .background(RebrandKoinTheme.colors.neutral0, RowShape)
            .padding(RowPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty()) {
                Text(
                    text = hint,
                    style = RebrandKoinTheme.typography.regular14,
                    color = RebrandKoinTheme.colors.neutral400
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = RebrandKoinTheme.typography.regular14.copy(
                    color = RebrandKoinTheme.colors.neutral800
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "삭제",
            tint = RebrandKoinTheme.colors.neutral500,
            modifier = Modifier
                .size(20.dp)
                .noRippleClickable { onRemove() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentSkillFieldRowPreview() {
    RebrandKoinTheme {
        RecruitmentSkillFieldRow(
            value = "정보처리기사",
            onValueChange = {},
            onRemove = {}
        )
    }
}
