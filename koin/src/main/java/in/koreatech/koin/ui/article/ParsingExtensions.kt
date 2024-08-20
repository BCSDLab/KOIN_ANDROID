package `in`.koreatech.koin.ui.article

import android.graphics.Color

internal fun String.parseColor(default: Int): Int {
    return try {
        Color.parseColor(this)
    } catch (e: IllegalArgumentException) {
        parseRgbColor(this, default)
    }
}

private fun parseRgbColor(rgbString: String, default: Int): Int {
    val regex = """rgb\((\d+), (\d+), (\d+)\)""".toRegex()
    val matchResult = regex.matchEntire(rgbString)

    return if (matchResult != null) {
        val (r, g, b) = matchResult.destructured
        Color.rgb(r.toInt(), g.toInt(), b.toInt())
    } else {
        default
    }
}

internal fun String.parseAbsoluteFontSize(): Int {
    return when {
        this.endsWith("px") -> this.removeSuffix("px").toFloat().toInt()
        this.endsWith("pt") -> (this.removeSuffix("pt").toFloat() * 1.3).toInt()
        else -> 12
    }
}

internal fun String.parseFontRatio(): Float {
    return when {
        this.endsWith("em") -> this.removeSuffix("em").toFloat()
        this.endsWith("%") -> this.removeSuffix("%").toFloat()
        this.endsWith("rem") -> this.removeSuffix("rem").toFloat() / 12
        else -> 1f
    }
}