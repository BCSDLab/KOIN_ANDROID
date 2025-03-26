package `in`.koreatech.koin.core.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.core.databinding.ActivityWebviewBinding
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.dataBinding

class WebViewActivity : ActivityBase(R.layout.activity_webview) {
    private val binding by dataBinding<ActivityWebviewBinding>()
    override val screenTitle: String = "웹뷰"

    override val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
                } else {
                    finish()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title = intent.getStringExtra("title") ?: ""
        val url = intent.getStringExtra("url")
        init(title, url)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_webview, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> onBackPressedDispatcher.onBackPressed()
            R.id.menu_webview_finish -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun init(
        title: String?,
        url: String?
    ) {
        setTitle(title)

        binding.webView.apply {
            webChromeClient =
                KoinWebChromeClient(
                    context = this@WebViewActivity,
                    showProgressDialog = {
                        showProgressDialog(R.string.loading)
                    },
                    hideProgressDialog = {
                        hideProgressDialog()
                    }
                )
            settings.javaScriptEnabled = true
            settings.setSupportMultipleWindows(true)
            webViewClient =
                KoinWebViewClient(
                    context = this@WebViewActivity,
                    showProgressDialog = {
                        showProgressDialog(R.string.loading)
                    },
                    hideProgressDialog = {
                        hideProgressDialog()
                    }
                )
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            loadUrl(url ?: "https://bcsdlab.com/")
        }
    }
}

internal class KoinWebViewClient(
    private val context: Context,
    private val openInNewTab: Boolean = false,
    private val showProgressDialog: () -> Unit,
    private val hideProgressDialog: () -> Unit
) : WebViewClient() {
    override fun onPageStarted(
        view: WebView,
        url: String,
        favicon: Bitmap?
    ) {
        super.onPageStarted(view, url, favicon)
        showProgressDialog()
    }

    override fun onPageFinished(
        view: WebView,
        url: String
    ) {
        super.onPageFinished(view, url)
        hideProgressDialog()
    }

    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest,
        error: WebResourceError
    ) {
        super.onReceivedError(view, request, error)
        hideProgressDialog()
        ToastUtil.getInstance().makeShort(R.string.error_network)
    }

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        if (openInNewTab) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("url", request?.url.toString())
            context.startActivity(intent)
            return true
        }
        return super.shouldOverrideUrlLoading(view, request)
    }
}

internal class KoinWebChromeClient(
    private val context: Context,
    private val showProgressDialog: () -> Unit,
    private val hideProgressDialog: () -> Unit
) : WebChromeClient() {
    override fun onCreateWindow(
        view: WebView,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        val newWebView =
            WebView(view.context).apply {
                webChromeClient = this@KoinWebChromeClient
                webViewClient =
                    KoinWebViewClient(
                        context = context,
                        openInNewTab = true,
                        showProgressDialog = {
                            showProgressDialog()
                        },
                        hideProgressDialog = {
                            hideProgressDialog()
                        }
                    )
            }
        val transport = resultMsg?.obj as? WebView.WebViewTransport
        transport?.webView = newWebView
        resultMsg?.sendToTarget()
        return true
    }
}
