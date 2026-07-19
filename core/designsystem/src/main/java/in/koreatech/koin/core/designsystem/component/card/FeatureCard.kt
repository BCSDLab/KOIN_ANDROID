package `in`.koreatech.koin.core.designsystem.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.icon.IconBadge
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

object FeatureCardDefaults {
    val Shape: Shape = RoundedCornerShape(16.dp)
    val BorderWidth: Dp = 0.5.dp
    val ContentPadding = PaddingValues(vertical = 20.dp, horizontal = 20.dp)
    val IconSpacing: Dp = 6.dp

    @Composable
    fun colors(
        backgroundColor: Color = Color(0xFFFFFFFF),
        borderColor: Color = Color(0xFFE6E6E6)
    ): FeatureCardColors = FeatureCardColors(
        backgroundColor = backgroundColor,
        borderColor = borderColor
    )
}

@Immutable
data class FeatureCardColors(
    val backgroundColor: Color,
    val borderColor: Color
)

@Composable
fun FeatureCard(
    title: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    description: (@Composable () -> Unit)? = null,
    actionButton: (@Composable () -> Unit)? = null,
    colors: FeatureCardColors = FeatureCardDefaults.colors(),
    shape: Shape = FeatureCardDefaults.Shape,
    contentPadding: PaddingValues = FeatureCardDefaults.ContentPadding,
    borderWidth: Dp = FeatureCardDefaults.BorderWidth,
    iconSpacing: Dp = FeatureCardDefaults.IconSpacing
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(borderWidth, colors.borderColor, shape)
            .clip(shape)
            .background(color = colors.backgroundColor)
            .padding(contentPadding)
    ) {
        icon()

        Spacer(modifier = Modifier.height(iconSpacing))

        title()

        if (description != null) {
            description()
        }

        if (actionButton != null) {
            Spacer(modifier = Modifier.height(6.dp))
            actionButton()
        }
    }
}

@Preview
@Composable
private fun FeatureCardPreview() {
    RebrandKoinTheme {
        FeatureCard(
            title = {
                Text(
                    text = "버스 시간표",
                    style = RebrandKoinTheme.typography.medium15
                )
            },
            icon = {
                IconBadge(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            },
            description = {
                Text(
                    text = "노선 별 출발 시간",
                    style = RebrandKoinTheme.typography.regular12,
                    color = Color(0xFFA8A8A8)
                )
            },
            actionButton = {
                Text(
                    text = "조회하기 →",
                    color = Color(0xFFB611F5),
                    style = RebrandKoinTheme.typography.regular10
                )
            }
        )
    }
}
