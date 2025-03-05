package `in`.koreatech.koin.core.onboarding

import com.skydoves.balloon.ArrowOrientation

enum class ArrowDirection {
    BOTTOM,
    TOP,
    LEFT,
    RIGHT,
    ;

    fun toArrowOrientation(): ArrowOrientation {
        return when (this) {
            BOTTOM -> ArrowOrientation.BOTTOM
            TOP -> ArrowOrientation.TOP
            LEFT -> ArrowOrientation.START
            RIGHT -> ArrowOrientation.END
        }
    }
}
