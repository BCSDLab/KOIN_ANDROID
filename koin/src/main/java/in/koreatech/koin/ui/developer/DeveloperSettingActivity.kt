package `in`.koreatech.koin.ui.developer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar2
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar

@AndroidEntryPoint
class DeveloperSettingActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdgeWithLightStatusBar()
        setContent {
            KoinTheme {
                Scaffold(
                    topBar = {
                        KoinTopAppBar2(
                            title = {
                                Text(
                                    text = stringResource(R.string.setting_title_developer_setting)
                                )
                            },
                            onNavigationIconClick = {
                                finish()
                            }
                        )
                    },
                    containerColor = KoinTheme.colors.neutral0
                ) { contentPadding ->
                    DeveloperSettingScreen(
                        modifier = Modifier.padding(contentPadding)
                    )
                }
            }
        }
    }
}
