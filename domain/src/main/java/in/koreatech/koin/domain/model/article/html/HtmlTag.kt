package `in`.koreatech.koin.domain.model.article.html

enum class HtmlTag(
    val viewType: ViewType = ViewType.UNKNOWN
) {
    HTML,
    HEAD,
    BODY,
    TITLE(ViewType.TEXT_VIEW),
    P(ViewType.TEXT_VIEW),
    A(ViewType.TEXT_VIEW),
    SPAN(ViewType.TEXT_VIEW),
    DIV(ViewType.TEXT_VIEW),
    BR(ViewType.TEXT_VIEW),
    IMG(ViewType.TEXT_VIEW),
    CITE(ViewType.TEXT_VIEW),
    B(ViewType.TEXT_VIEW),
    I(ViewType.TEXT_VIEW),
    STRONG(ViewType.TEXT_VIEW),
    EM(ViewType.TEXT_VIEW),
    U(ViewType.TEXT_VIEW),
    DFN(ViewType.TEXT_VIEW),
    SUB(ViewType.TEXT_VIEW),
    SUP(ViewType.TEXT_VIEW),
    BLOCKQUOTE(ViewType.TEXT_VIEW),
    BIG(ViewType.TEXT_VIEW),
    SMALL(ViewType.TEXT_VIEW),
    STRIKE(ViewType.TEXT_VIEW),
    DEL(ViewType.TEXT_VIEW),
    S(ViewType.TEXT_VIEW),
    CENTER(ViewType.TEXT_VIEW),
    OL(ViewType.ORDERED_LIST_VIEW),
    UL(ViewType.UNORDERED_LIST_VIEW),
    LI(ViewType.TEXT_VIEW),
    DL(ViewType.TEXT_VIEW),
    DT(ViewType.TEXT_VIEW),
    DD(ViewType.TEXT_VIEW),
    TABLE(ViewType.TABLE_VIEW),
    CAPTION,
    THEAD,
    TFOOT,
    TBODY,
    TR,
    TH,
    TD,
    UNKNOWN,
    NONE    // TextNode
}

enum class ViewType {
    TEXT_VIEW,
    TABLE_VIEW,
    ORDERED_LIST_VIEW,
    UNORDERED_LIST_VIEW,
    UNKNOWN
}

fun String.toHtmlTag(): HtmlTag {
    return HtmlTag.valueOf(this.uppercase())
}