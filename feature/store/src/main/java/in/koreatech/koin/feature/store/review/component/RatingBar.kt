package `in`.koreatech.koin.feature.store.review.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.feature.store.R

@Composable
fun RatingBar(
    rating: Int,
    modifier: Modifier = Modifier,
    minRating: Int = 0,
    maxRating: Int = 5,
    iconSize: Dp = 40.dp,
    onRatingChanged: (Int) -> Unit = {}
) {
    require(minRating >= 0) { "minRating should not smaller than $minRating" }
    require(rating in minRating..maxRating) { "rating should be $minRating to $maxRating" }

    var offset by remember { mutableFloatStateOf(0f) }

    val iconSizeInPx = with(LocalDensity.current) { iconSize.toPx() }

    LaunchedEffect(rating) {
        snapshotFlow { offset }.collect {
            if (offset > iconSizeInPx) {
                if (rating < maxRating) onRatingChanged(rating + 1)
                offset = 0f
            } else if (offset < -iconSizeInPx) {
                if (rating > minRating) onRatingChanged(rating - 1)
                offset = 0f
            }
        }
    }

    Row(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.systemBars)
            .windowInsetsPadding(WindowInsets.safeGestures)
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    offset += delta
                }
            )
    ) {
        repeat(maxRating) {
            Image(
                modifier = Modifier
                    .size(iconSize)
                    .noRippleClickable {
                        onRatingChanged(it + 1)
                    },
                imageVector = ImageVector.vectorResource(if (it + 1 <= rating) R.drawable.ic_star else R.drawable.ic_star_empty),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun RatingBarPreview() {
    RatingBar(1)
}
