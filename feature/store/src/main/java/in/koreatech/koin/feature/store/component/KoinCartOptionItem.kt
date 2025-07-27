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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
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
import `in`.koreatech.koin.feature.store.model.LocalShopMenuOptionGroup

@Composable
fun KoinCartOptionItem(
    options: List<LocalShopMenuOptionGroup.LocalShopMenuOption>,
    title: String,
    description: String,
    selectedId: List<Int>,
    modifier: Modifier = Modifier,
    requiredSelectCount: Int = 0,
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

            if (requiredSelectCount > 0) {
                Box(
                    modifier = modifier
                        .border(1.dp, RebrandKoinTheme.colors.primary500, RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .padding(vertical = 2.dp, horizontal = 8.dp)
                ) {
                    BasicText(
                        modifier = Modifier,
                        text = stringResource(R.string.store_cart_min_select, requiredSelectCount),
                        style = RebrandKoinTheme.typography.regular12.copy(color = RebrandKoinTheme.colors.primary500)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            options.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .clickable {
                            onClick(option.id)
                        }
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                        .then(
                            if (index == options.lastIndex) {
                                Modifier.padding(bottom = 6.dp)
                            } else {
                                Modifier
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedId.contains(option.id),
                        onCheckedChange = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    BasicText(
                        text = option.name,
                        style = RebrandKoinTheme.typography.regular16
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    BasicText(
                        text = stringResource(R.string.plus_won, option.price),
                        style = RebrandKoinTheme.typography.bold16
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KoinCartOptionItemPreview() {
    RebrandKoinTheme {
        KoinCartOptionItem(
            options = listOf(
                LocalShopMenuOptionGroup.LocalShopMenuOption(
                    id = 1,
                    name = "옵션 1",
                    price = 1000,
                    optionSelected = true
                ),
                LocalShopMenuOptionGroup.LocalShopMenuOption(
                    id = 2,
                    name = "옵션 2",
                    price = 2000,
                    optionSelected = false
                ),
                LocalShopMenuOptionGroup.LocalShopMenuOption(
                    id = 3,
                    name = "옵션 3",
                    price = 3000,
                    optionSelected = false
                )
            ),
            description = "옵션을 선택해주세요.",
            title = "옵션",
            selectedId = listOf(1)
        )
    }
}
