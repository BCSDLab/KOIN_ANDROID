package `in`.koreatech.koin.domain.model.article.html

data class HtmlModel(
    val tag: HtmlTag,
    val content: String = "",   // 직접 속하는 내용(child의 text는 제외)
    val attributes: Map<HtmlAttribute, String> = emptyMap(),
    val children: List<HtmlModel> = emptyList(),
    val styles: Map<CssAttribute, String> = emptyMap(),
)
