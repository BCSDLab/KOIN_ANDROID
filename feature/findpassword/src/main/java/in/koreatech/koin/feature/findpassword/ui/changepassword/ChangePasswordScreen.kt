package `in`.koreatech.koin.feature.findpassword.ui.changepassword

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.findpassword.R
import `in`.koreatech.koin.feature.findpassword.component.KoinFindPasswordPasswordTextField
import `in`.koreatech.koin.feature.findpassword.component.KoinFindPasswordProgressHeader
import `in`.koreatech.koin.feature.findpassword.component.KoinFindPasswordProgressIndicator
import `in`.koreatech.koin.feature.findpassword.component.KoinFindPasswordTextFieldAlert
import `in`.koreatech.koin.feature.findpassword.component.KoinFindPasswordTextFieldAlertState
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel = hiltViewModel(),
    onNextClick: () -> Unit = {}
) {
    val uiState by viewModel.collectAsState()

    ChangePasswordScreenImpl(
        password = uiState.password,
        isPasswordValid = uiState.isPasswordValid,
        passwordConfirm = uiState.passwordConfirm,
        showPassword = uiState.showPassword,
        onPasswordChange = viewModel::updatePassword,
        onPasswordConfirmChange = viewModel::updatePasswordConfirm,
        onPasswordVisibleChange = viewModel::updateShowPassword,
        onNextClick = onNextClick
    )
}

@Composable
fun ChangePasswordScreenImpl(
    password: String,
    isPasswordValid: Boolean,
    passwordConfirm: String,
    showPassword: Boolean,
    modifier: Modifier = Modifier,
    onPasswordChange: (String) -> Unit = {},
    onPasswordConfirmChange: (String) -> Unit = {},
    onPasswordVisibleChange: (Boolean) -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp)
    ) {
        KoinFindPasswordProgressHeader(
            currentStep = 2,
            maxStep = 2,
            text = stringResource(R.string.find_password_step_2)
        )

        Spacer(modifier = Modifier.height(8.dp))

        KoinFindPasswordProgressIndicator(
            currentStep = 2,
            maxStep = 2
        )

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = stringResource(R.string.find_password_change_password_new_password),
            style = KoinTheme.typography.medium18
        )

        Spacer(modifier = Modifier.height(12.dp))

        KoinFindPasswordPasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = onPasswordChange,
            hint = stringResource(R.string.find_password_change_password_new_password_hint),
            showPassword = showPassword,
            onShowPasswordChange = onPasswordVisibleChange
        )

        Box(
            modifier = Modifier.height(64.dp)
        ) {
            if (!isPasswordValid) {
                Spacer(modifier = Modifier.height(8.dp))

                KoinFindPasswordTextFieldAlert(
                    text = stringResource(R.string.find_password_change_password_invalid),
                    state = KoinFindPasswordTextFieldAlertState.Warning
                )
            }
        }

        Text(
            text = stringResource(R.string.find_password_change_password_password_confirm),
            style = KoinTheme.typography.medium18
        )

        Spacer(modifier = Modifier.height(12.dp))

        KoinFindPasswordPasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            value = passwordConfirm,
            onValueChange = onPasswordConfirmChange,
            hint = stringResource(R.string.find_password_change_password_password_confirm_hint),
            showPassword = showPassword,
            onShowPasswordChange = onPasswordVisibleChange
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (password != passwordConfirm) {
            KoinFindPasswordTextFieldAlert(
                text = stringResource(R.string.find_password_change_password_not_match),
                state = KoinFindPasswordTextFieldAlertState.Warning
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        FilledButton(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.find_password_next),
            contentPadding = PaddingValues(12.dp),
            onClick = onNextClick,
            enabled = isPasswordValid && password == passwordConfirm
        )

        Spacer(modifier = Modifier.height(40.dp))

        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}

@Preview(showBackground = true)
@Composable
fun ChangePasswordScreenPreview() {
    ChangePasswordScreenImpl(
        password = "password123",
        isPasswordValid = true,
        passwordConfirm = "password123",
        showPassword = false
    )
}
