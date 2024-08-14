package `in`.koreatech.koin.ui.store.ratingbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatRatingBar
import `in`.koreatech.koin.R

class StarRatingBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatRatingBar(context, attrs, defStyleAttr) {

    private val starImages = arrayOf(
        R.drawable.star_empty,
        R.drawable.star_filled
    )

    private var starDrawable: Drawable? = null

    init {
        starDrawable = AppCompatResources.getDrawable(context, starImages[1])
    }

    override fun onDraw(canvas: Canvas) {
        val starWidth = width / numStars
        for (i in 0 until numStars) {

            when {
                rating >= i + 1 -> starDrawable = AppCompatResources.getDrawable(context, starImages[1])
                else -> starDrawable = AppCompatResources.getDrawable(context, starImages[0])
            }
            starDrawable?.setBounds(i * starWidth, 0, (i + 1) * starWidth, height)
            starDrawable?.draw(canvas)
        }
    }
}
