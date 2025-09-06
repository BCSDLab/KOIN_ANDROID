package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
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

enum class StoreStatus(val isButtonEnabled: Boolean) {
    OPEN(true),
    PRE_OPEN(false),
    SOLD_OUT(false)
}

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
            else /* OPEN */ -> stringResource(R.string.add_same_menu_open)
        }

    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
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
fun MenuAddButtonOPENPreview() {
    MenuAddButton(
        StoreStatus.OPEN
    )
}

@Preview(showBackground = true)
@Composable
fun MenuAddButtonPRE_OPENPreview() {
    MenuAddButton(
        StoreStatus.PRE_OPEN
    )
}

@Preview(showBackground = true)
@Composable
fun MenuAddButtonSOLD_OUTPreview() {
    MenuAddButton(
        StoreStatus.SOLD_OUT
    )
}
