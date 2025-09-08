package `in`.koreatech.koin.core.activity

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup.MarginLayoutParams
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
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
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                ContextCompat.getColor(this, R.color.primary_500)
            )
        )
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

        ViewCompat.setOnApplyWindowInsetsListener(binding.webView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<MarginLayoutParams> {
                topMargin = systemBars.top
                bottomMargin = systemBars.bottom
            }
            WindowInsetsCompat.CONSUMED
        }

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
        val url = request?.url.toString()

        if (url.startsWith("intent://")) {
            val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                intent.getPackage()?.let { packageName ->
                    try {
                        val marketIntent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("market://details?id=$packageName")
                        }
                        context.startActivity(marketIntent)
                    } catch (e: ActivityNotFoundException) {
                        intent.getStringExtra("browser_fallback_url")?.let { fallbackUrl ->
                            view?.loadUrl(fallbackUrl)
                        }
                    }
                }
            }
            return true
        }

        if (openInNewTab) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("url", url)
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
