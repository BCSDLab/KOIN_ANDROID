package `in`.koreatech.koin.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.home.R

object SmallHomeCardDefaults {
    val Shape: Shape = RoundedCornerShape(16.dp)
    val BorderWidth: Dp = 0.5.dp
    val ContentPadding = PaddingValues(vertical = 20.dp, horizontal = 20.dp)

    @Composable
    fun colors(
        backgroundColor: Color = Color(0xFFFFFFFF),
        borderColor: Color = Color(0xFFE6E6E6)
    ): SmallHomeCardColors = SmallHomeCardColors(
        backgroundColor = backgroundColor,
        borderColor = borderColor
    )
}

@Immutable
data class SmallHomeCardColors(
    val backgroundColor: Color,
    val borderColor: Color
)

@Composable
fun SmallHomeCard(
    title: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    description: (@Composable () -> Unit)?,
    actionButton: (@Composable () -> Unit)?,
    colors: SmallHomeCardColors = SmallHomeCardDefaults.colors(),
    shape: Shape = SmallHomeCardDefaults.Shape,
    contentPadding: PaddingValues = SmallHomeCardDefaults.ContentPadding,
    borderWidth: Dp = SmallHomeCardDefaults.BorderWidth
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .border(borderWidth, colors.borderColor, shape)
            .clip(shape)
            .background(color = colors.backgroundColor)
            .padding(contentPadding)
    ) {
        icon()

        Spacer(modifier = Modifier.height(6.dp))

        title()

        if (description != null) {
            description()
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (actionButton != null) {
            actionButton()
        }
    }
}

@Preview
@Composable
private fun SmallHomeCardPreview() {
    SmallHomeCard(
        title = {
            Text(
                text = "버스 시간표",
                style = RebrandKoinTheme.typography.medium15
            )
        },
        icon = {
            HomeIcon(
                tint = RebrandKoinTheme.colors.primary500,
                backgroundColor = RebrandKoinTheme.colors.neutral100,
                imageVector = ImageVector.vectorResource(R.drawable.ic_home_bus_timetable),
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
