package `in`.koreatech.koin.ui.store.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomStepSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    optionCount: Int,
    thumbPainter: Painter,
    activeColor: Color,
    inactiveColor: Color
) {
    val interactionSource = remember { MutableInteractionSource() }
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = 0f..(optionCount - 1).toFloat(),
        steps = optionCount - 2,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp),
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
            val fraction = (sliderState.value - sliderState.valueRange.start) /
                    (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
            Box(Modifier.fillMaxWidth().height(6.dp)) {
                Box(
                    Modifier
                        .fillMaxWidth(fraction)
                        .align(Alignment.CenterStart)
                        .height(6.dp)
                        .background(activeColor, CircleShape)
                )
                Box(
                    Modifier
                        .fillMaxWidth(1f - fraction)
                        .align(Alignment.CenterEnd)
                        .height(6.dp)
                        .background(inactiveColor, CircleShape)
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun CustomStepSliderPreview() {
    val activeColor = Color(0xFF4CAF50)
    val inactiveColor = Color(0xFFBDBDBD)
    val optionCount = 5

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomStepSlider(
            value = 1f,
            onValueChange = {},
            optionCount = optionCount,
            thumbPainter = painterResource(R.drawable.ic_bcsd_symbol),
            activeColor = activeColor,
            inactiveColor = inactiveColor
        )
    }
}