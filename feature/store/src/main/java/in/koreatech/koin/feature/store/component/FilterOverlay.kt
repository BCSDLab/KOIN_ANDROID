package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.enums.LocationOption
import `in`.koreatech.koin.feature.store.enums.PeriodOption
import `in`.koreatech.koin.feature.store.enums.StatusOption
import `in`.koreatech.koin.feature.store.enums.TypeOption
import `in`.koreatech.koin.feature.store.model.OrderFilter

@Composable
fun FilterOverlay(
    filters: OrderFilter,
    modifier: Modifier = Modifier,
    onClose: () -> Unit = {},
    onApply: (OrderFilter) -> Unit = {}
) {
    var localFilters by remember { mutableStateOf(filters) }

    Popup(
        alignment = Alignment.TopStart,
        onDismissRequest = { onClose() }
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(RebrandKoinTheme.colors.neutral800.copy(alpha = 0.7f))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        color = RebrandKoinTheme.colors.neutral0,
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                    )
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_store_close),
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
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    FilterTextChipSelect(
                        title = stringResource(R.string.filter_option_location),
                        value = localFilters.location,
                        onValueChange = { newValue ->
                            localFilters = localFilters.copy(location = newValue)
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = RebrandKoinTheme.colors.neutral200
                    )

                    FilterTextChipSelect(
                        title = stringResource(R.string.filter_option_period),
                        value = localFilters.period,
                        onValueChange = { newValue ->
                            localFilters = localFilters.copy(period = newValue)
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = RebrandKoinTheme.colors.neutral200
                    )

                    FilterTextChipSelect(
                        title = stringResource(R.string.filter_option_type),
                        value = localFilters.type,
                        onValueChange = { newValue ->
                            localFilters = localFilters.copy(type = newValue)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FilterTextChipSelect(
                        title = stringResource(R.string.filter_option_status),
                        value = localFilters.status,
                        onValueChange = { newValue ->
                            localFilters = localFilters.copy(status = newValue)
                        }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                localFilters = OrderFilter(
                                    location = LocationOption.DEFAULT,
                                    period = PeriodOption.DEFAULT,
                                    type = TypeOption.DEFAULT,
                                    status = StatusOption.DEFAULT
                                )
                            },
                            shape = RebrandKoinTheme.shapes.small,
                            contentPadding = PaddingValues(vertical = 10.dp, horizontal = 18.dp),
                            border = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral400)
                        ) {
                            Text(
                                text = stringResource(R.string.orders_chip_reset),
                                style = RebrandKoinTheme.typography.bold16,
                                color = RebrandKoinTheme.colors.neutral600
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_process),
                                contentDescription = "",
                                tint = RebrandKoinTheme.colors.neutral500
                            )
                        }

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onApply(localFilters)
                                onClose()
                            },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(vertical = 10.dp, horizontal = 64.dp),
                            colors = ButtonDefaults.buttonColors(RebrandKoinTheme.colors.primary500)
                        ) {
                            Text(
                                text = stringResource(R.string.orders_chip_apply),
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
}

@Preview(showBackground = true)
@Composable
private fun FilterOverlayPreview() {
    FilterOverlay(
        OrderFilter(
            location = LocationOption.DEFAULT,
            period = PeriodOption.DEFAULT,
            type = TypeOption.DEFAULT,
            status = StatusOption.DEFAULT
        )
    )
}
