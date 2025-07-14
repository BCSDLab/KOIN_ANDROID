package `in`.koreatech.koin.core.webapp

import android.webkit.JavascriptInterface

open class KoinWebAppInterface {
    @JavascriptInterface
    open fun getUserTokens(): String {
        return "" // placeholder
    }
}
