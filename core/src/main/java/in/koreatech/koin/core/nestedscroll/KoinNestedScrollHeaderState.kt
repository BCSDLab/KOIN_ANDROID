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
    val headerMaxHeightPx: Float = 0f,
    val headerMinHeightPx: Float = 0f
) {
    val headerOffsetPx: Float
        get() = headerOffsetAnimatable.value

    @Stable
    @Composable
    fun progress(): State<Float> = remember {
        derivedStateOf {
            val offset = headerOffsetAnimatable.value
            val range = headerMaxHeightPx - headerMinHeightPx
            ((-offset) / range).coerceIn(0f, 1f)
        }
    }

    @Composable
    fun currentHeaderHeightDp(): State<Dp> {
        val density = LocalDensity.current
        return remember(headerOffsetAnimatable) {
            derivedStateOf {
                with(density) {
                    (headerMaxHeightPx + headerOffsetAnimatable.value).toDp()
                }
            }
        }
    }

    suspend fun collapseHeader() {
        headerOffsetAnimatable.animateTo(
            targetValue = -headerMaxHeightPx + headerMinHeightPx,
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
        headerOffsetAnimatable.snapTo(offset)
    }
}

@Composable
fun rememberKoinNestedScrollHeaderState(
    headerMinHeight: Dp = 60.dp,
    headerMaxHeight: Dp = 300.dp
): KoinNestedScrollHeaderState {
    val headerOffsetAnimatable = remember { Animatable(0f) }
    val headerMaxHeightPx = with(LocalDensity.current) { headerMaxHeight.toPx() }
    val headerMinHeightPx = with(LocalDensity.current) { headerMinHeight.toPx() }

    return remember(headerOffsetAnimatable, headerMaxHeightPx, headerMinHeightPx) {
        KoinNestedScrollHeaderState(
            headerOffsetAnimatable = headerOffsetAnimatable,
            headerMaxHeightPx = headerMaxHeightPx,
            headerMinHeightPx = headerMinHeightPx
        )
    }
}
