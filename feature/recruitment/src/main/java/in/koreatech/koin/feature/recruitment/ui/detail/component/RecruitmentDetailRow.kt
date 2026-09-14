package `in`.koreatech.koin.feature.recruitment.ui.detail.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R

@Composable
internal fun RecruitmentDetailRow(
    @DrawableRes iconRes: Int,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    iconTint: Color = RebrandKoinTheme.colors.primary600
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = ImageVector.vectorResource(iconRes),
                contentDescription = null,
                tint = iconTint
            )
            Text(
                text = label,
                style = RebrandKoinTheme.typography.medium12.copy(
                    color = RebrandKoinTheme.colors.neutral800
                )
            )
        }
        Text(
            text = value,
            style = RebrandKoinTheme.typography.regular12.copy(
                color = RebrandKoinTheme.colors.neutral800
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentDetailRowPreview() {
    RebrandKoinTheme {
        RecruitmentDetailRow(
            iconRes = R.drawable.ic_recruitment_author,
            label = "작성자",
            value = "코인이"
        )
    }
}
