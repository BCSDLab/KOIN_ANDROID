package `in`.koreatech.koin.feature.callvan.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R

@Composable
fun CallvanParticipantCount(
    currentCount: Int,
    maxCount: Int,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = RebrandKoinTheme.typography.regular12,
    iconSize: Dp = 16.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_participants),
            contentDescription = null,
            tint = RebrandKoinTheme.colors.neutral600,
            modifier = Modifier.size(iconSize)
        )
        Text(
            text = stringResource(R.string.callvan_detail_participants_count, currentCount, maxCount),
            style = textStyle,
            color = RebrandKoinTheme.colors.neutral600

        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanPersonCountPreview() {
    CallvanParticipantCount(
        currentCount = 1,
        maxCount = 8
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanPersonCountFullPreview() {
    CallvanParticipantCount(
        currentCount = 8,
        maxCount = 8
    )
}
