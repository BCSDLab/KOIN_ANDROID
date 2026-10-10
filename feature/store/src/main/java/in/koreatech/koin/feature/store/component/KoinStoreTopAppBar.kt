package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar2
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar2Defaults
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KoinStoreTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = KoinTheme.typography.medium18,
    overlayAlpha: () -> Float = { 1f },
    onNavigationIconClick: () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {},
    colors: TopAppBarColors = KoinTopAppBar2Defaults.topAppBarColors(
        containerColor = Color.White,
        navigationIconContentColor = Color.Black,
        titleContentColor = Color.Black,
        actionIconContentColor = Color.Black
    ),
    expandedColors: TopAppBarColors = KoinTopAppBar2Defaults.topAppBarColors(
        containerColor = Color.Transparent,
        navigationIconContentColor = Color.White,
        titleContentColor = Color.White,
        actionIconContentColor = Color.White
    ),
    content: @Composable () -> Unit = {}
) {
    val useCollapsedColors by remember(overlayAlpha) {
        derivedStateOf { overlayAlpha().coerceIn(0f, 1f) >= 0.5f }
    }
    val activeColors = if (useCollapsedColors) colors else expandedColors

    Box(
        modifier = modifier
    ) {
        content()

        KoinTopAppBar2(
            title = {
                Text(
                    text = title,
                    modifier = Modifier.graphicsLayer {
                        alpha = overlayAlpha().coerceIn(0f, 1f)
                    },
                    style = textStyle.copy(color = colors.titleContentColor)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRect(expandedColors.containerColor)
                    drawRect(
                        color = colors.containerColor,
                        alpha = overlayAlpha().coerceIn(0f, 1f)
                    )
                }
                .zIndex(2f),
            onNavigationIconClick = onNavigationIconClick,
            actions = actions,
            colors = KoinTopAppBar2Defaults.topAppBarColors(
                containerColor = Color.Transparent,
                navigationIconContentColor = activeColors.navigationIconContentColor,
                titleContentColor = colors.titleContentColor,
                actionIconContentColor = activeColors.actionIconContentColor
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun KoinStoreTopAppBarPreview() {
    KoinStoreTopAppBar(
        title = "Title",
        actions = { }
    )
}
