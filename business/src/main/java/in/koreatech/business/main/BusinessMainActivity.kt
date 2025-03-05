package `in`.koreatech.business.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.forcrupdate.ForceUpdateScreen
import `in`.koreatech.business.feature.signup.businessauth.BusinessAuthScreen
import `in`.koreatech.business.feature.signup.businessauth.EnterBusinessNumberScreen
import `in`.koreatech.business.navigation.KoinBusinessNavHost
import `in`.koreatech.business.ui.theme.KOIN_ANDROIDTheme
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.domain.state.version.VersionUpdatePriority
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.YearMonth

@AndroidEntryPoint
class BusinessMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KOIN_ANDROIDTheme {
               KoinBusinessAppScreen()
            }
        }
    }
}

@Composable
fun KoinBusinessAppScreen(viewModel: BusinessMainActivityViewModel = hiltViewModel()) {
    val state = viewModel.collectAsState().value
    HandleSideEffects(viewModel)

    if (state.version != null) {
        when (state.version.versionUpdatePriority) {
            VersionUpdatePriority.None -> {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    KoinBusinessNavHost(
                        startDestination = state.destination,
                    )
                }
            }
            VersionUpdatePriority.Importance -> {
                ForceUpdateScreen(
                    title = state.version.title,
                    content = state.version.content,
                )
            }
        }
    }
}

@Composable
private fun HandleSideEffects(viewModel: BusinessMainActivityViewModel) {
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            BusinessMainSideEffect.NetWorkError -> {
                ToastUtil.getInstance().makeShort(R.string.version_check_failed)
            }
        }
    }
}
