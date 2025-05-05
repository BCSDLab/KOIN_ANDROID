package `in`.koreatech.koin.feature.signup.ui.userinfo.general

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.util.ext.isNicknameFormat
import `in`.koreatech.koin.domain.util.ext.isNotValidGeneralEmail
import `in`.koreatech.koin.feature.signup.R
import `in`.koreatech.koin.feature.signup.SIGN_UP_NICKNAME_MAX_LENGTH
import `in`.koreatech.koin.feature.signup.component.KoinSignUpBasicTextField
import `in`.koreatech.koin.feature.signup.component.KoinSignUpPasswordTextField
import `in`.koreatech.koin.feature.signup.component.KoinSignUpProgressHeader
import `in`.koreatech.koin.feature.signup.component.KoinSignUpProgressIndicator
import `in`.koreatech.koin.feature.signup.component.KoinSignUpTextFieldAlert
import `in`.koreatech.koin.feature.signup.component.KoinSignUpTextFieldAlertState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SignUpGeneralUserInfo(
    modifier: Modifier = Modifier,
    viewModel: SignUpGeneralViewModel = hiltViewModel(),
    navigateToNextScreen: () -> Unit
) {
    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect {
        handleSideEffect(
            sideEffect = it,
            navigateToNextScreen = navigateToNextScreen
        )
    }

    SignUpGeneralUserInfoImpl(
        step = uiState.currentStep,
        loginId = uiState.loginId,
        isLoginIdAvailable = uiState.isLoginIdAvailable,
        isLoginIdValid = uiState.isLoginIdValid,
        nickname = uiState.nickname,
        isNicknameAvailable = uiState.isNicknameAvailable,
        password = uiState.password,
        passwordConfirm = uiState.passwordConfirm,
        showPassword = uiState.showPassword,
        isPasswordValid = uiState.isPasswordValid,
        isPasswordEqual = uiState.isPasswordEqual,
        email = uiState.email,
        enabled = uiState.isEnabled,
        modifier = modifier,
        onNicknameChange = { viewModel.setNickname(it) },
        checkNicknameDuplicate = { viewModel.checkNicknameDuplicate() },
        onLoginIdChange = { viewModel.setLoginId(it) },
        checkLoginIdDuplicate = { viewModel.checkLoginIdDuplicate() },
        onPasswordChange = { viewModel.setPassword(it) },
        onPasswordConfirmChange = { viewModel.setPasswordConfirm(it) },
        onShowPasswordChange = { viewModel.setPasswordVisibility(it) },
        onEmailChange = { viewModel.setEmail(it) },
        onSignUpButtonClick = { viewModel.signUp() }
    )
}

@Composable
fun SignUpGeneralUserInfoImpl(
    step: SignUpGeneralStep,
    loginId: String,
    isLoginIdAvailable: Boolean?,
    isLoginIdValid: Boolean,
    nickname: String,
    isNicknameAvailable: Boolean?,
    password: String,
    passwordConfirm: String,
    showPassword: Boolean,
    isPasswordValid: Boolean,
    isPasswordEqual: Boolean,
    email: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onNicknameChange: (String) -> Unit = {},
    checkNicknameDuplicate: () -> Unit = {},
    onLoginIdChange: (String) -> Unit = {},
    checkLoginIdDuplicate: () -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onPasswordConfirmChange: (String) -> Unit = {},
    onShowPasswordChange: (Boolean) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onSignUpButtonClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        KoinSignUpProgressHeader(
            text = stringResource(R.string.sign_up_user_info),
            currentStep = 4,
            maxStep = 4
        )

        Spacer(modifier = Modifier.height(8.dp))

        KoinSignUpProgressIndicator(
            currentStep = 4,
            maxStep = 4
        )

        Spacer(modifier = Modifier.height(64.dp))

        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            SignUpGeneralUserInfoInitialStep(
                loginId = loginId,
                isLoginIdAvailable = isLoginIdAvailable,
                isLoginIdValid = isLoginIdValid,
                password = password,
                passwordConfirm = passwordConfirm,
                showPassword = showPassword,
                isPasswordValid = isPasswordValid,
                isPasswordEqual = isPasswordEqual,
                onLoginIdChange = { onLoginIdChange(it) },
                checkLoginIdAvailable = { checkLoginIdDuplicate() },
                onPasswordChange = { onPasswordChange(it) },
                onPasswordConfirmChange = { onPasswordConfirmChange(it) },
                onShowPasswordChange = { onShowPasswordChange(it) }
            )

            if (step == SignUpGeneralStep.NICKNAME_AND_EMAIL) {
                Spacer(modifier = Modifier.height(32.dp))

                SignUpGeneralUserInfoNickNameEmailStep(
                    nickname = nickname,
                    isNicknameAvailable = isNicknameAvailable,
                    email = email,
                    checkNicknameDuplicate = { checkNicknameDuplicate() },
                    onNicknameChange = { onNicknameChange(it) },
                    onEmailChange = { onEmailChange(it) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.weight(1f))

        FilledButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.sign_up_next),
            enabled = enabled,
            contentPadding = PaddingValues(12.dp),
            onClick = {
                onSignUpButtonClick()
            }
        )
    }
}

