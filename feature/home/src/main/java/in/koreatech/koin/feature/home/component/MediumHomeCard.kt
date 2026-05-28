package `in`.koreatech.koin.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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

object MediumHomeCardDefaults {
    val Shape: Shape = RoundedCornerShape(16.dp)
    val BorderWidth: Dp = 0.5.dp
    val ContentPadding = PaddingValues(vertical = 20.dp, horizontal = 20.dp)

    @Composable
    fun colors(
        backgroundColor: Color = Color(0xFF1F1F1F),
        borderColor: Color = Color(0xFFE6E6E6)
    ): MediumHomeCardColors = MediumHomeCardColors(
        backgroundColor = backgroundColor,
        borderColor = borderColor
    )
}

@Immutable
data class MediumHomeCardColors(
    val backgroundColor: Color,
    val borderColor: Color
)

@Composable
fun MediumHomeCard(
    title: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    label: (@Composable () -> Unit)? = null,
    description: (@Composable () -> Unit)? = null,
    actionButton: (@Composable () -> Unit)? = null,
    colors: MediumHomeCardColors = MediumHomeCardDefaults.colors(),
    shape: Shape = MediumHomeCardDefaults.Shape,
    contentPadding: PaddingValues = MediumHomeCardDefaults.ContentPadding,
    borderWidth: Dp = MediumHomeCardDefaults.BorderWidth
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

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                title()

                Spacer(modifier = Modifier.width(8.dp))

                if (label != null) {
                    label()
                }
            }
            if (description != null) {
                description()
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (actionButton != null) {
            actionButton()
        }
    }
}

@Preview
@Composable
private fun MediumHomeCardPreview() {
    MediumHomeCard(
        title = {
            Text(
                text = "콜밴팟",
                style = RebrandKoinTheme.typography.medium15,
                color = Color.White
            )
        },
        icon = {
            HomeIcon(
                tint = Color(0xFFFFFFFF),
                backgroundColor = Color(0xFFB611F5),
                imageVector = ImageVector.vectorResource(R.drawable.ic_home_bus_timetable),
                contentDescription = null
            )
        },
        label = {
            HomeChip(
                text = "14건 모집중",
                colors = HomeChipDefaults.colors(
                    textColor = Color(0xFF980AC9),
                    backgroundColor = Color(0xFFFAFAFA)
                )
            )
        },
        description = {
            Text(
                text = "같이 콜벤 탈 사람을 찾아요",
                style = RebrandKoinTheme.typography.regular12,
                color = Color(0xFFA8A8A8)
            )
        },
        actionButton = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFB611F5))
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .clickable {},
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "모집 보기",
                    color = Color(0xFFFAFAFA),
                    style = RebrandKoinTheme.typography.medium13
                )
            }
        }
    )
}
