package `in`.koreatech.koin.feature.findpassword.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.findpassword.R

@Composable
fun KoinFindPasswordTextFieldAlert(
    text: String,
    state: KoinFindPasswordTextFieldAlertState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = when (state) {
                KoinFindPasswordTextFieldAlertState.Error -> painterResource(R.drawable.ic_find_password_alert_error)
                KoinFindPasswordTextFieldAlertState.Warning -> painterResource(R.drawable.ic_find_password_alert_warning)
                KoinFindPasswordTextFieldAlertState.Success -> painterResource(R.drawable.ic_find_password_alert_success)
            },
            contentDescription = text,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            style = KoinTheme.typography.regular12,
            color = when (state) {
                KoinFindPasswordTextFieldAlertState.Error -> KoinTheme.colors.danger600
                KoinFindPasswordTextFieldAlertState.Warning -> KoinTheme.colors.sub500
                KoinFindPasswordTextFieldAlertState.Success -> KoinTheme.colors.success700
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinFindPasswordTextFieldAlertErrorPreview() {
    KoinTheme {
        KoinFindPasswordTextFieldAlert(
            text = "This is an error alert",
            state = KoinFindPasswordTextFieldAlertState.Error
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinFindPasswordTextFieldAlertWarningPreview() {
    KoinTheme {
        KoinFindPasswordTextFieldAlert(
            text = "This is a warning alert",
            state = KoinFindPasswordTextFieldAlertState.Warning
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinFindPasswordTextFieldAlertSuccessPreview() {
    KoinTheme {
        KoinFindPasswordTextFieldAlert(
            text = "This is a success alert",
            state = KoinFindPasswordTextFieldAlertState.Success
        )
    }
}

sealed class KoinFindPasswordTextFieldAlertState {
    data object Error : KoinFindPasswordTextFieldAlertState()
    data object Warning : KoinFindPasswordTextFieldAlertState()
    data object Success : KoinFindPasswordTextFieldAlertState()
}
