package `in`.koreatech.koin.feature.user.ui.signin

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.enableEdgeToEdgeWithLightStatusBar
import `in`.koreatech.koin.feature.signin.ui.SignInScreen
import `in`.koreatech.koin.feature.user.DEEPLINK_ARTICLE
import `in`.koreatech.koin.feature.user.DEEPLINK_MAIN
import `in`.koreatech.koin.feature.user.DEEPLINK_TIMETABLE
import `in`.koreatech.koin.feature.user.ui.inforequire.InfoRequiredFullActivity
import `in`.koreatech.koin.feature.user.ui.inforequire.InfoRequiredModalActivity

@AndroidEntryPoint
class SignInActivity : ComponentActivity() {

    private lateinit var prefs: SharedPreferences
    private val viewModel: SignInViewModel by viewModels()
    private var isShownBefore: Int = -1
    private var modal: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = getSharedPreferences("info_required", MODE_PRIVATE)
        isShownBefore = prefs.getInt("isShownBefore", -1)    // -1:보여지지 않음, 0:보여줬는데 정보 수정 안함, 1:정보수정 완료
        viewModel.setIsShownBefore(isShownBefore)

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
        when (viewModel.getIsInfoRequired()) {
            -1 -> {
                prefs.edit { putInt("isShownBefore", 1) }
            }
            1 -> {
                if (isShownBefore == -1) {
                    // info Required 메시지 본 적 없음
                    startActivity(Intent(this, InfoRequiredFullActivity::class.java))
                    finish()
                    return
                }
                else if (isShownBefore == 0) {
                    modal = true
                }
            }
        }

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
                if (modal) {
                    startActivity(Intent(this, InfoRequiredModalActivity::class.java))
                }
                finish()
            } catch (e: Exception) {
                Intent(Intent.ACTION_VIEW).apply {
                    data = DEEPLINK_MAIN.toUri()
                }.let {
                    startActivity(it)
                }
                if (modal) {
                    startActivity(Intent(this, InfoRequiredModalActivity::class.java))
                }
                finish()
            }
        } else {
            if (handleTimetableIntent()) {
                Intent(Intent.ACTION_VIEW).apply {
                    data = DEEPLINK_TIMETABLE.toUri()
                    putExtra("modal", modal)
                }.let {
                    startActivity(it)
                }
                if (modal) {
                    startActivity(Intent(this, InfoRequiredModalActivity::class.java))
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
                        putExtra("modal", modal)
                    }
                )
                if (modal) {
                    startActivity(Intent(this, InfoRequiredModalActivity::class.java))
                }
                finish()
                return
            }
            Intent(Intent.ACTION_VIEW).apply {
                data = DEEPLINK_MAIN.toUri()
                putExtra("modal", modal)
            }.let {
                startActivity(it)
            }
            if (modal) {
                startActivity(Intent(this, InfoRequiredModalActivity::class.java))
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
