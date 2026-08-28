package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.recruitment.R

@Composable
internal fun RecruitmentFilterButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(RebrandKoinTheme.colors.neutral0, RoundedCornerShape(40.dp))
            .clip(RoundedCornerShape(40.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.recruitment_filter),
            style = RebrandKoinTheme.typography.regular12,
            color = RebrandKoinTheme.colors.neutral700
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_filter_horizontal),
            contentDescription = null,
            modifier = Modifier.size(21.dp),
            tint = RebrandKoinTheme.colors.primary500
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun RecruitmentFilterButtonPreview() {
    RebrandKoinTheme {
        RecruitmentFilterButton(onClick = {})
    }
}
