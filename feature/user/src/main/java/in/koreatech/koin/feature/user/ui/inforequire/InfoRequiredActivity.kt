package `in`.koreatech.koin.feature.user.ui.inforequire

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.feature.user.R
import `in`.koreatech.koin.feature.user.ui.userinfo.UserInfoActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InfoRequiredActivity : ComponentActivity() {

    private val viewModel: InfoRequiredViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdgeWithLightStatusBar()

        val isFull = intent.getBooleanExtra(EXTRA_IS_FULL, false)
        viewModel.onStart(isFull)

        observeEvent()

        setContent {
            KoinTheme {
                if (isFull) {
                    InfoRequiredFullScreen(
                        buttonText = stringResource(R.string.info_required_full_button),
                        titleText = stringResource(R.string.info_required_full_title),
                        descriptionText = stringResource(R.string.info_required_full_description),
                        onPositive = {
                            viewModel.onPositiveClick()
                        }
                    )
                } else {
                    InfoRequiredModalScreen(
                        title = stringResource(R.string.info_required_modal_title),
                        description = stringResource(R.string.info_required_modal_description),
                        descriptionStyle = KoinTheme.typography.regular12.copy(
                            textAlign = TextAlign.Center,
                            color = KoinTheme.colors.neutral500
                        ),
                        onPositive = {
                            viewModel.onPositiveClick()
                        },
                        onNegative = {
                            viewModel.onNegativeClick()
                        }
                    )
                }
            }
        }
    }

    private fun observeEvent() {
        lifecycleScope.launch {
            viewModel.event.collectLatest { event: InfoRequiredViewModel.UiEvent ->
                when (event) {
                    InfoRequiredViewModel.UiEvent.NavigateToUserInfo -> {
                        startActivity(Intent(this@InfoRequiredActivity, UserInfoActivity::class.java))
                        finishWithTransition()
                    }

                    InfoRequiredViewModel.UiEvent.Finish -> {
                        finishWithTransition()
                    }
                }
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun finishWithTransition() {
        if (Build.VERSION.SDK_INT >= 34) {
            overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, 0, 0)
        } else {
            overridePendingTransition(0, 0)
        }
        finish()
    }

    companion object {
        const val EXTRA_IS_FULL = "extra_is_full"
    }
}
