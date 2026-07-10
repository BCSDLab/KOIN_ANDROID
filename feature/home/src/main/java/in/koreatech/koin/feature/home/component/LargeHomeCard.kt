package `in`.koreatech.koin.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
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

object LargeHomeCardDefaults {
    val Shape: Shape = RoundedCornerShape(16.dp)
    val BorderWidth: Dp = 1.dp
    val ContentPadding = PaddingValues(vertical = 16.dp, horizontal = 20.dp)

    @Composable
    fun colors(
        backgroundColor: Color = Color(0xFFFFFFFF),
        borderColor: Color = Color(0xFFE6E6E6)
    ): LargeHomeCardColors = LargeHomeCardColors(
        backgroundColor = backgroundColor,
        borderColor = borderColor
    )
}

@Immutable
data class LargeHomeCardColors(
    val backgroundColor: Color,
    val borderColor: Color
)

@Composable
fun LargeHomeCard(
    title: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    label: (@Composable () -> Unit)? = null,
    description: (@Composable () -> Unit)? = null,
    colors: LargeHomeCardColors = LargeHomeCardDefaults.colors(),
    shape: Shape = LargeHomeCardDefaults.Shape,
    contentPadding: PaddingValues = LargeHomeCardDefaults.ContentPadding,
    borderWidth: Dp = LargeHomeCardDefaults.BorderWidth,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .border(borderWidth, colors.borderColor, shape)
            .clip(shape)
            .background(color = colors.backgroundColor)
            .clickable(onClick = onClick)
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()

        Spacer(modifier = Modifier.width(20.dp))

        Column(
            modifier = Modifier
        ) {
            if (label != null) {
                label()

                Spacer(modifier = Modifier.height(8.dp))
            }
            title()
            if (description != null) {
                description()
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_home_card_arrow),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun LargeHomeCardPreview() {
    LargeHomeCard(
        title = {
            Text(
                text = "많이 찾는 상점\n" +
                    "둘러보기",
                style = RebrandKoinTheme.typography.medium16
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
        label = {
            HomeChip(
                text = "KOIN 전용 이벤트 3곳"
            )
        },
        description = {
            Text(
                text = "영업중",
                style = RebrandKoinTheme.typography.regular13
            )
        }
    )
}
