package `in`.koreatech.koin.core.designsystem.component.snackbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

object KoinSnackbarDefaults {
    private val snackbarTextStyle
        @Composable get() = KoinTheme.typography.regular14.merge(color = KoinTheme.colors.neutral0)

    private val snackbarBackgroundColor
        @Composable get() = KoinTheme.colors.neutral500.copy(alpha = 0.7f)

    private val snackbarButtonBackgroundColor
        @Composable get() = KoinTheme.colors.neutral700

    private val snackbarOutsidePadding = PaddingValues(horizontal = 24.dp)

    private val snackbarInnerPadding = PaddingValues(vertical = 16.dp, horizontal = 20.dp)

    private val snackbarCornerShape = RoundedCornerShape(8.dp)

    @Composable
    fun koinSnackbarStyle(
        snackbarTextStyle: TextStyle = KoinSnackbarDefaults.snackbarTextStyle,
        snackbarBackgroundColor: Color = KoinSnackbarDefaults.snackbarBackgroundColor,
        snackbarButtonBackgroundColor: Color = KoinSnackbarDefaults.snackbarButtonBackgroundColor,
        snackbarOutsidePadding: PaddingValues = KoinSnackbarDefaults.snackbarOutsidePadding,
        snackbarInnerPadding: PaddingValues = KoinSnackbarDefaults.snackbarInnerPadding,
        snackbarCornerShape: Shape = KoinSnackbarDefaults.snackbarCornerShape
    ): KoinSnackbarStyle = KoinSnackbarStyle(
        snackbarTextStyle = snackbarTextStyle,
        snackbarBackgroundColor = snackbarBackgroundColor,
        snackbarButtonBackgroundColor = snackbarButtonBackgroundColor,
        snackbarOutsidePadding = snackbarOutsidePadding,
        snackbarInnerPadding = snackbarInnerPadding,
        snackbarCornerShape = snackbarCornerShape
    )
}

@Immutable
data class KoinSnackbarStyle(
    val snackbarTextStyle: TextStyle,
    val snackbarBackgroundColor: Color,
    val snackbarButtonBackgroundColor: Color,
    val snackbarOutsidePadding: PaddingValues,
    val snackbarInnerPadding: PaddingValues,
    val snackbarCornerShape: Shape
)

@Composable
fun KoinSnackbar(
    snackbarData: KoinSnackbarData,
    modifier: Modifier = Modifier,
    snackbarStyle: KoinSnackbarStyle = KoinSnackbarDefaults.koinSnackbarStyle()
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = SnackbarMinHeight)
            .padding(snackbarStyle.snackbarOutsidePadding)
            .clip(snackbarStyle.snackbarCornerShape)
            .background(snackbarStyle.snackbarBackgroundColor)
            .padding(snackbarStyle.snackbarInnerPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val icon = when (snackbarData.visuals.variant) {
            SnackbarVariant.Neutral -> null
        }

        // Use after variants with icon added
        icon?.let {
            Image(
                modifier = Modifier.size(16.dp),
                imageVector = it,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(12.dp))
        }

        BasicText(
            text = snackbarData.visuals.message,
            style = snackbarStyle.snackbarTextStyle
        )

        snackbarData.visuals.actionLabel?.let {
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(snackbarStyle.snackbarButtonBackgroundColor, CircleShape)
                    .clickable {
                        snackbarData.performAction()
                    }
                    .padding(vertical = 8.dp, horizontal = 20.dp)
            ) {
                BasicText(
                    style = snackbarStyle.snackbarTextStyle,
                    text = it
                )
            }
        }
    }
}

private val SnackbarMinHeight = 56.dp
