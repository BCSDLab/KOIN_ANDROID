package `in`.koreatech.koin.core.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.*
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.core.databinding.ActivityWebviewBinding
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.core.util.dataBinding

class WebViewActivity : ActivityBase(R.layout.activity_webview) {

    private val binding by dataBinding<ActivityWebviewBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val title = intent.getStringExtra("title")
        val url = intent.getStringExtra("url")
        init(title, url)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_webview, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> onBackPressed()
            R.id.menu_webview_finish -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun init(title: String?, url: String?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle(title)

        binding.webView.apply {
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    showProgressDialog(R.string.loading)
                }

                override fun onPageFinished(view: WebView, url: String) {
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
            }
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            loadUrl(url ?: "https://bcsdlab.com/")
        }
    }
}