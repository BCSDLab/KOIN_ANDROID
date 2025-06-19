package `in`.koreatech.koin.feature.user.ui.signin

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.feature.user.DEEPLINK_ARTICLE
import `in`.koreatech.koin.feature.user.DEEPLINK_MAIN
import `in`.koreatech.koin.feature.user.DEEPLINK_TIMETABLE

@AndroidEntryPoint
class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (ignore: IllegalStateException) {
        }
        enableEdgeToEdgeWithLightStatusBar()
        setContent {
            KoinTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = KoinTheme.colors.neutral0
                ) { innerPadding ->
                    SignInScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding),
                        nextRoute = {
                            goToNextRoute()
                        }
                    )
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
                        data = link.toUri()
                    }
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Intent(Intent.ACTION_VIEW).apply {
                    data = DEEPLINK_MAIN.toUri()
                }.let {
                    startActivity(it)
                }
                finish()
            }
        } else {
            if (handleTimetableIntent()) {
                Intent(Intent.ACTION_VIEW).apply {
                    data = DEEPLINK_TIMETABLE.toUri()
                }.let {
                    startActivity(it)
                }
                finish()
                return
            } else if (handleArticleIntent()) {
                val bundle = intent.getBundleExtra(BUNDLE_EXTRA_KEY)
                val startBoard = bundle?.getInt(START_BOARD, 4)
                startActivity(
                    Intent(Intent.ACTION_VIEW).apply {
                        data = DEEPLINK_ARTICLE.toUri()
                        putExtra(
                            BUNDLE_EXTRA_KEY,
                            bundleOf(
                                START_BOARD to startBoard
                            )
                        )
                    }
                )
                finish()
                return
            }
            Intent(Intent.ACTION_VIEW).apply {
                data = DEEPLINK_MAIN.toUri()
            }.let {
                startActivity(it)
            }
            finish()
        }
    }

    private fun handleTimetableIntent(): Boolean {
        val bundle = intent.getBundleExtra(BUNDLE_EXTRA_KEY)
        return bundle?.getBoolean(NAV_TIMETABLE, false) ?: false
    }

    private fun handleArticleIntent(): Boolean {
        val bundle = intent.getBundleExtra(BUNDLE_EXTRA_KEY)
        return bundle?.getBoolean(NAV_ARTICLE, false) ?: false
    }

    companion object {
        const val BUNDLE_EXTRA_KEY = "BUNDLE_EXTRA_KEY"
        const val NAV_TIMETABLE = "timetable"
        const val NAV_ARTICLE = "article"
        const val START_BOARD = "start_board"
    }
}
