package `in`.koreatech.koin.feature.callvan.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.callvan.R

@Composable
fun CallvanRouteCount(
    currentCount: Int,
    maxCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_people),
            contentDescription = "",
            modifier = Modifier.size(16.dp),
            tint = Color.Unspecified
        )
        Text(
            text = "$currentCount/$maxCount",
            style = KoinTheme.typography.regular12,
            color = KoinTheme.colors.neutral600
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanPersonCountPreview() {
    CallvanRouteCount(
        currentCount = 1,
        maxCount = 8
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanPersonCountFullPreview() {
    CallvanRouteCount(
        currentCount = 8,
        maxCount = 8
    )
}
