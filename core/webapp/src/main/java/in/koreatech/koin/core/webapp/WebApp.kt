package `in`.koreatech.koin.core.webapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import java.net.URL

/**
 * WebApp composable
 *
 * @param url Web app url
 * @param modifier Modifier
 * @param exposedInterfaceName The name used to expose the object in JavaScript
 * @param windowInsets WindowInsets
 * @param cookies List of Cookie. First one is key and second one is value
 * @param koinWebAppInterface An instance of [KoinWebAppInterface]
 * @param koinWebAppWebViewClient An instance of [KoinWebAppWebViewClient]
 * @param koinWebChromeClient An instance of [KoinWebAppWebChromeClient]
 * @param backHandler Back handler of WebApp
 */
@Composable
fun WebApp(
    url: String,
    modifier: Modifier = Modifier,
    exposedInterfaceName: String = "Android",
    windowInsets: WindowInsets = WindowInsets.safeDrawing,
    cookies: List<Pair<String, String>> = emptyList(),
    koinWebAppInterface: KoinWebAppInterface = KoinWebAppInterface(),
    koinWebAppWebViewClient: KoinWebAppWebViewClient = KoinWebAppWebViewClient(),
    koinWebChromeClient: KoinWebAppWebChromeClient = KoinWebAppWebChromeClient(),
    backHandler: @Composable (view: WebView?) -> Unit = {}
) {
    var webView: WebView? by remember { mutableStateOf(null) }
    val cookieManager = CookieManager.getInstance()
    val bundle = rememberSaveable { bundleOf() }

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        webView?.saveState(bundle)
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        webView?.restoreState(bundle)
    }

    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(windowInsets)
            .consumeWindowInsets(windowInsets),
        factory = { factoryContext ->
            createWebView(
                context = factoryContext,
                exposedInterfaceName = exposedInterfaceName,
                koinWebAppInterface = koinWebAppInterface,
                koinWebAppWebViewClient = koinWebAppWebViewClient,
                koinWebChromeClient = koinWebChromeClient
            ).apply {
                webView = this
            }
        },
        update = {
            val baseUrl = URL(url).let { url ->
                "${url.protocol}://${url.host}"
            }

            cookieManager.apply {
                removeSessionCookies { }
                removeAllCookies { }
                acceptCookie()
                acceptThirdPartyCookies(it)
                cookies.forEach { cookie ->
                    setCookie(baseUrl, "${cookie.first}=${cookie.second}")
                }
            }
            if (bundle.isEmpty) {
                it.loadUrl(url)
            }
        },
        onRelease = {
            webView = null
        }
    )

    backHandler(webView)
}

@SuppressLint("SetJavaScriptEnabled")
private fun createWebView(
    context: Context,
    exposedInterfaceName: String,
    koinWebAppInterface: KoinWebAppInterface,
    koinWebAppWebViewClient: KoinWebAppWebViewClient = KoinWebAppWebViewClient(),
    koinWebChromeClient: KoinWebAppWebChromeClient = KoinWebAppWebChromeClient()
) = WebView(context).apply {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    webViewClient = koinWebAppWebViewClient
    webChromeClient = koinWebChromeClient
    addJavascriptInterface(koinWebAppInterface, exposedInterfaceName)
    overScrollMode = View.OVER_SCROLL_NEVER
    settings.apply {
        builtInZoomControls = false
        domStorageEnabled = true
        javaScriptEnabled = true
        loadWithOverviewMode = true
        blockNetworkLoads = false
        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        setSupportZoom(false)
    }
}
