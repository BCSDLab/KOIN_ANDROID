package `in`.koreatech.koin.core.designsystem.util

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge

/**
 * Enables the edge-to-edge display for this [ComponentActivity] with light status bar.
 */
fun ComponentActivity.enableEdgeToEdgeWithLightStatusBar() = enableEdgeToEdge(
    statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
)

/**
 * Enables the edge-to-edge display for this [ComponentActivity] with blue status bar.
 */
fun ComponentActivity.enableEdgeToEdgeWithBlueStatusBar() = enableEdgeToEdge(
    statusBarStyle = SystemBarStyle.dark(Color.parseColor("#FF175C8E"))
)
