package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.model.LocalShopPrice

@Composable
fun KoinCartPriceItem(
    prices: List<LocalShopPrice>,
    title: String,
    description: String,
    selectedId: Int,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit = {}
) {
    Column(
        modifier = modifier
            .clip(shape = RebrandKoinTheme.shapes.medium)
            .border(width = 1.dp, color = RebrandKoinTheme.colors.neutral300, shape = RebrandKoinTheme.shapes.medium)
            .background(RebrandKoinTheme.colors.neutral0)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                BasicText(
                    text = title,
                    style = RebrandKoinTheme.typography.bold18
                )
                if (description.isNotEmpty()) {
                    BasicText(
                        text = description,
                        style = RebrandKoinTheme.typography.regular12.copy(
                            color = RebrandKoinTheme.colors.neutral500
                        )
                    )
                }
            }

            Box(
                modifier = modifier
                    .border(1.dp, RebrandKoinTheme.colors.primary500, RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .padding(vertical = 2.dp, horizontal = 8.dp)
            ) {
                BasicText(
                    modifier = Modifier,
                    text = stringResource(R.string.essential),
                    style = RebrandKoinTheme.typography.regular12.copy(color = RebrandKoinTheme.colors.primary500)
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            prices.forEachIndexed { index, price ->
                Row(
                    modifier = Modifier
                        .clickable {
                            onClick(price.id)
                        }
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                        .then(
                            if (index == prices.lastIndex) {
                                Modifier.padding(bottom = 6.dp)
                            } else {
                                Modifier
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedId == price.id,
                        onClick = { onClick(price.id) }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    BasicText(
                        text = price.name ?: "",
                        style = RebrandKoinTheme.typography.regular16
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    BasicText(
                        text = stringResource(R.string.price_with_won, price.price),
                        style = RebrandKoinTheme.typography.bold16
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KoinCartPriceItemPreview() {
    KoinCartPriceItem(
        prices = listOf(
            LocalShopPrice(
                id = 1,
                price = 5000,
                name = "가격 1"
            ),
            LocalShopPrice(
                id = 2,
                price = 6000,
                name = "가격 2"
            ),
            LocalShopPrice(
                id = 3,
                price = 7000,
                name = "가격 3"
            )
        ),
        selectedId = 0,
        title = "가격",
        description = "가격을 선택해주세요"
    )
}
