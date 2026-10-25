package `in`.koreatech.koin.feature.store.cart.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun PaymentSummaryCard(
    modifier: Modifier = Modifier,
    itemAmount: Int,
    deliveryFee: Int,
    totalAmount: Int,
    finalPaymentAmount: Int
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.check_payment_amount),
            style = KoinTheme.typography.bold16
        )
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RebrandKoinTheme.shapes.small,
            elevation = CardDefaults.cardElevation(1.dp),
            colors = CardDefaults.cardColors(containerColor = RebrandKoinTheme.colors.neutral0)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.total_amount),
                        style = RebrandKoinTheme.typography.medium15.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = stringResource(R.string.price_with_won, totalAmount),
                        style = RebrandKoinTheme.typography.medium15.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.menu_amount),
                        style = RebrandKoinTheme.typography.regular13.copy(
                            color = RebrandKoinTheme.colors.neutral500
                        )
                    )
                    Text(
                        text = stringResource(R.string.price_with_won, itemAmount),
                        style = RebrandKoinTheme.typography.regular13.copy(
                            color = RebrandKoinTheme.colors.neutral500
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.delivery_amount),
                        style = RebrandKoinTheme.typography.regular13.copy(
                            color = RebrandKoinTheme.colors.neutral500
                        )
                    )
                    Text(
                        text = stringResource(R.string.price_with_won, deliveryFee),
                        style = RebrandKoinTheme.typography.regular13.copy(
                            color = RebrandKoinTheme.colors.neutral500
                        )
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.expected_payment_amount),
                        style = RebrandKoinTheme.typography.medium16.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = stringResource(R.string.price_with_won, finalPaymentAmount),
                        style = RebrandKoinTheme.typography.medium16.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PaymentSummaryCardPreview() {
    KoinTheme {
        PaymentSummaryCard(
            itemAmount = 20000,
            deliveryFee = 3000,
            totalAmount = 23000,
            finalPaymentAmount = 23000
        )
    }
}
