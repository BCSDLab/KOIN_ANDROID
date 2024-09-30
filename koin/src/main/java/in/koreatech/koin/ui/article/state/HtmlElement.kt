package `in`.koreatech.koin.ui.article.state

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.article.html.CssAttribute
import `in`.koreatech.koin.domain.model.article.html.HtmlAttribute
import `in`.koreatech.koin.domain.model.article.html.HtmlModel
import `in`.koreatech.koin.domain.model.article.html.HtmlTag
import kotlinx.parcelize.Parcelize

@Parcelize
data class HtmlElement(
    val tag: HtmlTag,
    val content: String = "",
    val attributes: Map<HtmlAttribute, String> = emptyMap(),
    val children: List<HtmlElement> = emptyList(),
    val styles: Map<CssAttribute, String> = emptyMap(),
) : Parcelable

fun HtmlModel.toHtmlElement(): HtmlElement {
    return HtmlElement(
        tag = tag,
        content = content,
        attributes = attributes,
        children = children.map { it.toHtmlElement() },
        styles = styles
    )
}
