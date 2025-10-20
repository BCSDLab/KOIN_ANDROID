package `in`.koreatech.koin.feature.store.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun KoinOrdersResetChip(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    KoinStoreChip(
        modifier = modifier,
        text = stringResource(R.string.orders_chip_reset),
        chipStyle = KoinStoreChipDefaults.koinStoreChipStyle(
            elevation = 4.dp,
            containerColor = RebrandKoinTheme.colors.neutral0,
            textColor = RebrandKoinTheme.colors.neutral500
        ),
        trailingIcon = painterResource(R.drawable.ic_process),
        trailingIconStyle = KoinStoreChipDefaults.koinStoreIconStyle(
            iconColor = RebrandKoinTheme.colors.neutral500
        ),
        onClick = onClick
    )
}

@Preview(showBackground = true)
@Composable
private fun KoinOrdersResetChipPreview() {
    KoinOrdersResetChip()
}
