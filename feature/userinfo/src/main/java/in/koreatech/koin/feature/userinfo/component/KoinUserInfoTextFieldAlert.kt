package `in`.koreatech.koin.feature.userinfo.component

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
import `in`.koreatech.koin.feature.userinfo.R

@Composable
fun KoinUserInfoTextFieldAlert(
    text: String,
    state: KoinUserInfoTextFieldAlertState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = when (state) {
                KoinUserInfoTextFieldAlertState.Error -> painterResource(R.drawable.ic_user_info_alert_error)
                KoinUserInfoTextFieldAlertState.Warning -> painterResource(R.drawable.ic_user_info_alert_warning)
                KoinUserInfoTextFieldAlertState.Success -> painterResource(R.drawable.ic_user_info_alert_success)
            },
            contentDescription = text,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            style = KoinTheme.typography.regular12,
            color = when (state) {
                KoinUserInfoTextFieldAlertState.Error -> KoinTheme.colors.danger600
                KoinUserInfoTextFieldAlertState.Warning -> KoinTheme.colors.sub500
                KoinUserInfoTextFieldAlertState.Success -> KoinTheme.colors.success700
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinUserInfoTextFieldAlertErrorPreview() {
    KoinTheme {
        KoinUserInfoTextFieldAlert(
            text = "This is an error alert",
            state = KoinUserInfoTextFieldAlertState.Error
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinUserInfoTextFieldAlertWarningPreview() {
    KoinTheme {
        KoinUserInfoTextFieldAlert(
            text = "This is a warning alert",
            state = KoinUserInfoTextFieldAlertState.Warning
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinUserInfoTextFieldAlertSuccessPreview() {
    KoinTheme {
        KoinUserInfoTextFieldAlert(
            text = "This is a success alert",
            state = KoinUserInfoTextFieldAlertState.Success
        )
    }
}

sealed class KoinUserInfoTextFieldAlertState {
    data object Error : KoinUserInfoTextFieldAlertState()
    data object Warning : KoinUserInfoTextFieldAlertState()
    data object Success : KoinUserInfoTextFieldAlertState()
}
