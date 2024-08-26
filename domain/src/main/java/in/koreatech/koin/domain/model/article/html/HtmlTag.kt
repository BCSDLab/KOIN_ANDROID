package `in`.koreatech.koin.domain.model.article.html

enum class HtmlTag {
    HTML,
    HEAD,
    BODY,
    H1,
    H2,
    H3,
    H4,
    H5,
    H6,
    HR,
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
    UNKNOWN,
}

fun String.toHtmlTag(): HtmlTag {
    return try {
        HtmlTag.valueOf(this.uppercase())
    } catch (e: IllegalArgumentException) {
        HtmlTag.UNKNOWN
    }
}