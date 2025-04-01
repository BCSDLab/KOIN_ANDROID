package `in`.koreatech.koin.feature.signup.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

/**
 * Koin Sign Up Progress Indicator
 * @param currentStep: Current step of progress
 * @param maxStep: Maximum step of progress
 * @param modifier: [Modifier]
 * @param stepColor: Color of step line
 * @param backgroundColor: Color of background line
 */
@Composable
fun KoinSignUpProgressIndicator(
    currentStep: Int,
    maxStep: Int,
    modifier: Modifier = Modifier,
    stepColor: Color = KoinTheme.colors.primary500,
    backgroundColor: Color = KoinTheme.colors.neutral400
) {
    if (currentStep > maxStep) {
        throw IllegalArgumentException("Current step should be less than or equal to max step")
    }

    Canvas(
        modifier = modifier.fillMaxWidth()
    ) {
        drawLine(
            color = backgroundColor,
            start = Offset(size.width / maxStep * currentStep, 0f),
            end = Offset(size.width, size.height),
            strokeWidth = 4.dp.toPx(),
            cap = StrokeCap.Round
        )

        drawLine(
            color = stepColor,
            start = Offset(0f, 0f),
            end = Offset(size.width / maxStep * currentStep, size.height),
            strokeWidth = 4.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KoinSignUpProgressIndicatorPreview() {
    KoinSignUpProgressIndicator(
        modifier = Modifier.padding(16.dp),
        currentStep = 1,
        maxStep = 4,
        stepColor = KoinTheme.colors.primary500,
        backgroundColor = KoinTheme.colors.neutral400
    )
}