@Composable
private fun SignUpGeneralUserInfoInitialStep(
    loginId: String,
    isLoginIdAvailable: Boolean?,
    isLoginIdValid: Boolean,
    password: String,
    passwordConfirm: String,
    showPassword: Boolean,
    isPasswordValid: Boolean,
    isPasswordEqual: Boolean,
    onLoginIdChange: (String) -> Unit = {},
    checkLoginIdAvailable: () -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onPasswordConfirmChange: (String) -> Unit = {},
    onShowPasswordChange: (Boolean) -> Unit = {}
) {
    Text(
        text = stringResource(R.string.sign_up_user_info_id_title),
        style = KoinTheme.typography.medium16
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically
    ) {
        KoinSignUpBasicTextField(
            modifier = Modifier.weight(1f),
            hint = stringResource(R.string.sign_up_user_info_id_hint),
            value = loginId,
            onValueChange = {
                onLoginIdChange(it)
            },
            showTrailingClearButton = false,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.width(16.dp))

        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            FilledButton(
                modifier = Modifier.widthIn(min = 86.dp),
                text = stringResource(R.string.sign_up_user_info_id_check_duplicate),
                textStyle = KoinTheme.typography.regular10,
                enabled = loginId.isNotEmpty() && isLoginIdAvailable != true,
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                onClick = {
                    checkLoginIdAvailable()
                }
            )
        }
    }

    if (loginId.isNotEmpty() && !isLoginIdValid) {
        KoinSignUpTextFieldAlert(
            text = stringResource(R.string.sign_up_user_info_id_wrong_format),
            state = KoinSignUpTextFieldAlertState.Warning
        )
    }

    if (isLoginIdAvailable != null) {
        KoinSignUpTextFieldAlert(
            text = if (isLoginIdAvailable == true) stringResource(R.string.sign_up_user_info_id_available) else stringResource(R.string.sign_up_user_info_id_duplicate),
            state = if (isLoginIdAvailable == true) KoinSignUpTextFieldAlertState.Success else KoinSignUpTextFieldAlertState.Warning
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    Text(
        text = stringResource(R.string.sign_up_user_info_password_title),
        style = KoinTheme.typography.medium16
    )

    Spacer(modifier = Modifier.height(16.dp))

    KoinSignUpPasswordTextField(
        modifier = Modifier.fillMaxWidth(),
        hint = stringResource(R.string.sign_up_user_info_password_hint),
        value = password,
        onValueChange = { onPasswordChange(it) },
        showPassword = showPassword,
        onShowPasswordChange = { onShowPasswordChange(it) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        )
    )

    if (isPasswordValid) {
        Spacer(modifier = Modifier.height(16.dp))

        KoinSignUpPasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            hint = stringResource(R.string.sign_up_user_info_password_confirm_hint),
            value = passwordConfirm,
            onValueChange = { onPasswordConfirmChange(it) },
            showPassword = showPassword,
            onShowPasswordChange = { onShowPasswordChange(it) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )
    }

    if (password.isNotEmpty() && !isPasswordValid) {
        KoinSignUpTextFieldAlert(
            text = stringResource(R.string.sign_up_user_info_password_not_valid),
            state = KoinSignUpTextFieldAlertState.Warning
        )
    }

    if (passwordConfirm.isNotEmpty()) {
        KoinSignUpTextFieldAlert(
            text = if (isPasswordEqual) stringResource(R.string.sign_up_user_info_password_confirm_correct) else stringResource(R.string.sign_up_user_info_password_confirm_incorrect),
            state = if (isPasswordEqual) KoinSignUpTextFieldAlertState.Success else KoinSignUpTextFieldAlertState.Warning
        )
    }
}

@Composable
private fun SignUpGeneralUserInfoNickNameEmailStep(
    nickname: String,
    isNicknameAvailable: Boolean?,
    email: String,
    checkNicknameDuplicate: () -> Unit = {},
    onNicknameChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically
    ) {
        KoinSignUpBasicTextField(
            modifier = Modifier.weight(1f),
            hint = stringResource(R.string.sign_up_user_info_nickname_hint),
            value = nickname,
            maxLength = SIGN_UP_NICKNAME_MAX_LENGTH,
            onValueChange = {
                onNicknameChange(it)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.width(16.dp))

        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            FilledButton(
                modifier = Modifier.widthIn(min = 86.dp),
                text = stringResource(R.string.sign_up_user_info_nickname_check_duplicate),
                textStyle = KoinTheme.typography.regular10,
                enabled = nickname.isNotEmpty() && nickname.isNicknameFormat(),
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                onClick = {
                    checkNicknameDuplicate()
                }
            )
        }
    }

    if (nickname.isNotEmpty() && !nickname.isNicknameFormat()) {
        KoinSignUpTextFieldAlert(
            text = stringResource(R.string.sign_up_user_info_nickname_wrong_format),
            state = KoinSignUpTextFieldAlertState.Warning
        )
    }

    if (isNicknameAvailable != null) {
        KoinSignUpTextFieldAlert(
            text = if (isNicknameAvailable == true) stringResource(R.string.sign_up_user_info_nickname_available) else stringResource(R.string.sign_up_user_info_nickname_duplicate),
            state = if (isNicknameAvailable == true) KoinSignUpTextFieldAlertState.Success else KoinSignUpTextFieldAlertState.Warning
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

    KoinSignUpBasicTextField(
        modifier = Modifier.fillMaxWidth(),
        hint = stringResource(R.string.sign_up_user_info_email_hint),
        value = email,
        onValueChange = {
            onEmailChange(it)
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        )
    )

    if (email.isNotEmpty() && email.isNotValidGeneralEmail()) {
        KoinSignUpTextFieldAlert(
            text = stringResource(R.string.sign_up_user_info_email_wrong_format),
            state = KoinSignUpTextFieldAlertState.Warning
        )
    }
}

private fun handleSideEffect(
    sideEffect: SignUpGeneralSideEffect,
    navigateToNextScreen: () -> Unit
) {
    when (sideEffect) {
        is SignUpGeneralSideEffect.SignUpSuccess -> {
            navigateToNextScreen()
        }

        is SignUpGeneralSideEffect.SignUpFailure -> {
            // TODO: Handle sign up failure
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpGeneralUserInfoPreview() {
    KoinTheme {
        SignUpGeneralUserInfoImpl(
            step = SignUpGeneralStep.NICKNAME_AND_EMAIL,
            loginId = "loginid",
            isLoginIdAvailable = true,
            isLoginIdValid = true,
            nickname = "nickname",
            isNicknameAvailable = true,
            password = "password",
            passwordConfirm = "password",
            showPassword = false,
            isPasswordValid = true,
            isPasswordEqual = true,
            email = "test@test.com",
            enabled = true
        )
    }
}
