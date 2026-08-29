package `in`.koreatech.koin.feature.recruitment.ui.chat.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.component.tab.KoinSurface
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun RecruitmentChatTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    KoinTopAppBar(
        modifier = modifier,
        title = title,
        textStyle = RebrandKoinTheme.typography.medium18,
        onNavigationIconClick = onNavigationIconClick,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = RebrandKoinTheme.colors.neutral0,
            navigationIconContentColor = RebrandKoinTheme.colors.neutral800,
            titleContentColor = RebrandKoinTheme.colors.neutral800,
            actionIconContentColor = RebrandKoinTheme.colors.neutral600
        ),
        actions = actions
    )
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentChatTopBarPreview() {
    KoinSurface {
        RecruitmentChatTopBar(title = "낭만고양이")
    }
}
