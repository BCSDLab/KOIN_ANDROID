package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.domain.model.store.CartItemEdit.CartItemEditOptionGroup
import `in`.koreatech.koin.domain.model.store.CartItemEdit.CartItemEditPrice
import `in`.koreatech.koin.feature.store.R

@Composable
fun MenuOptionCard(
    shopMenuOption: CartItemEditOptionGroup,
    modifier: Modifier = Modifier,
    onChangeOption: (Int, Int) -> Unit = { _, _ -> },
) {
    Surface(
        shape = RebrandKoinTheme.shapes.medium,
        tonalElevation = 2.dp,
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = RebrandKoinTheme.colors.neutral300,
                shape = RebrandKoinTheme.shapes.medium
            ),

        color = RebrandKoinTheme.colors.neutral0
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = shopMenuOption.name, style = RebrandKoinTheme.typography.bold18)

                shopMenuOption.let { optionGroup ->
                    Box(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = colorResource(R.color.menu_option_chip),
                                shape = RebrandKoinTheme.shapes.large
                            )
                            .background(color = RebrandKoinTheme.colors.neutral0, shape = RebrandKoinTheme.shapes.large)
                            .padding(horizontal = 12.dp, vertical = 4.dp)

                    ) {
                        Text(
                            text = stringResource(R.string.option_select, optionGroup.maxSelect),
                            color = colorResource(R.color.menu_option_chip),
                            style = RebrandKoinTheme.typography.regular12,
                        )
                    }
                }
            }
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = shopMenuOption.description,
                style = RebrandKoinTheme.typography.regular12,
            )

            shopMenuOption.options.forEach { (optionId, label, price, isSelected) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onChangeOption(
                                shopMenuOption.id,
                                optionId
                            )
                        }
                        .padding(vertical = 10.dp)
                ) {
                    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = {
                                onChangeOption(
                                    shopMenuOption.id,
                                    optionId
                                )
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = RebrandKoinTheme.colors.primary500,
                                uncheckedColor = RebrandKoinTheme.colors.neutral500
                            )
                        )
                    }
                    Text(
                        text = label ?: "",
                        style = RebrandKoinTheme.typography.regular14,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    )

                    if (price > 0) {
                        Text(
                            text = stringResource(R.string.plus_won, price),
                            style = RebrandKoinTheme.typography.bold14,
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun MenuPriceOptionCard(
    prices: List<CartItemEditPrice>,
    modifier: Modifier = Modifier,
    onChangeOption: (Int) -> Unit = { _ -> },
) {
    Surface(
        shape = RebrandKoinTheme.shapes.medium,
        tonalElevation = 2.dp,
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = RebrandKoinTheme.colors.neutral300,
                shape = RebrandKoinTheme.shapes.medium
            ),

        color = RebrandKoinTheme.colors.neutral0
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.price_option), style = RebrandKoinTheme.typography.bold18)

                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = colorResource(R.color.menu_option_chip),
                            shape = RebrandKoinTheme.shapes.large
                        )
                        .background(color = RebrandKoinTheme.colors.neutral0, shape = RebrandKoinTheme.shapes.large)
                        .padding(horizontal = 12.dp, vertical = 4.dp)

                ) {
                    Text(
                        text = stringResource(R.string.essential),
                        color = colorResource(R.color.menu_option_chip),
                        style = RebrandKoinTheme.typography.regular12,
                    )
                }
            }

            prices.forEach { (id, name, price, isSelected) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onChangeOption(
                                id,
                            )
                        }
                        .padding(vertical = 10.dp)
                ) {
                    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = {
                                onChangeOption(
                                    id
                                )
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = RebrandKoinTheme.colors.primary500,
                                uncheckedColor = RebrandKoinTheme.colors.neutral500
                            )
                        )
                    }
                    Text(
                        text = name ?: "",
                        style = RebrandKoinTheme.typography.regular14,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    )

                    if (price > 0) {
                        Text(
                            text = stringResource(R.string.plus_won, price),
                            style = RebrandKoinTheme.typography.bold14,
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun MenuOptionCardPreview() {

    RebrandKoinTheme {
        val fakeCartItemEditOptionGroups = listOf(
            CartItemEditOptionGroup(
                id = 1,
                name = "맛 선택",
                description = "최대 2가지 선택",
                isRequired = true,
                minSelect = 1,
                maxSelect = 2,
                options = listOf(
                    CartItemEditPrice(
                        id = 101,
                        name = "매운맛",
                        price = 4500,
                        isSelected = true
                    ),
                    CartItemEditPrice(
                        id = 102,
                        name = "맛있는맛",
                        price = 0,
                        isSelected = false
                    ),
                    CartItemEditPrice(
                        id = 103,
                        name = "더 맛있는맛",
                        price = 3500,
                        isSelected = false
                    )
                )
            ),
            CartItemEditOptionGroup(
                id = 2,
                name = "사이드 선택",
                description = "1가지 필수 선택",
                isRequired = true,
                minSelect = 1,
                maxSelect = 1,
                options = listOf(
                    CartItemEditPrice(
                        id = 201,
                        name = "치즈볼",
                        price = 3000,
                        isSelected = false
                    ),
                    CartItemEditPrice(
                        id = 202,
                        name = "타코야끼",
                        price = 4000,
                        isSelected = true
                    )
                )
            )
        )


    }
}