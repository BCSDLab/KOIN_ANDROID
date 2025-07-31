package `in`.koreatech.koin.feature.store.view.payment

import android.app.Activity
import android.content.Intent
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.webapp.KoinWebAppInterface
import `in`.koreatech.koin.core.webapp.KoinWebAppWebViewClient
import `in`.koreatech.koin.core.webapp.Tokens
import `in`.koreatech.koin.core.webapp.WebApp
import `in`.koreatech.koin.feature.store.BuildConfig
import `in`.koreatech.koin.feature.store.component.KoinStoreProgressIndicator

@Composable
fun StorePaymentScreen(
    viewModel: StorePaymentViewModel = hiltViewModel(),
    finish: () -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val authToken by viewModel.authToken.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            KoinStoreProgressIndicator(
                modifier = Modifier.size(150.dp)
            )
        }

        return
    }

    WebApp(
        modifier = Modifier.fillMaxSize(),
        url = "${BuildConfig.ORDER_BASE_URL}/payment?orderType=DELIVERY", // TODO
        koinWebAppWebViewClient = StorePaymentWebViewClient(
            Tokens(
                refreshToken = authToken.refreshToken,
                accessToken = authToken.token,
                userType = authToken.userType ?: ""
            )
        ),
        koinWebAppInterface = StorePaymentScreenInterface(
            navigateBack = {
                (context as Activity).runOnUiThread {
                    navigateBack()
                }
            },
            finish = finish
        ),
        cookies = listOf(
            "AUTH_TOKEN_KEY" to authToken.token
        ),
        backHandler = { webView ->
            BackHandler {
                if (webView?.canGoBack() == true) {
                    webView.goBack()
                } else {
                    finish()
                }
            }
        }
    )
}

internal class StorePaymentScreenInterface(
    private val navigateBack: () -> Unit,
    private val finish: () -> Unit
) : KoinWebAppInterface() {
    @JavascriptInterface
    fun navigateBack() = navigateBack.invoke()

    @JavascriptInterface
    fun finish() = finish.invoke()
}

internal class StorePaymentWebViewClient(private val tokens: Tokens) : KoinWebAppWebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (request?.url?.scheme == "intent") {
            try {
                val intent = Intent.parseUri(request.url.toString(), Intent.URI_INTENT_SCHEME)
                view?.context?.startActivity(intent)
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        view?.evaluateJavascript("localStorage.removeItem('refresh-token-storage');", null)
        view?.evaluateJavascript("localStorage.setItem('refresh-token-storage','{\"state\":{\"refreshToken\":\"${tokens.refreshToken}\",\"userType\":\"${tokens.userType}\"},\"version\":0}');", null)
    }
}
