package `in`.koreatech.koin.feature.store.orders.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.enums.StoreStatus

@Composable
fun MenuAddButton(
    status: StoreStatus,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val buttonText =
        when (status) {
            StoreStatus.PRE_OPEN -> stringResource(R.string.add_same_menu_pre_open)
            StoreStatus.SOLD_OUT -> stringResource(R.string.add_same_menu_sold_out)
            else -> stringResource(R.string.add_same_menu_open)
        }

    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RebrandKoinTheme.shapes.small,
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = RebrandKoinTheme.colors.primary500,
            disabledContainerColor = RebrandKoinTheme.colors.neutral200
        ),
        enabled = status.isButtonEnabled
    ) {
        Text(
            text = buttonText,
            style = RebrandKoinTheme.typography.bold14,
            color = RebrandKoinTheme.colors.neutral0
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuAddButtonOPENPreview() {
    MenuAddButton(
        StoreStatus.OPEN
    )
}

@Preview(showBackground = true)
@Composable
private fun MenuAddButtonPREOPENPreview() {
    MenuAddButton(
        StoreStatus.PRE_OPEN
    )
}

@Preview(showBackground = true)
@Composable
private fun MenuAddButtonSOLDOUTPreview() {
    MenuAddButton(
        StoreStatus.SOLD_OUT
    )
}
