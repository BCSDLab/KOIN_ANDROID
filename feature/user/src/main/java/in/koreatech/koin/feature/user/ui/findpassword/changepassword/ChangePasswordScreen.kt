package `in`.koreatech.koin.feature.user.ui.findpassword.changepassword

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import `in`.koreatech.koin.feature.user.R
import `in`.koreatech.koin.feature.user.component.KoinUserPasswordTextField
import `in`.koreatech.koin.feature.user.component.KoinUserProgressHeader
import `in`.koreatech.koin.feature.user.component.KoinUserProgressIndicator
import `in`.koreatech.koin.feature.user.component.KoinUserTextFieldAlert
import `in`.koreatech.koin.feature.user.component.KoinUserTextFieldAlertState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel = hiltViewModel(),
    onNextClick: () -> Unit = {}
) {
    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect {
        handleSideEffect(it, onNextClick)
    }

    ChangePasswordScreenImpl(
        password = uiState.password,
        isPasswordValid = uiState.isPasswordValid,
        passwordConfirm = uiState.passwordConfirm,
        showPassword = uiState.showPassword,
        onPasswordChange = viewModel::updatePassword,
        onPasswordConfirmChange = viewModel::updatePasswordConfirm,
        onPasswordVisibleChange = viewModel::updateShowPassword,
        onNextClick = {
            viewModel.setPassword()
        }
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
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        KoinUserProgressHeader(
            currentStep = 2,
            maxStep = 2,
            text = stringResource(R.string.find_password_step_2)
        )

        Spacer(modifier = Modifier.height(8.dp))

        KoinUserProgressIndicator(
            currentStep = 2,
            maxStep = 2
        )

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = stringResource(R.string.find_password_change_password_new_password),
            style = KoinTheme.typography.medium18
        )

        Spacer(modifier = Modifier.height(12.dp))

        KoinUserPasswordTextField(
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
            if (password.isNotEmpty() && !isPasswordValid) {
                Spacer(modifier = Modifier.height(8.dp))

                KoinUserTextFieldAlert(
                    text = stringResource(R.string.find_password_change_password_invalid),
                    state = KoinUserTextFieldAlertState.Warning
                )
            }
        }

        Text(
            text = stringResource(R.string.find_password_change_password_password_confirm),
            style = KoinTheme.typography.medium18
        )

        Spacer(modifier = Modifier.height(12.dp))

        KoinUserPasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            value = passwordConfirm,
            onValueChange = onPasswordConfirmChange,
            hint = stringResource(R.string.find_password_change_password_password_confirm_hint),
            showPassword = showPassword,
            onShowPasswordChange = onPasswordVisibleChange
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (passwordConfirm.isNotEmpty() && password != passwordConfirm) {
            KoinUserTextFieldAlert(
                text = stringResource(R.string.find_password_change_password_not_match),
                state = KoinUserTextFieldAlertState.Warning
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

fun handleSideEffect(
    sideEffect: ChangePasswordSideEffect,
    onNextClick: () -> Unit
) {
    when (sideEffect) {
        ChangePasswordSideEffect.PasswordChanged -> onNextClick()
        ChangePasswordSideEffect.PasswordChangeFailed -> {
        }
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
