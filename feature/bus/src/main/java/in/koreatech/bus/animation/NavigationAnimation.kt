package `in`.koreatech.bus.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.ui.graphics.Color

private const val TRANSITION_DURATION = 400
private const val FADE_RATIO = .55f
private const val SCALE_RATIO = .9f
private const val SLIDE_DIVIDE_RATIO = 3

internal val defaultOutsideColor = Color(0xFF282828)

internal fun <S> AnimatedContentTransitionScope<S>.defaultEnterTransition() =
    slideInHorizontally(
        animationSpec = tween(TRANSITION_DURATION),
        initialOffsetX = { it },
    )

internal fun <S> AnimatedContentTransitionScope<S>.defaultExitTransition() =
    fadeOut(
        animationSpec = tween(TRANSITION_DURATION),
        targetAlpha = FADE_RATIO,
    ) +
        scaleOut(
            animationSpec = tween(TRANSITION_DURATION),
            targetScale = SCALE_RATIO,
        ) +
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(TRANSITION_DURATION),
            targetOffset = { it / SLIDE_DIVIDE_RATIO },
        )

internal fun <S> AnimatedContentTransitionScope<S>.defaultPopEnterTransition() =
    fadeIn(
        animationSpec = tween(TRANSITION_DURATION),
        initialAlpha = FADE_RATIO,
    ) +
        scaleIn(
            animationSpec = tween(TRANSITION_DURATION),
            initialScale = SCALE_RATIO,
        ) +
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(TRANSITION_DURATION),
            initialOffset = { it / SLIDE_DIVIDE_RATIO },
        )

internal fun <S> AnimatedContentTransitionScope<S>.defaultPopExitTransition() =
    slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(TRANSITION_DURATION),
    )
