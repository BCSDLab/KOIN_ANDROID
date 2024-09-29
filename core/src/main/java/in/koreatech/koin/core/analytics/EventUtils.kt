package `in`.koreatech.koin.core.analytics

object EventUtils {

    private const val SCROLL_THRESHOLD = .7f

    fun didCrossedScrollThreshold(prevScrollRatio: Float, currentScrollRatio: Float, threshold: Float = SCROLL_THRESHOLD): Boolean {
        return prevScrollRatio < threshold && currentScrollRatio >= threshold
    }

}