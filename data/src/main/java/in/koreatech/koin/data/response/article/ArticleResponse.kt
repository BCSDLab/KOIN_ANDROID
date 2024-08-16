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
    return this.body().toHtmlModel()
}

fun Node.toHtmlModel(parentStyles: Map<CssAttribute, String> = mapOf()): HtmlModel {
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
//fun Element.toHtmlModel(parentStyles: Map<CssAttribute, String> = mapOf()): HtmlModel {
//    var selfTag = try {
//        this.tagName().toHtmlTag()
//    } catch (e: IllegalArgumentException) {
//        HtmlTag.UNKNOWN
//    }
//    val selfContent = this.wholeOwnText()
//    val selfAttributes = mutableMapOf<HtmlAttribute, String>()
//    val selfChildren = mutableListOf<HtmlModel>()
//    val selfStyles = mutableMapOf<CssAttribute, String>()
//
//    this.attributes().forEach { attribute ->
//        try {
//            selfAttributes[attribute.key.toHtmlAttribute()] = attribute.value
//        } catch(e: IllegalArgumentException) {
//            // 지정되지 않은 속성 무시
//        }
//    }
//
//    // 부모 스타일을 상속한 후 자신의 스타일을 적용 --> 자신의 스타일이 우선 적용됨
//    // HTML 스타일 태그는 CSS Style로 변환 후 Span 태그로 변경
//    selfStyles.putAll(parentStyles)
//    selfTag = when (selfTag) {
//        HtmlTag.B, HtmlTag.STRONG -> {
//            selfStyles[CssAttribute.FONT_STYLE] = BOLD
//            HtmlTag.SPAN
//        }
//        HtmlTag.I, HtmlTag.EM, HtmlTag.DFN -> {
//            selfStyles[CssAttribute.FONT_STYLE] = if(selfStyles[CssAttribute.FONT_STYLE] == BOLD) BOLD_ITALIC else ITALIC
//            HtmlTag.SPAN
//        }
//        HtmlTag.U -> {
//            selfStyles[CssAttribute.TEXT_DECORATION] = UNDERLINE
//            HtmlTag.SPAN
//        }
//        HtmlTag.STRIKE, HtmlTag.DEL, HtmlTag.S -> {
//            selfStyles[CssAttribute.TEXT_DECORATION] = LINE_THROUGH
//            HtmlTag.SPAN
//        }
//        else -> { selfTag }
//    }
//
//    if (selfAttributes.containsKey(HtmlAttribute.STYLE)) {
//        val styleValue = selfAttributes[HtmlAttribute.STYLE]?.split(";")
//        selfStyles.applySelfStyles(styleValue)
//    }
//
//    this.children().forEach { child ->
//        selfChildren.add(child.toHtmlModel(selfStyles))
//    }
//
//    return HtmlModel(
//        tag = selfTag,
//        content = selfContent,
//        attributes = selfAttributes,
//        children = selfChildren,
//        styles = selfStyles
//    )
//}

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