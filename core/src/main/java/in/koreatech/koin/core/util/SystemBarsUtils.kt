package `in`.koreatech.koin.core.util

import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import androidx.annotation.ColorRes
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class SystemBarsUtils(
    private val context: Context
) {
    val navigationBarHeight =
        context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            .let { context.resources.getDimensionPixelSize(it) }

    val statusBarHeight =
        context.resources.getIdentifier("status_bar_height", "dimen", "android")
            .let { context.resources.getDimensionPixelSize(it) }

    /** Status Bar (상단 바) 색상 */
    fun setStatusBarColor(
        window: Window,
        @ColorRes color: Int
    ) {
        window.statusBarColor = context.getColor(color)
    }

    /** Navigation Bar (하단 바) 색상 */
    fun setNavigationBarColor(
        window: Window,
        @ColorRes color: Int
    ) {
        window.navigationBarColor = context.getColor(color)
    }

    /**
     * 시스템 바 아이콘 색상
     * @param isLightStatusBar (true : 검정 / false : 흰색)
     * @param isLightNavigationBar (true : 검정 / false : 흰색)
     */
    fun setAppearanceSystemBars(
        window: Window,
        isLightStatusBar: Boolean = true,
        isLightNavigationBar: Boolean = true
    ) {
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.isAppearanceLightStatusBars = isLightStatusBar
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                controller.isAppearanceLightNavigationBars = isLightNavigationBar
            } else {
                setAppearanceNavigationBarSdk26(window, isLightNavigationBar)
            }
        }
    }

    private fun setAppearanceNavigationBarSdk26(window: Window, isLightNavigationBar: Boolean) {
        if (isLightNavigationBar) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            window.decorView.systemUiVisibility = 0
        }
    }

    /**
     * 전체 화면(몰입형) 모드
     */
    fun setImmersiveMode(
        window: Window
    ) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            WindowInsetsControllerCompat(window, window.decorView).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            val windowInsetsController =
                WindowCompat.getInsetsController(window, window.decorView)

            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}