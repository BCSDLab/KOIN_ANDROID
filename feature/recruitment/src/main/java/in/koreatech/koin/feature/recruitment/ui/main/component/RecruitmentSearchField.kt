package `in`.koreatech.koin.feature.recruitment.ui.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R

@Composable
fun RecruitmentSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = stringResource(R.string.recruitment_search_hint)
) {
    val typography = RebrandKoinTheme.typography
    val contentColor = RebrandKoinTheme.colors.neutral600
    val textStyle = remember(typography, contentColor) {
        typography.regular12.copy(color = contentColor)
    }

    Row(
        modifier = modifier
            .height(RecruitmentChipDefaults.PillHeight)
            .clip(RecruitmentChipDefaults.PillShape)
            .background(RebrandKoinTheme.colors.neutral0)
            .border(
                width = 1.dp,
                color = RebrandKoinTheme.colors.primary100,
                shape = RecruitmentChipDefaults.PillShape
            )
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            modifier = Modifier.weight(1f),
            value = value,
            textStyle = textStyle,
            singleLine = true,
            onValueChange = onValueChange,
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            style = textStyle,
                            color = RebrandKoinTheme.colors.neutral500
                        )
                    }
                    innerTextField()
                }
            }
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_recruitment_search),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(21.dp)
        )
    }
}

@Preview
@Composable
private fun RecruitmentSearchFieldPreview() {
    RebrandKoinTheme {
        RecruitmentSearchField(
            value = "",
            onValueChange = {}
        )
    }
}
