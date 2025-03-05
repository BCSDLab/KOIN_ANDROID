package `in`.koreatech.koin.core.designsystem.component.icon

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Stable
@Composable
fun StableIcon(
    modifier: Modifier = Modifier,
    @DrawableRes drawableResId: Int,
    description: String? = null,
    tint: Color = LocalContentColor.current,
) {
    val painter = painterResource(id = drawableResId)
    Icon(
        painter = painter,
        contentDescription = description,
        tint = tint,
        modifier = modifier,
    )
}
