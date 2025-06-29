package `in`.koreatech.koin.feature.user.component

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
import `in`.koreatech.koin.feature.user.R

@Composable
fun KoinUserTextFieldAlert(
    text: String,
    state: KoinUserTextFieldAlertState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = when (state) {
                KoinUserTextFieldAlertState.Error -> painterResource(R.drawable.ic_user_alert_error)
                KoinUserTextFieldAlertState.Warning -> painterResource(R.drawable.ic_user_alert_warning)
                KoinUserTextFieldAlertState.Success -> painterResource(R.drawable.ic_user_alert_success)
            },
            contentDescription = text,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            style = KoinTheme.typography.regular12,
            color = when (state) {
                KoinUserTextFieldAlertState.Error -> KoinTheme.colors.danger600
                KoinUserTextFieldAlertState.Warning -> KoinTheme.colors.sub500
                KoinUserTextFieldAlertState.Success -> KoinTheme.colors.success700
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinUserTextFieldAlertErrorPreview() {
    KoinTheme {
        KoinUserTextFieldAlert(
            text = "This is an error alert",
            state = KoinUserTextFieldAlertState.Error
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinUserTextFieldAlertWarningPreview() {
    KoinTheme {
        KoinUserTextFieldAlert(
            text = "This is a warning alert",
            state = KoinUserTextFieldAlertState.Warning
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinUserTextFieldAlertSuccessPreview() {
    KoinTheme {
        KoinUserTextFieldAlert(
            text = "This is a success alert",
            state = KoinUserTextFieldAlertState.Success
        )
    }
}

sealed class KoinUserTextFieldAlertState {
    data object Error : KoinUserTextFieldAlertState()
    data object Warning : KoinUserTextFieldAlertState()
    data object Success : KoinUserTextFieldAlertState()
}
