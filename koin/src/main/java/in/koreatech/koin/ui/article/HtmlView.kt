package `in`.koreatech.koin.ui.article

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.toSpannable
import androidx.core.view.children
import `in`.koreatech.koin.domain.constant.BOLD
import `in`.koreatech.koin.domain.constant.BOLD_ITALIC
import `in`.koreatech.koin.domain.constant.ITALIC
import `in`.koreatech.koin.domain.constant.LINE_THROUGH
import `in`.koreatech.koin.domain.constant.UNDERLINE
import `in`.koreatech.koin.domain.model.article.html.CssAttribute
import `in`.koreatech.koin.domain.model.article.html.HtmlTag
import `in`.koreatech.koin.domain.model.article.html.ViewType
import `in`.koreatech.koin.ui.article.state.HtmlElement

class HtmlView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attributeSet, defStyleAttr) {

    private var html: HtmlElement? = null

    init {
        orientation = VERTICAL
        setWillNotDraw(false)
    }

    fun setHtml(html: HtmlElement) {
        this.html = html
        addHtmlView(html)
    }

    private fun addHtmlView(html: HtmlElement, parentView: View? = null) {
        html.children.forEach { child ->
            when (child.tag) {
                HtmlTag.P, HtmlTag.DIV -> {
                    if (parentView is TextView) {    // 부모 View가 TextView일 경우
                        val originalText = parentView.text
                        val newTextBuilder = SpannableStringBuilder("\n" + child.content)
                        val newSpanned = newTextBuilder.getStyledText(0, newTextBuilder.length, child.styles)

                        parentView.text = TextUtils.concat(originalText, newSpanned)
                        addHtmlView(child, parentView)
                    } else {
                        val textView = TextView(context).apply {
                            setTextIsSelectable(true)
                            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                        }
                        val newTextBuilder = SpannableStringBuilder(child.content)
                        textView.text = newTextBuilder.getStyledText(0, newTextBuilder.length, child.styles)
                        addView(textView)

                        addHtmlView(child, textView)
                    }
                }
                HtmlTag.SPAN -> {
                    if (parentView is TextView) {    // 부모 View가 TextView일 경우
                        val originalText = parentView.text
                        val newTextBuilder = SpannableStringBuilder(child.content)
                        val newSpanned = newTextBuilder.getStyledText(0, newTextBuilder.length, child.styles)

                        parentView.text = TextUtils.concat(originalText, newSpanned)

                        addHtmlView(child, parentView)
                    } else {
                        val textView = TextView(context).apply {
                            setTextIsSelectable(true)
                            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                        }
                        val newTextBuilder = SpannableStringBuilder(child.content)
                        textView.text = newTextBuilder.getStyledText(0, newTextBuilder.length, child.styles)
                        addView(textView)

                        addHtmlView(child, textView)
                    }
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

    return this
}

@SuppressLint("ViewConstructor")
private class HtmlTableView(
    context: Context,
    html: HtmlElement
) : AppCompatTextView(context) {

}