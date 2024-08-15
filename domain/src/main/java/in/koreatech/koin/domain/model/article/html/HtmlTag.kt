package `in`.koreatech.koin.domain.model.article.html

enum class HtmlTag {
    HTML,
    HEAD,
    BODY,
    TITLE,
    P,
    A,
    SPAN,
    DIV,
    BR,
    IMG,
    CITE,
    B,
    I,
    STRONG,
    EM,
    U,
    DFN,
    SUB,
    SUP,
    BLOCKQUOTE,
    BIG,
    SMALL,
    STRIKE,
    DEL,
    S,
    CENTER,
    OL,
    UL,
    LI,
    DL,
    DT,
    DD,
    TABLE,
    CAPTION,
    THEAD,
    TFOOT,
    TBODY,
    TR,
    TH,
    TD,
    UNKNOWN
}

fun String.toHtmlTag(): HtmlTag {
    return HtmlTag.valueOf(this.uppercase())
}