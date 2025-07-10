package `in`.koreatech.koin.ui.store.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlin.math.roundToInt

@Composable
fun MinOrderSlider(
    minOrderOptions: List<String>,
    minOrderValues: List<Int>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    onSelectedIndexChange: (Int) -> Unit = {}
) {
    var sliderPosition by remember(selectedIndex) { mutableFloatStateOf(selectedIndex.toFloat()) }

    Column(modifier = modifier) {
        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
            },
            valueRange = 0f..(minOrderOptions.size - 1).toFloat(),
            steps = minOrderOptions.size - 2,
            onValueChangeFinished = {
                onSelectedIndexChange(sliderPosition.roundToInt())
            }
        )
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            minOrderOptions.forEachIndexed { idx, label ->
                Text(label, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
