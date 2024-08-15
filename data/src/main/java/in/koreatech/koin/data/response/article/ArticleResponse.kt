package `in`.koreatech.koin.data.response.article

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.article.Article
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
    fun toArticle() = Article(
        id = id ?: 0,
        boardId = boardId ?: 0,
        title = title ?: "",
        html = Jsoup.parse(content ?: "").toHtmlModel(),
        author = author ?: "",
        viewCount = viewCount ?: 0,
        createdAt = createdAt ?: "",
        updatedAt = updatedAt ?: ""
    )
}

fun Document.toHtmlModel(): HtmlModel {
    return this.body().toHtmlModel()
}

fun Element.toHtmlModel(): HtmlModel {
    val selfTag = try {
        this.tagName().toHtmlTag()
    } catch (e: IllegalArgumentException) {
        HtmlTag.UNKNOWN
    }
    val selfContent = this.wholeText()
    val selfAttributes = mutableMapOf<HtmlAttribute, String>()
    val selfChildren = mutableListOf<HtmlModel>()
    val selfStyles = mutableMapOf<CssAttribute, String>()

    this.attributes().forEach { attribute ->
        try {
            selfAttributes[attribute.key.toHtmlAttribute()] = attribute.value
        } catch(e: IllegalArgumentException) {
            // 지정되지 않은 속성 무시
        }
    }

    if (selfAttributes.containsKey(HtmlAttribute.STYLE)) {
        val styleValue = selfAttributes[HtmlAttribute.STYLE]?.split(";")
        styleValue?.forEach {
            val styleKeyValue = it.split(":")
            if (styleKeyValue.size == 2) {
                try {
                    selfStyles[styleKeyValue[0].toCssAttribute()] = styleKeyValue[1].trim()
                } catch(e: IllegalArgumentException) {
                    // 지정되지 않은 속성 무시
                }
            }
        }
    }

    this.children().forEach { child ->
        selfChildren.add(child.toHtmlModel())
    }

    return HtmlModel(
        tag = selfTag,
        content = selfContent,
        attributes = selfAttributes,
        children = selfChildren,
        styles = selfStyles
    )
}