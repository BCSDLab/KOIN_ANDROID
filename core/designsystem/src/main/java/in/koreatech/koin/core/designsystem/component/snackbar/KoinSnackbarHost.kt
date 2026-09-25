package `in`.koreatech.koin.core.designsystem.component.snackbar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.RecomposeScope
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.AccessibilityManager
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.util.fastFilterNotNull
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMapTo
import kotlin.coroutines.resume
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Stable
class KoinSnackbarHostState {
    private val mutex = Mutex()
    var currentSnackbarData by mutableStateOf<KoinSnackbarData?>(null)
        private set

    suspend fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        duration: KoinSnackbarDuration = if (actionLabel == null) KoinSnackbarDuration.Short else KoinSnackbarDuration.Long,
        variant: SnackbarVariant = SnackbarVariant.Neutral
    ): KoinSnackbarResult {
        actionLabel?.let {
            require(it.length in 2..4) { "actionLabel length should be 2..4" }
        }

        return showSnackbar(SnackbarVisualsImpl(message, actionLabel, duration, variant))
    }

    suspend fun showSnackbar(visuals: KoinSnackbarVisuals): KoinSnackbarResult {
        visuals.actionLabel?.let {
            require(it.length in 2..4) { "actionLabel length should be 2..4" }
        }

        mutex.withLock {
            try {
                return suspendCancellableCoroutine { continuation ->
                    currentSnackbarData = SnackbarDataImpl(visuals, continuation)
                }
            } finally {
                currentSnackbarData = null
            }
        }
    }

    private class SnackbarVisualsImpl(
        override val message: String,
        override val actionLabel: String?,
        override val duration: KoinSnackbarDuration,
        override val variant: SnackbarVariant
    ) : KoinSnackbarVisuals {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is SnackbarVisualsImpl) return false
            if (message != other.message) return false
            if (actionLabel != other.actionLabel) return false
            if (duration != other.duration) return false
            if (variant != other.variant) return false
            return true
        }

        override fun hashCode(): Int {
            var result = message.hashCode()
            result = 31 * result + actionLabel.hashCode()
            result = 31 * result + duration.hashCode()
            result = 31 * result + variant.hashCode()
            return result
        }
    }

    private class SnackbarDataImpl(
        override val visuals: KoinSnackbarVisuals,
        private val continuation: CancellableContinuation<KoinSnackbarResult>
    ) : KoinSnackbarData {
        override fun performAction() {
            if (continuation.isActive) continuation.resume(KoinSnackbarResult.ActionPerformed)
        }

        override fun dismiss() {
            if (continuation.isActive) continuation.resume(KoinSnackbarResult.Dismissed)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is SnackbarDataImpl) return false
            if (visuals != other.visuals) return false
            if (continuation != other.continuation) return false
            return true
        }

        override fun hashCode(): Int {
            var result = visuals.hashCode()
            result = 31 * result + continuation.hashCode()
            return result
        }
    }
}

@Composable
fun rememberKoinSnackbarHostState() = remember { KoinSnackbarHostState() }

@Composable
fun KoinSnackbarHost(
    hostState: KoinSnackbarHostState,
    modifier: Modifier = Modifier,
    snackbar: @Composable (KoinSnackbarData) -> Unit = { KoinSnackbar(it) }
) {
    val currentSnackbarData = hostState.currentSnackbarData
    val accessibilityManager = LocalAccessibilityManager.current
    LaunchedEffect(currentSnackbarData) {
        if (currentSnackbarData != null) {
            val duration = currentSnackbarData.visuals.duration.toMillis(
                currentSnackbarData.visuals.actionLabel != null,
                accessibilityManager
            ).milliseconds
            delay(duration)
            currentSnackbarData.dismiss()
        }
    }
    FadeInFadeOutWithScale(
        current = hostState.currentSnackbarData,
        modifier = modifier,
        content = snackbar
    )
}

