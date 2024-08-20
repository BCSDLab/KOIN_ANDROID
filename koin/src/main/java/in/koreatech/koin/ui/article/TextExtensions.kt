package `in`.koreatech.koin.ui.article

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import `in`.koreatech.koin.domain.constant.BOLD
import `in`.koreatech.koin.domain.constant.BOLD_ITALIC
import `in`.koreatech.koin.domain.constant.ITALIC
import `in`.koreatech.koin.domain.constant.LINE_THROUGH
import `in`.koreatech.koin.domain.constant.UNDERLINE
import `in`.koreatech.koin.domain.model.article.html.CssAttribute

fun SpannableStringBuilder.setFontStyle(
    start: Int,
    end: Int,
    styles: Map<CssAttribute, String>
) {
    styles[CssAttribute.FONT_STYLE]?.let {
        setSpan(
            when(it) {
                BOLD -> StyleSpan(Typeface.BOLD)
                ITALIC -> StyleSpan(Typeface.ITALIC)
                BOLD_ITALIC -> StyleSpan(Typeface.BOLD_ITALIC)
                else -> StyleSpan(Typeface.NORMAL)
            },
            start,
            end,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

fun SpannableStringBuilder.setTextDecoration(
    start: Int,
    end: Int,
    styles: Map<CssAttribute, String>
) {
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
}

fun SpannableStringBuilder.setTextColor(
    start: Int,
    end: Int,
    styles: Map<CssAttribute, String>
) {
    styles[CssAttribute.COLOR]?.let {
        setSpan(
            ForegroundColorSpan(it.parseColor(Color.BLACK)),
            start,
            end,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

fun SpannableStringBuilder.setBackgroundColor(
    start: Int,
    end: Int,
    styles: Map<CssAttribute, String>
) {
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
}

fun SpannableStringBuilder.setFontSize(
    start: Int,
    end: Int,
    styles: Map<CssAttribute, String>
) {
    if (styles[CssAttribute.FONT_SIZE]?.endsWith("px") == true || styles[CssAttribute.FONT_SIZE]?.endsWith("pt") == true) {
        styles[CssAttribute.FONT_SIZE]?.let {
            setSpan(
                AbsoluteSizeSpan(it.parseAbsoluteFontSize(), true),
                start,
                end,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    if (styles[CssAttribute.FONT_SIZE]?.endsWith("em") == true || styles[CssAttribute.FONT_SIZE]?.endsWith("%") == true) {
        styles[CssAttribute.FONT_SIZE]?.let {
            setSpan(
                RelativeSizeSpan(it.parseFontRatio() * 12),
                start,
                end,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    if (styles[CssAttribute.DEFAULT_FONT_SIZE]?.endsWith("rem") == true) {
        styles[CssAttribute.DEFAULT_FONT_SIZE]?.let {
            setSpan(
                RelativeSizeSpan(it.parseFontRatio() * 12),
                start,
                end,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}