package `in`.koreatech.koin.ui.unibus

import android.content.Context
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.core.activity.KoinWebViewClient
import `in`.koreatech.koin.core.activity.WebViewActivity

class UnibusActivity : WebViewActivity() {
    override fun init(
        title: String?,
        url: String?
    ) {
        super.init(title, url)
        binding.webView.webViewClient = UnibusWebViewClient(
            context = this@UnibusActivity,
            showProgressDialog = {
                showProgressDialog(R.string.loading)
            },
            hideProgressDialog = {
                hideProgressDialog()
            }
        )
        binding.webView.addJavascriptInterface(UnibusJavascriptInterface(),"Android")
    }

    private inner class UnibusJavascriptInterface {
        @JavascriptInterface
        fun onQrcodeModalChanged(isActive: Boolean) {
            runOnUiThread {
                if (isFinishing || isDestroyed) return@runOnUiThread
                val layout = window.attributes
                layout.screenBrightness = if (isActive) {
                    WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
                } else {
                    WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
                }
                window.attributes = layout
            }
        }
    }
}

private class UnibusWebViewClient(
    context: Context,
    openInNewTab: Boolean = false,
    showProgressDialog: () -> Unit,
    hideProgressDialog: () -> Unit
) : KoinWebViewClient(context, openInNewTab, showProgressDialog, hideProgressDialog) {
    companion object {
        val QR_CHECK = """
(function waitForAngular(retry) {
  if (typeof angular === 'undefined') {
    if (retry > 0) setTimeout(function() { waitForAngular(retry - 1); }, 300);
    return;
  }
  
  const ${'$'}rootScope = angular.element(document.body).injector().get('${'$'}rootScope');

  ${'$'}rootScope.${'$'}watch(function() {
    return !!document.querySelector('div.modal[ui-if="qrcodeModal"]');
  }, function(newVal, oldVal) {
    if (newVal === oldVal) return;
    window.Android?.onQrcodeModalChanged?.(newVal);
  });
})(10);
        """.trimIndent()
    }

    override fun onPageFinished(
        view: WebView,
        url: String
    ) {
        super.onPageFinished(view, url)
        view.evaluateJavascript(QR_CHECK, null)
    }
}
