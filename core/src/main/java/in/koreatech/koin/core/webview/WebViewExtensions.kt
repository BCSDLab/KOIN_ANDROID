package `in`.koreatech.koin.core.webview

import android.app.Activity
import android.content.Context
import android.webkit.JavascriptInterface
import android.webkit.WebView
import `in`.koreatech.koin.core.R

fun WebView.loadKoreatechHtml(context: Context, html: String) {
    this.loadDataWithBaseURL(
        context.getString(R.string.koreatech_url),
        html.addImageClickScript().addImageWidthStyle(),
        "text/html",
        "utf-8",
        null
    )
}

fun WebView.setOnImageClickListener(activity: Activity, onClick: (String) -> Unit) {
    settings.javaScriptEnabled = true
    addJavascriptInterface(
        ImageHandler(activity, onClick),
        ImageHandler::class.java.simpleName
    )
}

// 이미지 클릭 시 ImageHandler의 onImageClick 함수 호출
private fun String.addImageClickScript(): String {
    return "<script type=\"text/javascript\">\n" +
            "        function setupImageClickListener() {\n" +
            "            var images = document.getElementsByTagName('img');\n" +
            "            for (var i = 0; i < images.length; i++) {\n" +
            "                images[i].onclick = function() {\n" +
            "                    ${ImageHandler::class.java.simpleName}.${ImageHandler::onImageClick.name}(this.src);\n" +
            "                };\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        window.onload = setupImageClickListener;\n" +
            "    </script>" + this
}

// 이미지가 화면 크기를 넘어가지 않도록
private fun String.addImageWidthStyle(): String {
    return "<style>\n" +
            "        img {\n" +
            "            max-width: 100%;\n" +
            "        }\n" +
            "    </style>" + this
}

private class ImageHandler(
    private val activity: Activity,
    private val onClick: (String) -> Unit
) {

    @JavascriptInterface
    fun onImageClick(imageUrl: String) {
        activity.runOnUiThread {
            onClick(imageUrl)
        }
    }
}