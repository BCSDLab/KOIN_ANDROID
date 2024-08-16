package `in`.koreatech.koin.ui.article

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import `in`.koreatech.koin.domain.constant.BOLD
import `in`.koreatech.koin.domain.constant.BOLD_ITALIC
import `in`.koreatech.koin.domain.constant.ITALIC
import `in`.koreatech.koin.domain.constant.LINE_THROUGH
import `in`.koreatech.koin.domain.constant.UNDERLINE
import `in`.koreatech.koin.domain.model.article.html.CssAttribute
import `in`.koreatech.koin.domain.model.article.html.HtmlTag
import `in`.koreatech.koin.ui.article.state.HtmlElement

class HtmlView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attributeSet, defStyleAttr) {

    private var html: HtmlElement? = null
    private var lastAddedView: View = this  // 가장 마지막으로 추가된 View. 이미 추가된 View의 재활용을 위함

    init {
        orientation = VERTICAL
        setWillNotDraw(false)
    }

    fun setHtml(html: HtmlElement) {
        this.html = html
        addHtmlView(html)
    }

    private fun addHtmlView(html: HtmlElement) {
        html.children.forEach { child ->
            when (child.tag) {
                HtmlTag.P, HtmlTag.DIV, HtmlTag.SPAN, HtmlTag.A, HtmlTag.BR -> {
                    if (lastAddedView is TextView) {    // 직전 View가 TextView일 경우 재활용
                        val lineBreak = when(child.tag) {
                            HtmlTag.P, HtmlTag.DIV, HtmlTag.BR -> "\n"
                            else -> ""
                        }
                        val originalText = (lastAddedView as TextView).text
                        val newTextBuilder = SpannableStringBuilder(lineBreak + child.content)
                        val newSpanned = newTextBuilder.getStyledText(0, newTextBuilder.length, child.styles)

                        (lastAddedView as TextView).text = TextUtils.concat(originalText, newSpanned)
                    } else {
                        val textView = TextView(context).apply {
                            setTextIsSelectable(true)
                            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                        }
                        val newTextBuilder = SpannableStringBuilder(child.content)
                        textView.text = newTextBuilder.getStyledText(0, newTextBuilder.length, child.styles)
                        addView(textView)

                        lastAddedView = textView
                    }
                    addHtmlView(child)
                }
                else -> {}
            }
        }
    }
}

fun SpannableStringBuilder.getStyledText(
    start: Int,
    end: Int,
    styles: Map<CssAttribute, String>
): SpannableStringBuilder {
    if (start == end) return this

    styles[CssAttribute.FONT_STYLE]?.let {
        setSpan(
            when(it) {
                BOLD -> StyleSpan(Typeface.BOLD)
                ITALIC -> StyleSpan(Typeface.ITALIC)
                BOLD_ITALIC -> StyleSpan(Typeface.BOLD_ITALIC)
                else -> StyleSpan(Typeface.NORMAL)
            },
            0,
            end,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    styles[CssAttribute.TEXT_DECORATION]?.let {
        setSpan(
            when(it) {
                UNDERLINE -> UnderlineSpan()
                LINE_THROUGH -> StrikethroughSpan()
                else -> null
            },
            start,
            end,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    styles[CssAttribute.COLOR]?.let {
        setSpan(
            ForegroundColorSpan(it.parseColor(Color.BLACK)),
            start,
            end,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    styles[CssAttribute.BACKGROUND_COLOR]?.let {
        setSpan(
            BackgroundColorSpan(it.parseColor(Color.WHITE)),
            start,
            end,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    styles[CssAttribute.BACKGROUND]?.let {
        setSpan(
            BackgroundColorSpan(it.parseColor(Color.WHITE)),
            start,
            end,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    return this
}

@SuppressLint("ViewConstructor")
private class HtmlTableView(
    context: Context,
    html: HtmlElement
) : AppCompatTextView(context) {

}

private fun String.parseColor(default: Int): Int {
    return try {
        Color.parseColor(this)
    } catch (e: IllegalArgumentException) {
        parseRgbColor(this, default)
    }
}

private fun parseRgbColor(rgbString: String, default: Int): Int {
    val regex = """rgb\((\d+), (\d+), (\d+)\)""".toRegex()
    val matchResult = regex.matchEntire(rgbString)

    return if (matchResult != null) {
        val (r, g, b) = matchResult.destructured
        Color.rgb(r.toInt(), g.toInt(), b.toInt())
    } else {
        default
    }
}