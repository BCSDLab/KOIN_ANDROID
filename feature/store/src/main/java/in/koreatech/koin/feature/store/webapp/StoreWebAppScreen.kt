package `in`.koreatech.koin.feature.store.webapp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.webkit.JavascriptInterface
import android.webkit.URLUtil
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.webapp.KoinWebAppInterface
import `in`.koreatech.koin.core.webapp.KoinWebAppWebViewClient
import `in`.koreatech.koin.core.webapp.Tokens
import `in`.koreatech.koin.core.webapp.WebApp
import `in`.koreatech.koin.feature.store.component.KoinStoreProgressIndicator
import java.net.URISyntaxException

@Composable
fun StoreWebAppScreen(
    url: String,
    viewModel: StoreWebAppViewModel = hiltViewModel(),
    finish: () -> Unit = {},
    navigateToMain: () -> Unit = {},
    navigateToCart: () -> Unit = {}
) {
    val context = LocalContext.current
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

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
        url = url,
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
                    dispatcher?.onBackPressed()
                }
            },
            finish = finish
        ),
        cookies = listOf(
            "AUTH_TOKEN_KEY" to authToken.token
        ),
        backHandler = { webView ->
            BackHandler {
                when (webView?.url?.toUri()?.path?.split("/")?.get(1)) {
                    "payment" -> {
                        navigateToCart()
                    }
                    "result", "orderCancel" -> {
                        navigateToMain()
                    }
                    else -> {
                        if (webView?.canGoBack() == true) {
                            webView.goBack()
                        } else {
                            navigateToMain()
                        }
                    }
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
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        val url = request.url.toString()
        return handleUrl(view.context, url)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)

        view?.evaluateJavascript("localStorage.removeItem('refresh-token-storage');", null)
        view?.evaluateJavascript("localStorage.setItem('refresh-token-storage','{\"state\":{\"refreshToken\":\"${tokens.refreshToken}\",\"userType\":\"${tokens.userType}\"},\"version\":0}');", null)
    }
}

/*
 * from https://docs.tosspayments.com/guides/v2/webview
 */
private fun handleUrl(context: Context?, url: String): Boolean {
    if (!URLUtil.isNetworkUrl(url) && !URLUtil.isJavaScriptUrl(url)) {
        val uri = try {
            url.toUri()
        } catch (_: Exception) {
            return false
        }

        return when (uri.scheme) {
            "intent" -> {
                startSchemeIntent(context, url)
            }
            else -> {
                return try {
                    context?.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    true
                } catch (_: Exception) {
                    false
                }
            }
        }
    } else {
        return false
    }
}

private fun startSchemeIntent(context: Context?, url: String): Boolean {
    val schemeIntent: Intent = try {
        Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
    } catch (_: URISyntaxException) {
        return false
    }
    try {
        context?.startActivity(schemeIntent)
        return true
    } catch (_: ActivityNotFoundException) {
        val packageName = schemeIntent.getPackage()

        if (!packageName.isNullOrBlank()) {
            context?.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "market://details?id=$packageName".toUri()
                )
            )
            return true
        }
    }
    return false
}
