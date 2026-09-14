package `in`.koreatech.koin.core.designsystem.component.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.R
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KoinTopAppBar2(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {},
    colors: TopAppBarColors = KoinTopAppBar2Defaults.topAppBarColors()
) {
    CenterAlignedTopAppBar(
        title = title,
        modifier = modifier.padding(horizontal = 20.dp),
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .noRippleClickable { onNavigationIconClick() },
                imageVector = ImageVector.vectorResource(R.drawable.ic_toolbar_navigate_back),
                contentDescription = stringResource(R.string.navigate_up_content_description)
            )
        },
        actions = actions,
        colors = colors
    )
}

object KoinTopAppBar2Defaults {
    @Composable
    fun topAppBarColors(
        containerColor: Color = RebrandKoinTheme.colors.neutral0,
        scrolledContainerColor: Color = Color.Unspecified,
        navigationIconContentColor: Color = RebrandKoinTheme.colors.neutral800,
        titleContentColor: Color = RebrandKoinTheme.colors.neutral800,
        actionIconContentColor: Color = RebrandKoinTheme.colors.neutral800,
        subtitleContentColor: Color = Color.Unspecified
    ): TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor,
        scrolledContainerColor,
        navigationIconContentColor,
        titleContentColor,
        actionIconContentColor,
        subtitleContentColor
    )
}