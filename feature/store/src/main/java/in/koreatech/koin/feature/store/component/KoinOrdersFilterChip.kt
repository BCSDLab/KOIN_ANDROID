package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun KoinOrdersFilterChip(
    text: String,
    active: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val backgroundColor = if (active) RebrandKoinTheme.colors.primary500 else RebrandKoinTheme.colors.neutral0
    val textColor = if (active) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.neutral500

    Row(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RebrandKoinTheme.shapes.medium,
                ambientColor = RebrandKoinTheme.colors.neutral400,
                spotColor = RebrandKoinTheme.colors.neutral500
            )
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = RebrandKoinTheme.typography.bold14,
            color = textColor
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_store_arrow_down),
            contentDescription = "",
            tint = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DownClipTruePreview() {
    KoinOrdersFilterChip(
        text = "주소",
        active = true
    )
}
