package `in`.koreatech.koin.feature.club.component

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun KoinClubIntroduction(
    data: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val width by remember { mutableIntStateOf(getScreenWidth(context)) }

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                isVerticalScrollBarEnabled = false

                settings.apply {
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    cacheMode = WebSettings.LOAD_NO_CACHE
                    setSupportZoom(false)
                    builtInZoomControls = false
                }
            }
        },
        modifier = modifier,
        update = {
            it.loadData(fullHtml(width, data), "text/html", "UTF-8")
        }
    )
}

private fun getScreenWidth(context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = wm.currentWindowMetrics
        val insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.width() - insets.left - insets.right
    } else {
        val displayMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.widthPixels
    }
}

private fun fullHtml(width: Int, html: String): String {
    return """
        <html>
        <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        </head>
        <body>
        <style>
            img {display: inline;height: auto;max-width: 100%;width: $width;}
        </style>
        $html
        </body>
        </html>
    """.trimIndent()
}