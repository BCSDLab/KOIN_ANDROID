package `in`.koreatech.koin.feature.setting.ui

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.setting.R
import `in`.koreatech.koin.feature.setting.component.SettingItem
import `in`.koreatech.koin.feature.setting.component.SettingTitle
import `in`.koreatech.koin.feature.setting.component.SettingVersionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    // viewModel: SettingViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = KoinTheme.colors.neutral0,
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.setting_appbar_title),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = KoinTheme.colors.primary500,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                onNavigationIconClick = onTopbarBackClick
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { contentPadding ->
        SettingScreenImpl(
            contentPadding
        )
    }
}

@Composable
fun SettingScreenImpl(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        SettingTitle(
            text = stringResource(R.string.setting_title_normal),
        )
        SettingItem(
            text = stringResource(R.string.setting_item_profile),
            showIcon = true
        )
        SettingItem(
            text = stringResource(R.string.setting_item_change_password),
            showIcon = true
        )
        SettingItem(
            text = stringResource(R.string.setting_item_notification),
            showIcon = true
        )
        SettingTitle(
            text = stringResource(R.string.setting_title_service)
        )
        SettingItem(
            text = stringResource(R.string.setting_item_privacy_policy)
        )
        SettingItem(
            text = stringResource(R.string.setting_item_koin_terms)
        )
        SettingItem(
            text = stringResource(R.string.setting_item_marketing_terms)
        )
        SettingItem(
            text = stringResource(R.string.setting_item_open_source_license)
        )
        SettingVersionItem(
            appVersion = "4.5.2",
            currentVersion = "4.5.3",
            showVersionInfo = false
        )
        Spacer(Modifier.weight(1f))
        SettingItem(
            text = stringResource(R.string.setting_item_contact)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SettingScreenPreview() {
    SettingScreenImpl(
        contentPadding = PaddingValues()
    )
}
