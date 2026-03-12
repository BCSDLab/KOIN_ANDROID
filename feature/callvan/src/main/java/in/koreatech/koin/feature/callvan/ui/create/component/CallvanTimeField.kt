package `in`.koreatech.koin.feature.callvan.ui.create.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CallvanTimeField(
    formattedTime: String,
    isPickerVisible: Boolean,
    isAm: Boolean,
    selectedHour: Int,
    selectedMinute: Int,
    onFieldClick: () -> Unit = {},
    onAmPmIndexChange: (Int) -> Unit = {},
    onHourIndexChange: (Int) -> Unit = {},
    onMinuteIndexChange: (Int) -> Unit = {},
    onReset: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    val amPmText = stringResource(if (isAm) R.string.callvan_am else R.string.callvan_pm)

    Column(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CallvanSectionHeader(
                label = stringResource(R.string.callvan_create_time_label),
                hint = stringResource(R.string.callvan_create_time_hint)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, RebrandKoinTheme.colors.neutral400, RoundedCornerShape(4.dp))
                    .clickable(onClick = onFieldClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = amPmText,
                    style = RebrandKoinTheme.typography.regular14,
                    color = RebrandKoinTheme.colors.neutral800,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                VerticalDivider(
                    modifier = Modifier.height(38.dp),
                    color = RebrandKoinTheme.colors.neutral400
                )
                Text(
                    text = formattedTime,
                    style = RebrandKoinTheme.typography.regular14,
                    color = RebrandKoinTheme.colors.neutral800,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
        AnimatedVisibility(
            visible = isPickerVisible,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            CallvanTimePickerCard(
                isAm = isAm,
                selectedHour = selectedHour,
                selectedMinute = selectedMinute,
                onAmPmIndexChange = onAmPmIndexChange,
                onHourIndexChange = onHourIndexChange,
                onMinuteIndexChange = onMinuteIndexChange,
                onReset = onReset,
                onConfirm = onConfirm
            )
        }
    }
}

@Suppress("LongParameterList")
@Composable
private fun CallvanTimePickerCard(
    isAm: Boolean,
    selectedHour: Int,
    selectedMinute: Int,
    onAmPmIndexChange: (Int) -> Unit,
    onHourIndexChange: (Int) -> Unit,
    onMinuteIndexChange: (Int) -> Unit,
    onReset: () -> Unit,
    onConfirm: () -> Unit
) {
    val amLabel = stringResource(R.string.callvan_am)
    val pmLabel = stringResource(R.string.callvan_pm)
    val amPmItems = remember(amLabel, pmLabel) {
        listOf(amLabel, pmLabel).toImmutableList()
    }
    val hourItems = remember { (1..12).map { it.toString() }.toImmutableList() }
    val minuteItems = remember { (0..59).map { "%02d".format(it) }.toImmutableList() }

    val amPmIndex = if (isAm) 0 else 1
    val hourIndex = (selectedHour - 1).coerceIn(0, 11)
    val minuteIndex = selectedMinute.coerceIn(0, 59)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = RebrandKoinTheme.colors.neutral100),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                CallvanScrollPicker(
                    items = amPmItems,
                    selectedIndex = amPmIndex,
                    onIndexChange = onAmPmIndexChange,
                    modifier = Modifier.weight(1f)
                )
                CallvanScrollPicker(
                    items = hourItems,
                    selectedIndex = hourIndex,
                    onIndexChange = onHourIndexChange,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
                CallvanScrollPicker(
                    items = minuteItems,
                    selectedIndex = minuteIndex,
                    onIndexChange = onMinuteIndexChange,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
            HorizontalDivider(color = RebrandKoinTheme.colors.neutral200)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)
            ) {
                Text(
                    text = stringResource(R.string.callvan_create_picker_reset),
                    style = RebrandKoinTheme.typography.medium14,
                    color = RebrandKoinTheme.colors.primary500,
                    modifier = Modifier.clickable(onClick = onReset)
                )
                Text(
                    text = stringResource(R.string.callvan_create_picker_confirm),
                    style = RebrandKoinTheme.typography.medium14,
                    color = RebrandKoinTheme.colors.primary500,
                    modifier = Modifier.clickable(onClick = onConfirm)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanTimeFieldPreview() {
    CallvanTimeField(
        formattedTime = "09:30",
        isPickerVisible = false,
        isAm = true,
        selectedHour = 9,
        selectedMinute = 30,
        onFieldClick = {},
        onAmPmIndexChange = {},
        onHourIndexChange = {},
        onMinuteIndexChange = {},
        onReset = {},
        onConfirm = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanTimeFieldPickerVisiblePreview() {
    CallvanTimeField(
        formattedTime = "02:45",
        isPickerVisible = true,
        isAm = false,
        selectedHour = 2,
        selectedMinute = 45,
        onFieldClick = {},
        onAmPmIndexChange = {},
        onHourIndexChange = {},
        onMinuteIndexChange = {},
        onReset = {},
        onConfirm = {}
    )
}
