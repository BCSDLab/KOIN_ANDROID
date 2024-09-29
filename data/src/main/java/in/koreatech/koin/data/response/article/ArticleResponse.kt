package `in`.koreatech.koin.data.response.article

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.constant.BOLD
import `in`.koreatech.koin.domain.constant.BOLD_ITALIC
import `in`.koreatech.koin.domain.constant.ITALIC
import `in`.koreatech.koin.domain.constant.LINE_THROUGH
import `in`.koreatech.koin.domain.constant.NORMAL
import `in`.koreatech.koin.domain.constant.UNDERLINE
import `in`.koreatech.koin.domain.model.article.Article
import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.model.article.html.CssAttribute
import `in`.koreatech.koin.domain.model.article.html.HtmlAttribute
import `in`.koreatech.koin.domain.model.article.html.HtmlModel
import `in`.koreatech.koin.domain.model.article.html.HtmlTag
import `in`.koreatech.koin.domain.model.article.html.toCssAttribute
import `in`.koreatech.koin.domain.model.article.html.toHtmlAttribute
import `in`.koreatech.koin.domain.model.article.html.toHtmlTag
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

data class ArticleResponse(
    @SerializedName("id") val id: Int?,
    @SerializedName("board_id") val boardId: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("author") val author: String?,
    @SerializedName("hit") val viewCount: Int?,
    @SerializedName("registered_at") val registeredAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("prev_id") val prevArticleId: Int?,
    @SerializedName("next_id") val nextArticleId: Int?,
    @SerializedName("attachments") val attachments: List<AttachmentResponse>?
) {
    fun toArticleHeader() = ArticleHeader(
        id = id ?: 0,
        boardId = boardId ?: 0,
        title = title ?: "",
        author = author ?: "",
        viewCount = viewCount ?: 0,
        registeredAt = registeredAt ?: "",
        updatedAt = updatedAt ?: "",
    )

    fun toArticle() = Article(
        header = toArticleHeader(),
        content = content ?: "",
        prevArticleId = prevArticleId,
        nextArticleId = nextArticleId,
        attachments = attachments?.map { it.toAttachment() } ?: listOf()
    )
}

fun Document.toHtmlModel(): HtmlModel {
    return this.body().toHtmlModel(mapOf(CssAttribute.DEFAULT_FONT_SIZE to "12px"))
}

