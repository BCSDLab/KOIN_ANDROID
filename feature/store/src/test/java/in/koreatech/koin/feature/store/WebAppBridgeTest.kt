package `in`.koreatech.koin.feature.store

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import `in`.koreatech.koin.core.webapp.KoinWebAppInterface
import `in`.koreatech.koin.core.webapp.KoinWebAppWebChromeClient
import `in`.koreatech.koin.core.webapp.KoinWebAppWebViewClient

class WebAppBridgeTest {
    val TEST_STORE_ID = 845

    val webAppBridgeHtml = """
        <html>
          <body>
            <button style="padding: 20px 40px;" onclick="Android.goToShopDetail(${TEST_STORE_ID})">
              Call goToShopDetail(${TEST_STORE_ID})
            </button>
          </body>
        </html>
    """.trimIndent()

    internal class WebAppBridgeTestInterface(val testStoreId: Int) : KoinWebAppInterface() {
        @JavascriptInterface
        fun goToShopDetail(shopId: Int) {
            Log.e("BridgeTest", "result = ${shopId == testStoreId}, StoreId = ${shopId}, TestStoreId = ${testStoreId}")
        }
    }

    @Composable
    fun WebAppBridgeTestWebView(
        context: Context = LocalContext.current
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { factoryContext ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = KoinWebAppWebViewClient()
                    webChromeClient = KoinWebAppWebChromeClient()
                    addJavascriptInterface(WebAppBridgeTestInterface(TEST_STORE_ID), "Android")
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
            },
            update = { webView ->
                webView.loadDataWithBaseURL(
                    "",
                    webAppBridgeHtml,
                    "text/html",
                    "utf-8",
                    null
                )
            }
        )
    }
}
