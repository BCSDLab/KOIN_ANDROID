package `in`.koreatech.koin.core.nestedscroll

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
open class KoinNestedScrollHeaderState(
    val headerOffsetAnimatable: Animatable<Float, *>,
    val headerExpandedHeightPx: Float = 0f,
    val headerCollapsedHeightPx: Float = 0f
) {
    val headerOffsetPx: Float
        get() = headerOffsetAnimatable.value

    val range: Float
        get() = headerExpandedHeightPx - headerCollapsedHeightPx

    @Stable
    @Composable
    fun progress(): State<Float> = remember {
        derivedStateOf {
            val offset = headerOffsetAnimatable.value
            ((-offset) / range).coerceIn(0f, 1f)
        }
    }

    @Composable
    fun currentHeaderHeightDp(): State<Dp> {
        val density = LocalDensity.current
        return remember(headerOffsetPx) {
            derivedStateOf {
                with(density) {
                    (headerExpandedHeightPx + headerOffsetPx).toDp()
                }
            }
        }
    }

    suspend fun collapseHeader() {
        headerOffsetAnimatable.animateTo(
            targetValue = -range,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    suspend fun expandHeader() {
        headerOffsetAnimatable.animateTo(
            targetValue = 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    suspend fun snapOffset(offset: Float) {
        headerOffsetAnimatable.snapTo(offset.coerceIn(-range, 0f))
    }
}

@Composable
fun rememberKoinNestedScrollHeaderState(
    headerCollapsedHeight: Dp = 60.dp,
    headerExpandedHeight: Dp = 300.dp
): KoinNestedScrollHeaderState {
    val headerOffsetAnimatable = remember { Animatable(0f) }
    val headerExpandedHeightPx = with(LocalDensity.current) { headerExpandedHeight.toPx() }
    val headerCollapsedHeightPx = with(LocalDensity.current) { headerCollapsedHeight.toPx() }

    return remember(headerOffsetAnimatable, headerExpandedHeightPx, headerCollapsedHeightPx) {
        KoinNestedScrollHeaderState(
            headerOffsetAnimatable = headerOffsetAnimatable,
            headerExpandedHeightPx = headerExpandedHeightPx,
            headerCollapsedHeightPx = headerCollapsedHeightPx
        )
    }
}