@Stable
interface KoinSnackbarVisuals {
    val message: String
    val actionLabel: String?
    val duration: KoinSnackbarDuration
    val variant: SnackbarVariant
}

@Stable
interface KoinSnackbarData {
    val visuals: KoinSnackbarVisuals
    fun performAction()
    fun dismiss()
}

enum class KoinSnackbarResult {
    Dismissed,
    ActionPerformed
}

enum class KoinSnackbarDuration {
    Short,
    Long
}

/**
 * TODO
 * Add more variants
 */
enum class SnackbarVariant {
    Neutral
}

internal fun KoinSnackbarDuration.toMillis(
    hasAction: Boolean,
    accessibilityManager: AccessibilityManager?
): Long {
    val original = when (this) {
        KoinSnackbarDuration.Long -> 10000L
        KoinSnackbarDuration.Short -> 4000L
    }
    if (accessibilityManager == null) {
        return original
    }
    return accessibilityManager.calculateRecommendedTimeoutMillis(
        original,
        containsIcons = true,
        containsText = true,
        containsControls = hasAction
    )
}

@Composable
private fun FadeInFadeOutWithScale(
    current: KoinSnackbarData?,
    modifier: Modifier = Modifier,
    content: @Composable (KoinSnackbarData) -> Unit
) {
    val state = remember { FadeInFadeOutState<KoinSnackbarData?>() }
    if (current != state.current) {
        state.current = current
        val keys = state.items.fastMap { it.key }.toMutableList()
        if (!keys.contains(current)) {
            keys.add(current)
        }
        state.items.clear()
        keys.fastFilterNotNull().fastMapTo(state.items) { key ->
            FadeInFadeOutAnimationItem(key) { children ->
                val isVisible = key == current
                val opacity = animatedOpacity(
                    animation = spring(stiffness = 400f),
                    visible = isVisible,
                    onAnimationFinish = {
                        if (key != state.current) {
                            state.items.removeAll { it.key == key }
                            state.scope?.invalidate()
                        }
                    }
                )
                val scale = animatedScale(
                    animation = spring(stiffness = 400f),
                    visible = isVisible
                )
                Box(
                    modifier = Modifier
                        .graphicsLayer(
                            scaleX = scale.value,
                            scaleY = scale.value,
                            alpha = opacity.value
                        )
                        .semantics {
                            if (isVisible) {
                                liveRegion = LiveRegionMode.Polite
                            }
                            dismiss {
                                key.dismiss()
                                true
                            }
                            paneTitle = "KoinSnackbar"
                        }
                ) {
                    children()
                }
            }
        }
    }
    Box(modifier.windowInsetsPadding(WindowInsets.systemBars)) {
        state.scope = currentRecomposeScope
        state.items.fastForEach { (item, opacity) -> key(item) { opacity { content(item!!) } } }
    }
}

private class FadeInFadeOutState<T> {
    var current: Any? = Any()
    var items = mutableListOf<FadeInFadeOutAnimationItem<T>>()
    var scope: RecomposeScope? = null
}

private data class FadeInFadeOutAnimationItem<T>(
    val key: T,
    val transition: FadeInFadeOutTransition
)
private typealias FadeInFadeOutTransition = @Composable (content: @Composable () -> Unit) -> Unit

@Composable
private fun animatedOpacity(
    animation: AnimationSpec<Float>,
    visible: Boolean,
    onAnimationFinish: () -> Unit = {}
): State<Float> {
    val alpha = remember { Animatable(if (!visible) 1f else 0f) }
    LaunchedEffect(visible) {
        alpha.animateTo(if (visible) 1f else 0f, animationSpec = animation)
        onAnimationFinish()
    }
    return alpha.asState()
}

@Composable
private fun animatedScale(animation: AnimationSpec<Float>, visible: Boolean): State<Float> {
    val scale = remember { Animatable(if (!visible) 1f else 0.8f) }
    LaunchedEffect(visible) {
        scale.animateTo(if (visible) 1f else 0.8f, animationSpec = animation)
    }
    return scale.asState()
}
