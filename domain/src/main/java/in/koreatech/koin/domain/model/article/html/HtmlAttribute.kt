package `in`.koreatech.koin.domain.model.article.html

enum class HtmlAttribute {
    ID,
    CLASS,
    STYLE,
    HREF,
    TITLE,
    SRC,
    ALT,  // 이미지 대체 이미지
    DOWNLOAD,
    COLSPAN,
    ROWSPAN;
}

fun String.toHtmlAttribute(): HtmlAttribute {
    return HtmlAttribute.valueOf(this.uppercase())
}