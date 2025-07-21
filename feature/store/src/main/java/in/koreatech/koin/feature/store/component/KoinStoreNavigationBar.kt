package `in`.koreatech.koin.feature.store.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun KoinStoreNavigationBar(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.systemBars.only(
        WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
    ),
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(windowInsets)
            .consumeWindowInsets(windowInsets)
            .selectableGroup(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
fun RowScope.KoinStoreNavigationBarItem(
    selected: Boolean,
    icon: Painter,
    label: String,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    val animatedTintColor by animateColorAsState(
        targetValue = if (selected) RebrandKoinTheme.colors.primary500 else RebrandKoinTheme.colors.neutral400,
        label = "NavigationBarItemTintColor"
    )

    Column(
        modifier = Modifier.then(
            if (enabled) {
                Modifier.noRippleClickable {
                    onClick()
                }
            } else {
                Modifier
            }
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier.paint(
                painter = icon,
                colorFilter = ColorFilter.tint(animatedTintColor)
            )
        )

        Spacer(modifier = Modifier.height(2.dp))

        BasicText(
            text = label,
            style = RebrandKoinTheme.typography.bold12
        )
    }
}
