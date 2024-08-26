package `in`.koreatech.koin.ui.article

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.net.toUri
import `in`.koreatech.koin.domain.model.article.html.CssAttribute
import `in`.koreatech.koin.domain.model.article.html.HtmlAttribute
import `in`.koreatech.koin.domain.model.article.html.HtmlTag
import `in`.koreatech.koin.ui.article.state.HtmlElement

class HtmlView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attributeSet, defStyleAttr) {

    private var html: HtmlElement? = null
    private var lastAddedView: View = this  // 가장 마지막으로 추가된 View. 이미 추가된 View의 재활용을 위함

    private var onPreDrawListener: OnPreDrawListener? = null
    private var onPostDrawListener: OnPostDrawListener? = null

    init {
        orientation = VERTICAL
        setWillNotDraw(false)
    }

    fun setHtml(html: HtmlElement) {
        onPreDrawListener?.onPreDraw()
        clearView()
        this.html = html
        addHtmlView(html)
        onPostDrawListener?.onPostDraw()
    }

    private fun clearView() {
        removeAllViews()
        html = null
        lastAddedView = this
    }

    private fun addHtmlView(html: HtmlElement) {
        html.children.forEach { self ->
            when (self.tag) {
                HtmlTag.P, HtmlTag.DIV, HtmlTag.SPAN, HtmlTag.A, HtmlTag.BR -> {
                    if (lastAddedView is TextView) {    // 직전 View가 TextView일 경우 재활용
                        val lineBreak = when(self.tag) {
                            HtmlTag.P, HtmlTag.DIV, HtmlTag.BR -> "\n"
                            else -> ""
                        }
                        val originalText = SpannableStringBuilder((lastAddedView as TextView).text)
                        val newTextBuilder = SpannableStringBuilder(lineBreak + self.content)
                        val newSpanned = newTextBuilder.getStyledText(0, newTextBuilder.length, self.styles)

                        (lastAddedView as TextView).text = originalText.append(newSpanned)
                    } else {
                        val textView = TextView(context).apply {
                            setTextIsSelectable(true)
                            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                        }
                        val newTextBuilder = SpannableStringBuilder(self.content)
                        textView.text = newTextBuilder.getStyledText(0, newTextBuilder.length, self.styles)
                        addView(textView)

                        lastAddedView = textView
                    }
                    addHtmlView(self)
                }
                HtmlTag.HR -> {
                    val hr = View(context).apply {
                        //layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 1)
                        //setBackgroundColor(Color.BLACK)
                    }
                    addView(hr)
                    lastAddedView = hr
                }
                HtmlTag.IMG -> {
                    val imageView = ImageView(context).apply {
                        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                        setImageURI(self.attributes[HtmlAttribute.SRC]?.toUri())
                    }
                    addView(imageView)
                    lastAddedView = imageView
                }
                else -> {}
            }
        }
    }

    private fun interface OnPreDrawListener {
        fun onPreDraw()
    }

    private fun interface OnPostDrawListener {
        fun onPostDraw()
    }

    fun setOnPreDrawListener(callback: () -> Unit) {
        onPreDrawListener = OnPreDrawListener { callback() }
    }

    fun setOnPostDrawListener(callback: () -> Unit) {
        onPostDrawListener = OnPostDrawListener { callback() }
    }
}

fun SpannableStringBuilder.getStyledText(
    start: Int,
    end: Int,
    styles: Map<CssAttribute, String>
): SpannableStringBuilder {
    if (start == end) return this

    setFontStyle(start, end, styles)
    setTextDecoration(start, end, styles)
    setTextColor(start, end, styles)
    setBackgroundColor(start, end, styles)
    setFontSize(start, end, styles)

    return this
}

@SuppressLint("ViewConstructor")
private class HtmlTableView(
    context: Context,
    html: HtmlElement
) : AppCompatTextView(context) {

}
