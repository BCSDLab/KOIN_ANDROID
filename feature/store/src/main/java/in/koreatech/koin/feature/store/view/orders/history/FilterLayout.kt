package `in`.koreatech.koin.feature.store.view.orders.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.chip.TextChip
import `in`.koreatech.koin.core.designsystem.component.chip.TextChipDefaults
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.enums.OrderFilterEnum
import `in`.koreatech.koin.feature.store.model.filters

@Composable
fun FilterOverlay(
    backgroundContent: @Composable () -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        backgroundContent()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                )
        ) {
            Column(
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(32.dp))
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.order_history_filter),
                        style = RebrandKoinTheme.typography.bold18,
                        color = RebrandKoinTheme.colors.primary500
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onClose() },
                        painter = painterResource(id = R.drawable.ic_store_close),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                }
            }

            HorizontalDivider(
                thickness = 0.5.dp,
                color = RebrandKoinTheme.colors.neutral300
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                FilterSelect(
                    title = stringResource(R.string.filter_option_location),
                    value = filters.location
                )

                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    thickness = 1.dp,
                    color = RebrandKoinTheme.colors.neutral200
                )

                FilterSelect(
                    title = stringResource(R.string.filter_option_period),
                    value = filters.period
                )

                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    thickness = 1.dp,
                    color = RebrandKoinTheme.colors.neutral200
                )

                FilterSelect(
                    title = stringResource(R.string.filter_option_type),
                    value = filters.type
                )

                Spacer(modifier = Modifier.height(16.dp))

                FilterSelect(
                    title = stringResource(R.string.filter_option_status),
                    value = filters.status
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 5) 버튼 2개
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 16.dp),
                        border = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral400)
                    ) {
                        Text(
                            text = "초기화", // stringResource(R.string.write_review),
                            style = RebrandKoinTheme.typography.bold16,
                            color = RebrandKoinTheme.colors.neutral600
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(id = R.drawable.ic_process),
                            contentDescription = "",
                            tint = RebrandKoinTheme.colors.neutral500
                        )
                    }

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 64.dp),
                        colors = ButtonDefaults.buttonColors(RebrandKoinTheme.colors.primary500)
                    ) {
                        Text(
                            text = "적용하기",
                            style = RebrandKoinTheme.typography.bold16,
                            color = RebrandKoinTheme.colors.neutral0
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun <T> FilterSelect(
    title: String,
    value: T
) where T : Enum<T>, T : OrderFilterEnum {
    Text(
        text = title,
        style = RebrandKoinTheme.typography.bold14,
        color = RebrandKoinTheme.colors.neutral600
    )

    Spacer(modifier = Modifier.height(12.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        value::class.java.enumConstants
            .drop(1)
            .forEach { item ->
                val isSelected = item == value
                FilterTextChip(
                    title = stringResource(id = item.stringRes),
                    isSelected = isSelected,
                    onClick = { }
                )
            }
    }
}

@Composable
fun FilterTextChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit = {}
) {
    val modifier = if (isSelected) {
        Modifier
            .clickable { onClick() }
    } else {
        Modifier
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = RebrandKoinTheme.colors.neutral300,
                shape = CircleShape
            )
    }
    TextChip(
        title = title,
        modifier = modifier.shadow(
            elevation = 4.dp,
            shape = RebrandKoinTheme.shapes.medium,
            ambientColor = RebrandKoinTheme.colors.neutral400,
            spotColor = RebrandKoinTheme.colors.neutral500
        ),
        isSelected = isSelected,
        onSelect = {},
        chipColors = TextChipDefaults.chipColors(
            RebrandKoinTheme.colors.primary500,
            RebrandKoinTheme.colors.neutral0,
            RebrandKoinTheme.colors.neutral100,
            RebrandKoinTheme.colors.neutral500
        )
    )
}

@Preview(showBackground = true)
@Composable
fun FilterOverlayPreview() {
    FilterOverlay(
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
            )
        },
        onClose = {}
    )
}
