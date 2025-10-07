package `in`.koreatech.koin.feature.setting.ui

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.navigation.utils.goToUrl
import `in`.koreatech.koin.feature.setting.R
import `in`.koreatech.koin.feature.setting.component.SettingItem
import `in`.koreatech.koin.feature.setting.component.SettingTitle
import `in`.koreatech.koin.feature.setting.component.SettingVersionItem
import `in`.koreatech.koin.feature.setting.constant.URLConstant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    onTopbarBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onServiceClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onKoinTermsClick: () -> Unit = {},
    onMarketingTermsClick: () -> Unit = {}
) {
    val versionState by viewModel.versionState.collectAsState()

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
        var currentVersionName = ""
        var latestVersionName = ""
        when (versionState) { // smartcast not working
            is VersionState.Outdated -> {
                currentVersionName = (versionState as VersionState.Outdated).currentVersion
                latestVersionName = (versionState as VersionState.Outdated).latestVersion
            }
            is VersionState.Latest -> {
                currentVersionName = (versionState as VersionState.Latest).currentVersion
                latestVersionName = currentVersionName
            }
            else -> {}
        }
        SettingScreenImpl(
            isLoggedIn = viewModel.isLoggedIn,
            currentVersionName = currentVersionName,
            latestVersionName = latestVersionName,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding),
            onPrivacyPolicyClick = onPrivacyPolicyClick,
            onKoinTermsClick = onKoinTermsClick,
            onMarketingTermsClick = onMarketingTermsClick
        )
    }
}

@Composable
fun SettingScreenImpl(
    isLoggedIn: Boolean,
    currentVersionName: String,
    latestVersionName: String,
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onServiceClick: () -> Unit = {},
    onPrivacyPolicyClick: () -> Unit = {},
    onKoinTermsClick: () -> Unit = {},
    onMarketingTermsClick: () -> Unit = {}
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SettingTitle(
            text = stringResource(R.string.setting_title_normal)
        )
        SettingItem(
            text = stringResource(R.string.setting_item_profile),
            showIcon = true,
            onClick = {}
        )
        SettingItem(
            text = stringResource(R.string.setting_item_change_password),
            showIcon = true,
            onClick = {}
        )
        SettingItem(
            text = stringResource(R.string.setting_item_notification),
            showIcon = true,
            onClick = {}
        )
        SettingTitle(
            text = stringResource(R.string.setting_title_service)
        )
        SettingItem(
            text = stringResource(R.string.setting_item_privacy_policy),
            onClick = onPrivacyPolicyClick
        )
        SettingItem(
            text = stringResource(R.string.setting_item_koin_terms),
            onClick = onKoinTermsClick
        )
        SettingItem(
            text = stringResource(R.string.setting_item_marketing_terms),
            onClick = onMarketingTermsClick
        )
        SettingItem(
            text = stringResource(R.string.setting_item_open_source_license),
            onClick = {
                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            }
        )
        SettingVersionItem(
            currentVersion = currentVersionName,
            latestVersion = latestVersionName,
            showVersionInfo = currentVersionName.isNotEmpty() && latestVersionName.isNotEmpty()
        )
        Spacer(Modifier.weight(1f))
        SettingItem(
            text = stringResource(R.string.setting_item_contact),
            onClick = {
                context.goToUrl(URLConstant.KOIN_ASK_FORM.toUri())
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SettingScreenPreview() {
    SettingScreenImpl(
        isLoggedIn = false,
        currentVersionName = "4.5.2",
        latestVersionName = "4.5.3"
    )
}
