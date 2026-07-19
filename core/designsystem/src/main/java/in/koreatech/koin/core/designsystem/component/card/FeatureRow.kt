package `in`.koreatech.koin.core.designsystem.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.icon.IconBadge
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

object FeatureRowDefaults {
    val Shape: Shape = RoundedCornerShape(16.dp)
    val BorderWidth: Dp = 1.dp
    val ContentPadding = PaddingValues(vertical = 16.dp, horizontal = 20.dp)
    val IconSpacing: Dp = 20.dp
    val LabelSpacing: Dp = 8.dp

    @Composable
    fun colors(
        backgroundColor: Color = Color(0xFFFFFFFF),
        borderColor: Color = Color(0xFFE6E6E6)
    ): FeatureRowColors = FeatureRowColors(
        backgroundColor = backgroundColor,
        borderColor = borderColor
    )
}

@Immutable
data class FeatureRowColors(
    val backgroundColor: Color,
    val borderColor: Color
)

@Composable
fun FeatureRow(
    title: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    label: (@Composable () -> Unit)? = null,
    description: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    colors: FeatureRowColors = FeatureRowDefaults.colors(),
    shape: Shape = FeatureRowDefaults.Shape,
    contentPadding: PaddingValues = FeatureRowDefaults.ContentPadding,
    borderWidth: Dp = FeatureRowDefaults.BorderWidth,
    iconSpacing: Dp = FeatureRowDefaults.IconSpacing,
    labelSpacing: Dp = FeatureRowDefaults.LabelSpacing
) {
    Row(
        modifier = modifier
            .border(borderWidth, colors.borderColor, shape)
            .clip(shape)
            .background(color = colors.backgroundColor)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()

        Spacer(modifier = Modifier.width(iconSpacing))

        Column {
            if (label != null) {
                label()
                Spacer(modifier = Modifier.height(labelSpacing))
            }
            title()
            if (description != null) {
                description()
            }
        }

        if (trailingIcon != null) {
            Spacer(modifier = Modifier.weight(1f))
            trailingIcon()
        }
    }
}

@Preview
@Composable
private fun FeatureRowPreview() {
    RebrandKoinTheme {
        FeatureRow(
            title = {
                Text(
                    text = "많이 찾는 상점\n둘러보기",
                    style = RebrandKoinTheme.typography.medium16
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
                    text = "영업중",
                    style = RebrandKoinTheme.typography.regular13
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        )
    }
}
