package `in`.koreatech.koin.core.viewpager

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class ScaledViewPager2Transformation(
    private val itemMarginPx: Float,
    private val nextItemVisiblePx: Float
) : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pageTranslationX = nextItemVisiblePx + itemMarginPx

        page.translationX = - pageTranslationX * position
        page.scaleY = 1 - (0.1f * abs(position))
    }

}