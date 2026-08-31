package `in`.koreatech.koin.feature.recruitment.ui.recruitmentcreate.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R
import `in`.koreatech.koin.feature.recruitment.model.RecruitmentProgressType

@Composable
fun RecruitmentProgressTypeSelector(
    selected: RecruitmentProgressType?,
    onSelect: (RecruitmentProgressType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RecruitmentProgressType.entries.forEach { type ->
            key(type) {
                val isSelected = type == selected
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) RebrandKoinTheme.colors.primary500 else RebrandKoinTheme.colors.neutral200,
                            shape = RebrandKoinTheme.shapes.small
                        )
                        .background(
                            color = if (isSelected) RebrandKoinTheme.colors.primary500 else RebrandKoinTheme.colors.neutral0,
                            shape = RebrandKoinTheme.shapes.small
                        )
                        .clickable { onSelect(type) }
                        .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = type.iconRes()),
                        contentDescription = null,
                        tint = if (isSelected) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.neutral600,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = type.label,
                        style = if (isSelected) RebrandKoinTheme.typography.medium14 else RebrandKoinTheme.typography.regular14,
                        color = if (isSelected) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.neutral600,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@DrawableRes
private fun RecruitmentProgressType.iconRes(): Int = when (this) {
    RecruitmentProgressType.ONLINE -> R.drawable.ic_progress_online
    RecruitmentProgressType.OFFLINE -> R.drawable.ic_progress_offline
    RecruitmentProgressType.HYBRID -> R.drawable.ic_progress_hybrid
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentProgressTypeSelectorPreview() {
    RebrandKoinTheme {
        RecruitmentProgressTypeSelector(
            selected = RecruitmentProgressType.ONLINE,
            onSelect = {}
        )
    }
}
