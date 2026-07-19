package `in`.koreatech.koin.core.designsystem.component.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

object IconBadgeDefaults {
    val Shape: Shape = RoundedCornerShape(8.dp)
    val ContentPadding: PaddingValues = PaddingValues(8.dp)
    val IconSize: Dp = 24.dp

    @Composable
    fun colors(
        tint: Color = RebrandKoinTheme.colors.primary500,
        backgroundColor: Color = RebrandKoinTheme.colors.neutral100
    ): IconBadgeColors = IconBadgeColors(
        tint = tint,
        backgroundColor = backgroundColor
    )
}

@Immutable
data class IconBadgeColors(
    val tint: Color,
    val backgroundColor: Color
)

@Composable
fun IconBadge(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    colors: IconBadgeColors = IconBadgeDefaults.colors(),
    iconSize: Dp = IconBadgeDefaults.IconSize,
    shape: Shape = IconBadgeDefaults.Shape,
    contentPadding: PaddingValues = IconBadgeDefaults.ContentPadding
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(colors.backgroundColor)
            .padding(contentPadding),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = colors.tint
        )
    }
}

@Preview
@Composable
private fun IconBadgePreview() {
    RebrandKoinTheme {
        IconBadge(
            imageVector = Icons.Default.Home,
            contentDescription = null
        )
    }
}
