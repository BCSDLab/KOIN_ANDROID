package `in`.koreatech.koin.feature.user.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

/**
 * Koin User Progress Header
 * @param text: Header text
 * @param currentStep: Current step of progress
 * @param maxStep: Maximum step of progress
 * @param modifier: [Modifier]
 */
@Composable
fun KoinUserProgressHeader(
    text: String,
    currentStep: Int,
    maxStep: Int,
    modifier: Modifier = Modifier
) {
    require(currentStep <= maxStep) { "Current step should be less than or equal to max step" }

    Row(
        modifier = modifier
    ) {
        Text(
            text = "$currentStep. $text",
            style = KoinTheme.typography.medium16,
            color = KoinTheme.colors.primary500
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "$currentStep / $maxStep",
            style = KoinTheme.typography.medium16,
            color = KoinTheme.colors.primary500
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinUserProgressPreview() {
    KoinTheme {
        KoinUserProgressHeader(
            text = "약관 동의",
            currentStep = 1,
            maxStep = 4
        )
    }
}
