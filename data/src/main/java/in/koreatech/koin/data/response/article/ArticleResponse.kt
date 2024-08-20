package `in`.koreatech.koin.data.response.article

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.constant.BOLD
import `in`.koreatech.koin.domain.constant.BOLD_ITALIC
import `in`.koreatech.koin.domain.constant.ITALIC
import `in`.koreatech.koin.domain.constant.LINE_THROUGH
import `in`.koreatech.koin.domain.constant.UNDERLINE
import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.domain.model.article.html.ArticleContent
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
    @SerializedName("nickname") val author: String?,
    @SerializedName("hit") val viewCount: Int?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
) {
    fun toArticleHeader() = ArticleHeader(
        id = id ?: 0,
        boardId = boardId ?: 0,
        title = title ?: "",
        author = author ?: "",
        viewCount = viewCount ?: 0,
        createdAt = createdAt ?: "",
        updatedAt = updatedAt ?: ""
    )

    fun toArticleContent() = ArticleContent(
        html = Jsoup.parse(content ?: "").toHtmlModel(),
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

    selfStyles.putAll(parentStyles)
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
                HtmlTag.SPAN
            }

            HtmlTag.H2 -> {
                selfStyles[CssAttribute.FONT_SIZE] = "1.5em"
                HtmlTag.SPAN
            }

            HtmlTag.H3 -> {
                selfStyles[CssAttribute.FONT_SIZE] = "1.17em"
                HtmlTag.SPAN
            }

            HtmlTag.H4 -> {
                selfStyles[CssAttribute.FONT_SIZE] = "1em"
                HtmlTag.SPAN
            }

            HtmlTag.H5 -> {
                selfStyles[CssAttribute.FONT_SIZE] = "0.83em"
                HtmlTag.SPAN
            }

            HtmlTag.H6 -> {
                selfStyles[CssAttribute.FONT_SIZE] = "0.67em"
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
}