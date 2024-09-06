package `in`.koreatech.koin.ui.article

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.dialog.ImageZoomableDialog
import `in`.koreatech.koin.domain.model.article.html.CssAttribute
import `in`.koreatech.koin.domain.model.article.html.HtmlAttribute
import `in`.koreatech.koin.domain.model.article.html.HtmlTag
import `in`.koreatech.koin.ui.article.HtmlView.OnPostDrawListener
import `in`.koreatech.koin.ui.article.HtmlView.OnPreDrawListener
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
        html.children.forEachIndexed { i, self ->
            when (self.tag) {
                HtmlTag.P, HtmlTag.DIV, HtmlTag.SPAN, HtmlTag.A, HtmlTag.BR, HtmlTag.LI, HtmlTag.OL, HtmlTag.UL -> {
                    if (lastAddedView is TextView       // 직전 View가 TextView이고
                        && lastAddedView.textAlignment == self.styles[CssAttribute.TEXT_ALIGN].parseTextAlignment()) {    // Text-align이 같으면 TextView 재사용
                        val frontLineBreak = when(self.tag) {
                            HtmlTag.P, HtmlTag.DIV, HtmlTag.BR, HtmlTag.LI, HtmlTag.OL, HtmlTag.UL -> if (self.children.isEmpty()) "" else "\n"
                            else -> ""
                        }

                        val listMarker = createListMarker(self.tag, html.tag, i)

                        val originalText = SpannableStringBuilder((lastAddedView as TextView).text)
                        val newTextBuilder = SpannableStringBuilder(frontLineBreak + listMarker + self.content)
                        val newSpanned = newTextBuilder.getStyledText(0, newTextBuilder.length, self.styles)

                        (lastAddedView as TextView).text = originalText.append(newSpanned)
                    } else {
                        TextView(context).apply {
                            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                            addView(this)
                            setTextIsSelectable(true)

                            val listMarker = createListMarker(self.tag, html.tag, i)

                            val newTextBuilder = SpannableStringBuilder(listMarker + self.content)
                            text = newTextBuilder.getStyledText(0, newTextBuilder.length, self.styles)

                            this.textAlignment = self.styles[CssAttribute.TEXT_ALIGN].parseTextAlignment()
                        }.also {
                            lastAddedView = it
                        }
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
                    lastAddedView = drawImage(self)
                }
                else -> {}
            }
        }
    }

    private fun createListMarker(selfTag: HtmlTag, parentTag: HtmlTag, index: Int): String {
        return when(selfTag) {
            HtmlTag.LI -> {
                when(parentTag) {
                    HtmlTag.UL -> "• "
                    HtmlTag.OL -> "${index + 1}. "
                    else -> "• "
                }
            }
            else -> ""
        }
    }

    private fun drawImage(self: HtmlElement): ImageView {
        val imageView = ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
        addView(imageView)
        Glide.with(context).load(self.attributes[HtmlAttribute.SRC]).error(
            Glide.with(context).load(context.getString(R.string.koreatech_url) + self.attributes[HtmlAttribute.SRC])
        ).addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                val dialog = ImageZoomableDialog(context, model as String)
                dialog.initialScale = .8f
                imageView.setOnClickListener {
                    dialog.show()
                }
                return false
            }
        }).into(imageView)
        return imageView
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
