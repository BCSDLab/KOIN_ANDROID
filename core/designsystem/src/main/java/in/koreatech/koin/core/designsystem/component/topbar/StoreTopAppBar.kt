package `in`.koreatech.koin.core.designsystem.component.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.R
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = KoinTheme.typography.medium18,
    onNavigationIconClick: () -> Unit = {},
    actions:
    @Composable()
    (RowScope.() -> Unit) = {},
    colors: TopAppBarColors = StoreTopAppBarDefaults.colors()
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = textStyle
            )
        },
        modifier = modifier,
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .padding(start = 24.dp)
                    .size(24.dp)
                    .noRippleClickable { onNavigationIconClick() },
                painter = painterResource(R.drawable.ic_arrow_back_ios_new),
                contentDescription = stringResource(R.string.navigate_up_content_description)
            )
        },
        actions = actions,
        colors = colors
    )
}

@OptIn(ExperimentalMaterial3Api::class)
object StoreTopAppBarDefaults {
    @Composable
    fun colors(
        containerColor: Color = RebrandKoinTheme.colors.neutral0,
        navigationIconContentColor: Color = RebrandKoinTheme.colors.neutral800,
        titleContentColor: Color = RebrandKoinTheme.colors.neutral800,
        actionIconContentColor: Color = RebrandKoinTheme.colors.neutral0
    ) = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = containerColor,
        navigationIconContentColor = navigationIconContentColor,
        titleContentColor = titleContentColor,
        actionIconContentColor = actionIconContentColor
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun StoreTopAppBarPreview() {
    StoreTopAppBar(
        title = "상점",
        actions = { }
    )
}
