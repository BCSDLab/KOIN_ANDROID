package `in`.koreatech.koin.feature.login.ui

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.feature.login.ui.component.UiStatus
import kotlinx.coroutines.launch
import kotlin.getValue
import `in`.koreatech.koin.feature.login.R

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initViewModel() = with(loginViewModel) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    loginState.collect {
                        when (it.status) {
                            is UiStatus.Init -> Unit
                            is UiStatus.Loading -> Unit
                            is UiStatus.Success -> goToNextRoute()
                            is UiStatus.Failed -> {
                                loginViewModel.onLoginFalse(it.status.message)
                            }
                        }
                    }
                }
                launch {
                    loginEvent.collect { event ->
                        when (event) {
                            LoginEvent.SIGNUP -> {
                                loginViewModel.resetLoginEvent()
                                val uri = Uri.parse("koin://signup/activity")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                                EventLogger.logClickEvent(
                                    EventAction.USER,
                                    AnalyticsConstant.Label.LOGIN,
                                    getString(R.string.sign_up)
                                )
                            }
                            LoginEvent.FIND_ID -> {
                                loginViewModel.resetLoginEvent()
                                // Todo id 찾기 aitivity가 아직 없음
                                // val uri = Uri.parse("koin://forgotid/activity")
                                // val intent = Intent(Intent.ACTION_VIEW, uri)
                                // startActivity(intent)
                            }
                            LoginEvent.FIND_PW -> {
                                loginViewModel.resetLoginEvent()
                                val uri = Uri.parse("koin://forgotpassword/activity")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            }
                            LoginEvent.TOUR -> {
                                val uri = Uri.parse("koin://home/home")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            }
                            LoginEvent.BUSINESS -> {
                                loginViewModel.resetLoginEvent()
                                val uri = Uri.parse("koin://businesslogin/activity")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
    }

    private fun goToNextRoute() {
        val uri = intent.data
        val link = uri?.getQueryParameter("link")

        if (link != null) {
            try {
                val intent =
                    Intent(Intent.ACTION_VIEW).apply {
                        flags = FLAG_ACTIVITY_CLEAR_TOP
                        data = Uri.parse(link)
                    }
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                val uri = Uri.parse("koin://home/home")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
                finish()
            }
        } else {
            if (handleTimetableIntent()) {
                val uri = Uri.parse("koin://timetable/activity")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
                finish()
                return
            } else if (handleArticleIntent()) {
                val fragment = 4
                val uri = Uri.parse("koin://article/activity?fragment=$fragment")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
                finish()
                return
            }
            val uri = Uri.parse("koin://home/home")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
            finish()
        }
    }

    private fun initView() {
        enableEdgeToEdgeWithLightStatusBar()
        setContent {
            KoinTheme {
                LoginPage(viewModel = loginViewModel)
            }
        }
    }
    private fun handleTimetableIntent(): Boolean {
        val bundle = intent.getBundleExtra(BUNDLE_LOGIN_EXTRA_KEY)
        return bundle?.getBoolean(NAV_TIMETABLE, false) == true
    }

    private fun handleArticleIntent(): Boolean {
        val bundle = intent.getBundleExtra(BUNDLE_ARTICLE_EXTRA_KEY)
        return bundle?.getBoolean(NAV_ARTICLE, false) == true
    }

    companion object {
        const val BUNDLE_LOGIN_EXTRA_KEY = "BUNDLE_EXTRA_KEY"
        const val NAV_TIMETABLE = "timetable"
        const val NAV_ARTICLE = "article"
        const val BUNDLE_ARTICLE_EXTRA_KEY = "BUNDLE_EXTRA_KEY"
    }
}
