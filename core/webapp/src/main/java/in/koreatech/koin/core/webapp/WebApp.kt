package `in`.koreatech.koin.core.webapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * WebApp composable
 *
 * @param url Web app url
 * @param modifier Modifier
 * @param exposedInterfaceName The name used to expose the object in JavaScript
 * @param koinWebAppInterface An instance of [KoinWebAppInterface]
 * @param onProgressChanged Callback invoked when the progress changes
 */
@Composable
fun WebApp(
    url: String,
    modifier: Modifier = Modifier,
    exposedInterfaceName: String = "Android",
    windowInsets: WindowInsets = WindowInsets.safeDrawing,
    koinWebAppInterface: KoinWebAppInterface = KoinWebAppInterface(),
    onNavigateBack: () -> Unit = {},
    onProgressChanged: (Int) -> Unit = {}
) {
    var webView: WebView? by remember { mutableStateOf(null) }

    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(windowInsets),
        factory = { factoryContext ->
            createWebView(
                context = factoryContext,
                exposedInterfaceName = exposedInterfaceName,
                koinWebAppInterface = koinWebAppInterface,
                onProgressChanged = onProgressChanged
            ).apply {
                webView = this
            }
        },
        update = {
            it.loadUrl(url)
        }
    )

    BackHandler {
        if (webView?.canGoBack() == true) {
            webView!!.goBack()
        } else {
            onNavigateBack()
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun createWebView(
    context: Context,
    exposedInterfaceName: String,
    koinWebAppInterface: KoinWebAppInterface,
    onProgressChanged: (Int) -> Unit = {}
) = WebView(context).apply {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return false
        }
    }
    webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            onProgressChanged(newProgress)
            super.onProgressChanged(view, newProgress)
        }
    }
    addJavascriptInterface(koinWebAppInterface, exposedInterfaceName)
    overScrollMode = View.OVER_SCROLL_NEVER
    settings.apply {
        builtInZoomControls = false
        domStorageEnabled = true
        javaScriptEnabled = true
        loadWithOverviewMode = true
        blockNetworkLoads = false
        setSupportZoom(false)
    }
}
