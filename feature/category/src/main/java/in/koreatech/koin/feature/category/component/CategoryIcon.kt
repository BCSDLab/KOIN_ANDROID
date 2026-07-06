package `in`.koreatech.koin.feature.category.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.category.R

@Composable
fun CategoryIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = RebrandKoinTheme.colors.primary500,
    backgroundColor: Color = RebrandKoinTheme.colors.neutral100
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

@Preview
@Composable
private fun CategoryIconPreview() {
    RebrandKoinTheme {
        CategoryIcon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_calendar_category),
            contentDescription = null
        )
    }
}