fun Node.toHtmlModel(parentStyles: Map<CssAttribute, String>): HtmlModel {
    val selfAttributes = mutableMapOf<HtmlAttribute, String>()
    val selfChildren = mutableListOf<HtmlModel>()
    val selfStyles = mutableMapOf<CssAttribute, String>()
    var selfTag = HtmlTag.UNKNOWN

    this.attributes().forEach { attribute ->
        try {
            selfAttributes[attribute.key.toHtmlAttribute()] = attribute.value
        } catch(e: IllegalArgumentException) {
            // 지정되지 않은 속성 무시
        }
    }

    selfStyles.putAll(parentStyles)     // 부모 스타일 상속
    selfStyles.removeNonInheritableStyles() // 상속되지 않는 스타일 제거

    if (this is Element) {
        selfTag = this.tagName().toHtmlTag()
        selfTag = when (selfTag) {
            HtmlTag.B, HtmlTag.STRONG -> {
                selfStyles[CssAttribute.FONT_STYLE] = BOLD
                HtmlTag.SPAN
            }

            HtmlTag.I, HtmlTag.EM, HtmlTag.DFN -> {
                selfStyles[CssAttribute.FONT_STYLE] =
                    if (selfStyles[CssAttribute.FONT_STYLE] == BOLD) BOLD_ITALIC else ITALIC
                HtmlTag.SPAN
            }

            HtmlTag.U -> {
                selfStyles[CssAttribute.TEXT_DECORATION] = UNDERLINE
                HtmlTag.SPAN
            }

            HtmlTag.STRIKE, HtmlTag.DEL, HtmlTag.S -> {
                selfStyles[CssAttribute.TEXT_DECORATION] = LINE_THROUGH
                HtmlTag.SPAN
            }

            HtmlTag.H1 -> {
                selfStyles[CssAttribute.FONT_SIZE] = "2em"
                selfStyles[CssAttribute.FONT_STYLE] = BOLD
                HtmlTag.SPAN
            }

            HtmlTag.H2 -> {
                selfStyles[CssAttribute.FONT_SIZE] = "1.5em"
                selfStyles[CssAttribute.FONT_STYLE] = BOLD
                HtmlTag.SPAN
            }

            HtmlTag.H3 -> {
                selfStyles[CssAttribute.FONT_SIZE] = "1.17em"
                selfStyles[CssAttribute.FONT_STYLE] = BOLD
                HtmlTag.SPAN
            }

            HtmlTag.H4 -> {
                selfStyles[CssAttribute.FONT_SIZE] = "1em"
                selfStyles[CssAttribute.FONT_STYLE] = BOLD
                HtmlTag.SPAN
            }

            HtmlTag.H5 -> {
                selfStyles[CssAttribute.FONT_SIZE] = "0.83em"
                selfStyles[CssAttribute.FONT_STYLE] = BOLD
                HtmlTag.SPAN
            }

            HtmlTag.H6 -> {
                selfStyles[CssAttribute.FONT_SIZE] = "0.67em"
                selfStyles[CssAttribute.FONT_STYLE] = BOLD
                HtmlTag.SPAN
            }

            else -> {
                selfTag
            }
        }
    }

    if (selfAttributes.containsKey(HtmlAttribute.STYLE)) {
        val styleValue = selfAttributes[HtmlAttribute.STYLE]?.split(";")
        selfStyles.applySelfStyles(styleValue)
    }

    this.childNodes().forEach { child ->
        if (child is TextNode) {
            if (selfTag == HtmlTag.HTML && child.wholeText == "\n") return@forEach
            if (child.wholeText.isNotEmpty())
                selfChildren.add(HtmlModel(
                    tag = HtmlTag.SPAN,
                    content = child.wholeText,
                    attributes = selfAttributes,
                    children = listOf(),
                    styles = selfStyles
                ))
        } else if (child is Element) {
            selfChildren.add(child.toHtmlModel(selfStyles))
        }
    }

    return HtmlModel(
        tag = selfTag,
        content = "",
        attributes = selfAttributes,
        children = selfChildren,
        styles = selfStyles
    )
}

private fun MutableMap<CssAttribute, String>.applySelfStyles(styles: List<String>?) {
    styles?.forEach {
        val styleKeyValue = it.split(":")
        if (styleKeyValue.size == 2) {
            try {
                this[styleKeyValue[0].toCssAttribute()] = styleKeyValue[1].trim()
            } catch(e: IllegalArgumentException) {
                // 지정되지 않은 속성 무시
            }
        }
    }
    this.convertFontWeightToFontStyle()
}

private fun MutableMap<CssAttribute, String>.convertFontWeightToFontStyle() {
    this[CssAttribute.FONT_WEIGHT]?.let {
        when (it) {
            "bold", "bolder" -> {
                this[CssAttribute.FONT_STYLE] = BOLD
            }
            "lighter", "normal" -> {
                this[CssAttribute.FONT_STYLE] = NORMAL
            }
        }
        try {
            if (it.toInt() <= 500) {
                this[CssAttribute.FONT_STYLE] = NORMAL
            } else {
                this[CssAttribute.FONT_STYLE] = BOLD
            }
        } catch (e: NumberFormatException) { }
    }.also {
        this.remove(CssAttribute.FONT_WEIGHT)
    }
}

private fun MutableMap<CssAttribute, String>.removeNonInheritableStyles() {
    this.remove(CssAttribute.WIDTH)
    this.remove(CssAttribute.HEIGHT)
    this.remove(CssAttribute.MARGIN)
    this.remove(CssAttribute.PADDING)
    this.remove(CssAttribute.BORDER)
    this.remove(CssAttribute.BORDER_RADIUS)
    this.remove(CssAttribute.BORDER_COLOR)
    this.remove(CssAttribute.BORDER_WIDTH)
    this.remove(CssAttribute.DISPLAY)
    this.remove(CssAttribute.BACKGROUND_IMAGE)
    this.remove(CssAttribute.BACKGROUND_SIZE)
}