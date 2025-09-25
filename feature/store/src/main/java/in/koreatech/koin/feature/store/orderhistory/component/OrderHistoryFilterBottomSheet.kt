package `in`.koreatech.koin.feature.store.orderhistory.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.CommonBottomSheet
import `in`.koreatech.koin.feature.store.component.KoinStoreChip
import `in`.koreatech.koin.feature.store.component.KoinStoreChipDefaults
import `in`.koreatech.koin.feature.store.model.OrderType
import `in`.koreatech.koin.feature.store.orderhistory.enums.OrderHistoryPeriod
import `in`.koreatech.koin.feature.store.orderhistory.enums.OrderStatusFilter
import `in`.koreatech.koin.feature.store.orderhistory.model.StoreOrderHistoryFilters

@Composable
fun OrderHistoryFilterBottomSheet(
    filters: StoreOrderHistoryFilters,
    updateFilters: (StoreOrderHistoryFilters) -> Unit,
    onClose: () -> Unit
) {
    var temporaryFilters by remember { mutableStateOf(filters) }

    CommonBottomSheet(
        title = stringResource(R.string.order_history_filter),
        skipPartiallyExpanded = true,
        onClose = onClose
    ) {
        Column(
            modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 16.dp, bottom = 12.dp)
        ) {
            BasicText(
                text = stringResource(R.string.order_history_period_none),
                style = RebrandKoinTheme.typography.medium16.merge(
                    color = RebrandKoinTheme.colors.neutral600
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OrderHistoryPeriod.entries.forEach {
                    KoinStoreChip(
                        text = stringResource(it.stringRes),
                        chipStyle = KoinStoreChipDefaults.koinStoreChipStyle(
                            borderWidth = if (it != temporaryFilters.orderHistoryPeriod) 0.5.dp else 0.dp,
                            borderColor = RebrandKoinTheme.colors.neutral300,
                            elevation = 2.dp,
                            containerColor = if (it != temporaryFilters.orderHistoryPeriod) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.primary500,
                            textColor = if (it != temporaryFilters.orderHistoryPeriod) RebrandKoinTheme.colors.neutral500 else RebrandKoinTheme.colors.neutral0,
                            paddingValues = PaddingValues(vertical = 6.dp, horizontal = 12.dp)
                        ),
                        onClick = {
                            temporaryFilters = temporaryFilters.copy(orderHistoryPeriod = if (temporaryFilters.orderHistoryPeriod == it) null else it)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = RebrandKoinTheme.colors.neutral200)
            Spacer(modifier = Modifier.height(24.dp))

            BasicText(
                text = stringResource(R.string.order_type_none),
                style = RebrandKoinTheme.typography.medium16.merge(
                    color = RebrandKoinTheme.colors.neutral600
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OrderType.entries.forEach {
                    KoinStoreChip(
                        text = stringResource(it.stringRes),
                        chipStyle = KoinStoreChipDefaults.koinStoreChipStyle(
                            borderWidth = if (it != temporaryFilters.orderType) 0.5.dp else 0.dp,
                            borderColor = RebrandKoinTheme.colors.neutral300,
                            elevation = 2.dp,
                            containerColor = if (it != temporaryFilters.orderType) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.primary500,
                            textColor = if (it != temporaryFilters.orderType) RebrandKoinTheme.colors.neutral500 else RebrandKoinTheme.colors.neutral0,
                            paddingValues = PaddingValues(vertical = 6.dp, horizontal = 12.dp)
                        ),
                        onClick = {
                            temporaryFilters = temporaryFilters.copy(orderType = if (temporaryFilters.orderType == it) null else it)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            BasicText(
                text = stringResource(R.string.order_status_none),
                style = RebrandKoinTheme.typography.medium16.merge(
                    color = RebrandKoinTheme.colors.neutral600
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OrderStatusFilter.entries.forEach {
                    KoinStoreChip(
                        text = stringResource(it.stringRes),
                        chipStyle = KoinStoreChipDefaults.koinStoreChipStyle(
                            borderWidth = if (it != temporaryFilters.orderStatusFilter) 0.5.dp else 0.dp,
                            borderColor = RebrandKoinTheme.colors.neutral300,
                            elevation = 2.dp,
                            containerColor = if (it != temporaryFilters.orderStatusFilter) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.primary500,
                            textColor = if (it != temporaryFilters.orderStatusFilter) RebrandKoinTheme.colors.neutral500 else RebrandKoinTheme.colors.neutral0,
                            paddingValues = PaddingValues(vertical = 6.dp, horizontal = 12.dp)
                        ),
                        onClick = {
                            temporaryFilters = temporaryFilters.copy(orderStatusFilter = if (temporaryFilters.orderStatusFilter == it) null else it)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        temporaryFilters = StoreOrderHistoryFilters()
                    },
                    shape = RebrandKoinTheme.shapes.small,
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 18.dp),
                    border = BorderStroke(1.dp, RebrandKoinTheme.colors.neutral400)
                ) {
                    BasicText(
                        text = stringResource(R.string.orders_chip_reset),
                        style = RebrandKoinTheme.typography.medium16.merge(
                            color = RebrandKoinTheme.colors.neutral600
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_process),
                        contentDescription = null,
                        tint = RebrandKoinTheme.colors.neutral500
                    )
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        updateFilters(temporaryFilters)
                        onClose()
                    },
                    shape = RebrandKoinTheme.shapes.small,
                    contentPadding = PaddingValues(vertical = 10.dp, horizontal = 64.dp),
                    colors = ButtonDefaults.buttonColors(RebrandKoinTheme.colors.primary500)
                ) {
                    BasicText(
                        text = stringResource(R.string.orders_chip_apply),
                        style = RebrandKoinTheme.typography.medium16.merge(
                            color = RebrandKoinTheme.colors.neutral0
                        )
                    )
                }
            }
        }
    }
}
