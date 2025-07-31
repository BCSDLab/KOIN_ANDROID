package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import `in`.koreatech.koin.core.designsystem.R
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

/**
 * KoinStoreTopAppBar is a copy of [KoinTopAppBar] with few modifications.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KoinStoreTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = KoinTheme.typography.medium18,
    overlayAlpha: State<Float> = mutableFloatStateOf(1f),
    onNavigationIconClick: () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {},
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Color.White,
        navigationIconContentColor = Color.Black,
        titleContentColor = Color.Black,
        actionIconContentColor = Color.Black
    ),
    expandedColors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Color.Transparent,
        navigationIconContentColor = Color.White,
        titleContentColor = Color.White,
        actionIconContentColor = Color.White
    ),
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = modifier
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    style = textStyle.copy(color = colors.titleContentColor)
                )
            },
            modifier = modifier.graphicsLayer {
                alpha = overlayAlpha.value
            },
            navigationIcon = {
                Icon(
                    modifier = Modifier
                        .size(36.dp)
                        .noRippleClickable { onNavigationIconClick() },
                    imageVector = Icons.AutoMirrored.Sharp.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.navigate_up_content_description),
                    tint = colors.actionIconContentColor
                )
            },
            actions = actions,
            colors = colors
        )
        CenterAlignedTopAppBar(
            title = {},
            modifier = Modifier.zIndex(2f).graphicsLayer {
                alpha = 1f - overlayAlpha.value
            },
            navigationIcon = {
                Icon(
                    modifier = Modifier
                        .size(36.dp)
                        .noRippleClickable { onNavigationIconClick() },
                    imageVector = Icons.AutoMirrored.Sharp.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.navigate_up_content_description),
                    tint = expandedColors.actionIconContentColor
                )
            },
            actions = actions,
            colors = expandedColors
        )
        content()
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
