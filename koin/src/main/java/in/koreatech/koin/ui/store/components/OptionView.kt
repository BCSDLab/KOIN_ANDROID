package `in`.koreatech.koin.ui.store.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun OptionView(option: MenuOption) {
    Column(
        modifier = Modifier
            .border(width = 1.dp, color = RebrandKoinTheme.colors.neutral300, shape = RebrandKoinTheme.shapes.medium)
            .background(color = RebrandKoinTheme.colors.neutral0, shape = RebrandKoinTheme.shapes.medium)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = option.name,
                    style = RebrandKoinTheme.typography.bold18,
                    color = RebrandKoinTheme.colors.neutral800
                )
                if (option.description.isNotEmpty()) {
                    Text(
                        text = option.description,
                        style = RebrandKoinTheme.typography.regular12,
                        color = RebrandKoinTheme.colors.neutral500
                    )
                }
            }
            OptionGuideChip(
                label = if (option.selectCount > 1)
                    stringResource(R.string.menu_detail_option_select_count, option.selectCount)
                else
                    stringResource(R.string.menu_detail_option_required),
                modifier = Modifier,
                backgroundColor = RebrandKoinTheme.colors.neutral0,
                contentColor = RebrandKoinTheme.colors.primary300,
                borderColor = RebrandKoinTheme.colors.primary300,
                textColor = RebrandKoinTheme.colors.primary300
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            when (option.selectionType) {
                SelectionType.RADIO -> {
                    var selectedIndex by remember { mutableIntStateOf(0) }
                    option.items.forEachIndexed { idx, item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            CustomRadioButton(
                                selected = selectedIndex == idx,
                                onClick = { selectedIndex = idx }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.name,
                                style = RebrandKoinTheme.typography.regular16,
                                color = RebrandKoinTheme.colors.neutral800,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = stringResource(R.string.menu_detail_option_price, item.price),
                                style = RebrandKoinTheme.typography.bold16,
                                color = RebrandKoinTheme.colors.neutral800,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                SelectionType.CHECKBOX -> {
                    var checkedState = remember { mutableStateListOf(*Array(option.items.size) { false }) }
                    option.items.forEachIndexed { idx, item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            CompositionLocalProvider(
                                LocalMinimumInteractiveComponentSize provides Dp.Unspecified
                            ) {
                                Checkbox(
                                    checked = checkedState[idx],
                                    onCheckedChange = { checkedState[idx] = it }
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.name,
                                style = RebrandKoinTheme.typography.regular16,
                                color = RebrandKoinTheme.colors.neutral800,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "+${stringResource(R.string.menu_detail_option_price, item.price)}",
                                style = RebrandKoinTheme.typography.bold16,
                                color = RebrandKoinTheme.colors.neutral800,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun OptionViewPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        val menuOption = MenuOption(
            name = "추가 토핑",
            description = "원하는 토핑을 추가하세요.",
            selectionType = SelectionType.RADIO,
            required = false,
            selectCount = 1,
            items = listOf(
                OptionItem("치즈", 2000),
                OptionItem("베이컨", 1500)
            )
        )
        OptionView(menuOption)

        val menuOption1 = MenuOption(
            name = "추가 토핑",
            description = "원하는 토핑을 추가하세요.",
            selectionType = SelectionType.CHECKBOX,
            required = false,
            selectCount = 3,
            items = listOf(
                OptionItem("치즈", 2000),
                OptionItem("베이컨", 1500)
            )
        )
        OptionView(menuOption1)
    }
}
