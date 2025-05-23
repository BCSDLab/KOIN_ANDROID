package `in`.koreatech.koin.feature.signin.component

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
import `in`.koreatech.koin.feature.signin.R

@Composable
fun KoinSignInTextFieldAlert(
    text: String,
    state: KoinSignInTextFieldAlertState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = when (state) {
                KoinSignInTextFieldAlertState.Error -> painterResource(R.drawable.ic_sign_in_alert_error)
                KoinSignInTextFieldAlertState.Warning -> painterResource(R.drawable.ic_sign_in_alert_warning)
                KoinSignInTextFieldAlertState.Success -> painterResource(R.drawable.ic_sign_in_alert_success)
            },
            contentDescription = text,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            style = KoinTheme.typography.regular12,
            color = when (state) {
                KoinSignInTextFieldAlertState.Error -> KoinTheme.colors.danger600
                KoinSignInTextFieldAlertState.Warning -> KoinTheme.colors.sub500
                KoinSignInTextFieldAlertState.Success -> KoinTheme.colors.success700
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinSignInTextFieldAlertErrorPreview() {
    KoinTheme {
        KoinSignInTextFieldAlert(
            text = "This is an error alert",
            state = KoinSignInTextFieldAlertState.Error
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinSignInTextFieldAlertWarningPreview() {
    KoinTheme {
        KoinSignInTextFieldAlert(
            text = "This is a warning alert",
            state = KoinSignInTextFieldAlertState.Warning
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinSignInTextFieldAlertSuccessPreview() {
    KoinTheme {
        KoinSignInTextFieldAlert(
            text = "This is a success alert",
            state = KoinSignInTextFieldAlertState.Success
        )
    }
}

sealed class KoinSignInTextFieldAlertState {
    data object Error : KoinSignInTextFieldAlertState()
    data object Warning : KoinSignInTextFieldAlertState()
    data object Success : KoinSignInTextFieldAlertState()
}
