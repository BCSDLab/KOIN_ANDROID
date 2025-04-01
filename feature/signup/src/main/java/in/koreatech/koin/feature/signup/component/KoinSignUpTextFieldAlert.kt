package `in`.koreatech.koin.feature.signup.component

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
import `in`.koreatech.koin.feature.signup.R

@Composable
fun KoinSignUpTextFieldAlert(
    text: String,
    state: KoinSignUpTextFieldAlertState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = when (state) {
                KoinSignUpTextFieldAlertState.Warning -> painterResource(R.drawable.ic_sign_up_alert_warning)
                KoinSignUpTextFieldAlertState.Success -> painterResource(R.drawable.ic_sign_up_alert_success)
            },
            contentDescription = text,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            style = KoinTheme.typography.regular12,
            color = when (state) {
                KoinSignUpTextFieldAlertState.Warning -> KoinTheme.colors.sub500
                KoinSignUpTextFieldAlertState.Success -> KoinTheme.colors.success700
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KoinSignUpTextFieldAlertWarningPreview() {
    KoinTheme {
        KoinSignUpTextFieldAlert(
            text = "This is a warning alert",
            state = KoinSignUpTextFieldAlertState.Warning
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KoinSignUpTextFieldAlertSuccessPreview() {
    KoinTheme {
        KoinSignUpTextFieldAlert(
            text = "This is a success alert",
            state = KoinSignUpTextFieldAlertState.Success
        )
    }
}

sealed class KoinSignUpTextFieldAlertState() {
    data object Warning : KoinSignUpTextFieldAlertState()
    data object Success : KoinSignUpTextFieldAlertState()
}