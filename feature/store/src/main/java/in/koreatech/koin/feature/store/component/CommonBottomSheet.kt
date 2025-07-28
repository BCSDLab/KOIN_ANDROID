package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
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
        containerColor = RebrandKoinTheme.colors.neutral0
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
                    painter = painterResource(id = R.drawable.ic_store_close),
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

            HorizontalDivider(thickness = 0.5.dp, color = RebrandKoinTheme.colors.neutral300)
            content()
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            options.forEachIndexed { idx, label ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(idx) }
                        .padding(horizontal = 32.dp),
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
}

@Composable
fun MinOrderSliderBottomSheet(
    selectedIndex: Int,
    options: List<String>,
    onSelected: (Int) -> Unit,
    onClose: () -> Unit
) {
    CommonBottomSheet(
        title = stringResource(R.string.store_bottom_sheet_min_order_title),
        onClose = onClose
    ) {
        var sliderValue by remember { mutableFloatStateOf(selectedIndex.toFloat()) }

        Spacer(modifier = Modifier.height(32.dp))

        CustomStepSlider(
            modifier = Modifier.padding(horizontal = 32.dp),
            value = sliderValue,
            onValueChange = { sliderValue = it },
            optionCount = options.size
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            options.forEach { label ->
                Text(
                    text = label,
                    style = RebrandKoinTheme.typography.medium14
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        FilledButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            text = stringResource(R.string.store_bottom_sheet_apply),
            onClick = {
                onSelected(sliderValue.toInt())
            },
            colors = ButtonDefaults.buttonColors(containerColor = RebrandKoinTheme.colors.primary500),
            shape = RebrandKoinTheme.shapes.medium,
            contentPadding = PaddingValues(vertical = 10.dp, horizontal = 16.dp),
            textStyle = RebrandKoinTheme.typography.bold18
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}
