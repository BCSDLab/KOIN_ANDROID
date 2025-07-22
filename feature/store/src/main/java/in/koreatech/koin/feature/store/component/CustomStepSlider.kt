package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import kotlin.math.roundToInt

object CustomStepSliderDefaults {
    @Composable
    fun colors() = CustomStepSliderColors(
        activeTrackColor = RebrandKoinTheme.colors.primary500,
        inactiveTrackColor = Color(0xFFE9EBED), // TODO: Move to design system
        activePointColor = RebrandKoinTheme.colors.neutral0,
        inactivePointColor = RebrandKoinTheme.colors.neutral400
    )
}

@Immutable
class CustomStepSliderColors(
    val activeTrackColor: Color,
    val inactiveTrackColor: Color,
    val activePointColor: Color,
    val inactivePointColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomStepSlider(
    value: Float,
    optionCount: Int,
    modifier: Modifier = Modifier,
    thumbPainter: Painter = painterResource(R.drawable.ic_bcsd_symbol),
    colors: CustomStepSliderColors = CustomStepSliderDefaults.colors(),
    onValueChange: (Float) -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = 0f..(optionCount - 1).toFloat(),
        steps = optionCount - 2,
        modifier = modifier.fillMaxWidth(),
        interactionSource = interactionSource,
        thumb = {
            Icon(
                painter = thumbPainter,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.Unspecified
            )
        },
        track = { sliderState ->
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val currentPos = size.width * (sliderState.value / sliderState.valueRange.endInclusive)

                drawLine(
                    color = colors.activeTrackColor,
                    start = Offset(-24.dp.toPx(), 0f),
                    end = Offset(currentPos, size.height),
                    strokeWidth = 8.dp.toPx(),
                    cap = StrokeCap.Round
                )

                drawLine(
                    color = colors.inactiveTrackColor,
                    start = Offset(currentPos, 0f),
                    end = Offset(size.width + 24.dp.toPx(), size.height),
                    strokeWidth = 8.dp.toPx(),
                    cap = StrokeCap.Round
                )

                (sliderState.valueRange.start.roundToInt()..sliderState.value.roundToInt()).forEach {
                    drawCircle(
                        color = colors.activePointColor,
                        radius = 2.dp.toPx(),
                        center = Offset(size.width * (it / (sliderState.steps + 1).toFloat()), size.height)
                    )
                }

                (sliderState.value.roundToInt()..sliderState.valueRange.endInclusive.roundToInt()).forEach {
                    drawCircle(
                        color = colors.inactivePointColor,
                        radius = 2.dp.toPx(),
                        center = Offset(size.width * (it / (sliderState.steps + 1).toFloat()), size.height)
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CustomStepSliderPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomStepSlider(
            value = 1f,
            optionCount = 5
        )
    }
}
