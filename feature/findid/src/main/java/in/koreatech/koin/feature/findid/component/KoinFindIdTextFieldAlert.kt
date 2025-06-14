package `in`.koreatech.koin.feature.findid.component

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
import `in`.koreatech.koin.feature.findid.R

@Composable
fun KoinFindIdTextFieldAlert(
    text: String,
    state: KoinFindIdTextFieldAlertState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = when (state) {
                KoinFindIdTextFieldAlertState.Error -> painterResource(R.drawable.ic_find_id_alert_error)
                KoinFindIdTextFieldAlertState.Warning -> painterResource(R.drawable.ic_find_id_alert_warning)
                KoinFindIdTextFieldAlertState.Success -> painterResource(R.drawable.ic_find_id_alert_success)
            },
            contentDescription = text,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            style = KoinTheme.typography.regular12,
            color = when (state) {
                KoinFindIdTextFieldAlertState.Error -> KoinTheme.colors.danger600
                KoinFindIdTextFieldAlertState.Warning -> KoinTheme.colors.sub500
                KoinFindIdTextFieldAlertState.Success -> KoinTheme.colors.success700
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinFindIdTextFieldAlertErrorPreview() {
    KoinTheme {
        KoinFindIdTextFieldAlert(
            text = "This is an error alert",
            state = KoinFindIdTextFieldAlertState.Error
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinFindIdTextFieldAlertWarningPreview() {
    KoinTheme {
        KoinFindIdTextFieldAlert(
            text = "This is a warning alert",
            state = KoinFindIdTextFieldAlertState.Warning
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinFindIdTextFieldAlertSuccessPreview() {
    KoinTheme {
        KoinFindIdTextFieldAlert(
            text = "This is a success alert",
            state = KoinFindIdTextFieldAlertState.Success
        )
    }
}

sealed class KoinFindIdTextFieldAlertState {
    data object Error : KoinFindIdTextFieldAlertState()
    data object Warning : KoinFindIdTextFieldAlertState()
    data object Success : KoinFindIdTextFieldAlertState()
}
