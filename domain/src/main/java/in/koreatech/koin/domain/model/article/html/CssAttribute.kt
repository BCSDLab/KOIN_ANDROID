package `in`.koreatech.koin.domain.model.article.html

enum class CssAttribute {
    FONT_SIZE,
    DEFAULT_FONT_SIZE,
    FONT_WEIGHT,    // FONT_STYLE로 변환. 즉 UI 단에서 실제 사용은 X
    FONT_STYLE,
    TEXT_DECORATION,
    TEXT_ALIGN,
    COLOR,
    BACKGROUND,
    BACKGROUND_COLOR,
    MARGIN,
    PADDING,
    BORDER,
    BORDER_RADIUS,
    BORDER_COLOR,
    BORDER_STYLE,
    BORDER_WIDTH,
    DISPLAY,
    WIDTH,
    HEIGHT,
    LINE_HEIGHT,
    VERTICAL_ALIGN,
    TEXT_TRANSFORM,
    TEXT_OVERFLOW,
    WHITE_SPACE,
    OVERFLOW,
    POSITION,
    TOP,
    RIGHT,
    BOTTOM,
    LEFT,
    Z_INDEX,
    FLOAT,
    CLEAR,
    LIST_STYLE_TYPE,
    LIST_STYLE_POSITION,
    LIST_STYLE_IMAGE,
    BACKGROUND_IMAGE,
    BACKGROUND_REPEAT,
    BACKGROUND_POSITION,
    BACKGROUND_SIZE,
    BACKGROUND_ORIGIN,
    BACKGROUND_CLIP,
    BACKGROUND_ATTACHMENT,
    BOX_SHADOW,
    TEXT_SHADOW
}

fun String.toCssAttribute(): CssAttribute {
    return CssAttribute.valueOf(this.trim().replaceDashToUnderbar().uppercase())
}

private fun String.replaceDashToUnderbar(): String {
    return this.replace("-", "_")
}