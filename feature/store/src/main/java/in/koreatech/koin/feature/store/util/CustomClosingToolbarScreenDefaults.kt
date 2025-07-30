package `in`.koreatech.koin.feature.store.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp

object CustomClosingToolbarScreenDefaults {
    val windowInsets: WindowInsets
        @Composable
        get() =
            WindowInsets.systemBars.only(
                WindowInsetsSides.Horizontal + WindowInsetsSides.Top
            )
}

@Stable
fun Modifier.customCollapsingToolbarContent(
    maxToolbarHeight: Dp,
    currentToolbarHeight: Dp,
    overlayAlpha: Float
) = this.then(
    Modifier.graphicsLayer {
        clip = true
        translationY = -(maxToolbarHeight.toPx() - currentToolbarHeight.toPx())
        alpha = 1f - overlayAlpha
    }
)
