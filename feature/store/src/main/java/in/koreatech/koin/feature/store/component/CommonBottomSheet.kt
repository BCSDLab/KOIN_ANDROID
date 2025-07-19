package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonBottomSheet(
    title: String,
    onClose: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = onClose,
        dragHandle = null,
        containerColor = Color(0xFFE1E1E1)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 24.dp, top = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = RebrandKoinTheme.typography.bold18,
                    color = RebrandKoinTheme.colors.primary500,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = stringResource(R.string.store_bottom_sheet_close),
                    tint = RebrandKoinTheme.colors.neutral800,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onClose
                        )
                )
            }
            Column(
                modifier = Modifier
                    .border(width = 0.5.dp, color = RebrandKoinTheme.colors.neutral300)
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp, top = 4.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SortBottomSheet(
    currentIndex: Int,
    options: List<String>,
    onSelect: (Int) -> Unit,
    onClose: () -> Unit
) {
    CommonBottomSheet(title = stringResource(R.string.store_bottom_sheet_sort_title), onClose = onClose) {
        options.forEachIndexed { idx, label ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(idx) }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    color = if (currentIndex == idx) RebrandKoinTheme.colors.primary500 else RebrandKoinTheme.colors.neutral800,
                    style = if (currentIndex == idx) RebrandKoinTheme.typography.regular16 else RebrandKoinTheme.typography.regular16,
                    modifier = Modifier.weight(1f)
                )
                if (currentIndex == idx) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check),
                        contentDescription = null,
                        tint = RebrandKoinTheme.colors.primary500
                    )
                }
            }
        }
    }
}

@Composable
fun MinOrderSliderBottomSheet(
    selectedIndex: Int,
    options: List<String>,
    onSelected: (Int) -> Unit,
    onApply: () -> Unit,
    onClose: () -> Unit
) {
    CommonBottomSheet(
        title = stringResource(R.string.store_bottom_sheet_min_order_title),
        onClose = onClose
    ) {
        var sliderValue by remember { mutableFloatStateOf(selectedIndex.toFloat()) }

        val thumbPainter = painterResource(id = R.drawable.ic_bcsd_symbol)
        val activeColor = RebrandKoinTheme.colors.primary500
        val inactiveColor = RebrandKoinTheme.colors.neutral200

        CustomStepSlider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            optionCount = options.size,
            thumbPainter = thumbPainter,
            activeColor = activeColor,
            inactiveColor = inactiveColor
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEachIndexed { idx, label ->
                Text(
                    text = label,
                    color = if (idx == sliderValue.toInt()) activeColor else RebrandKoinTheme.colors.neutral400,
                    style = if (idx == sliderValue.toInt()) RebrandKoinTheme.typography.bold14 else RebrandKoinTheme.typography.regular14
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                onSelected(sliderValue.toInt())
                onApply()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = RebrandKoinTheme.colors.primary500)
        ) {
            Text(stringResource(R.string.store_bottom_sheet_apply), color = RebrandKoinTheme.colors.neutral0)
        }
    }
}
