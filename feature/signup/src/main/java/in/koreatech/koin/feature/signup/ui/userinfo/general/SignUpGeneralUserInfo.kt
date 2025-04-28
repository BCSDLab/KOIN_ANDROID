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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.util.ext.isNicknameFormat
import `in`.koreatech.koin.feature.signup.R
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
        step = uiState.step,
        userId = uiState.userId,
        isUserIdAvailable = uiState.isUserIdAvailable,
        isUserIdValid = uiState.isUserIdValid,
        nickname = uiState.nickname,
        isNicknameAvailable = uiState.isNicknameAvailable,
        password = uiState.password,
        passwordConfirm = uiState.passwordConfirm,
        showPassword = uiState.showPassword,
        isPasswordValid = uiState.isPasswordValid,
        isPasswordEqual = uiState.isPasswordEqual,
        email = uiState.email,
        modifier = modifier,
        onNicknameChange = { viewModel.setNickname(it) },
        checkNicknameDuplicate = { viewModel.checkNicknameDuplicate() },
        onUserIdChange = { viewModel.setUserId(it) },
        checkUserIdDuplicate = { viewModel.checkUserIdDuplicate() },
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
    userId: String,
    isUserIdAvailable: Boolean?,
    isUserIdValid: Boolean,
    nickname: String,
    isNicknameAvailable: Boolean?,
    password: String,
    passwordConfirm: String,
    showPassword: Boolean,
    isPasswordValid: Boolean,
    isPasswordEqual: Boolean,
    email: String,
    modifier: Modifier = Modifier,
    onNicknameChange: (String) -> Unit = {},
    checkNicknameDuplicate: () -> Unit = {},
    onUserIdChange: (String) -> Unit = {},
    checkUserIdDuplicate: () -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onPasswordConfirmChange: (String) -> Unit = {},
    onShowPasswordChange: (Boolean) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onSignUpButtonClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .imePadding()
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
                userId = userId,
                isUserIdAvailable = isUserIdAvailable,
                isUserIdValid = isUserIdValid,
                password = password,
                passwordConfirm = passwordConfirm,
                showPassword = showPassword,
                isPasswordValid = isPasswordValid,
                isPasswordEqual = isPasswordEqual,
                onUserIdChange = { onUserIdChange(it) },
                checkUserIdAvailable = { checkUserIdDuplicate() },
                onPasswordChange = { onPasswordChange(it) },
                onPasswordConfirmChange = { onPasswordConfirmChange(it) },
                onShowPasswordChange = { onShowPasswordChange(it) }
            )

            if (step == SignUpGeneralStep.NICKNANE_AND_EMAIL) {
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
            enabled = (nickname.isNotEmpty() && isNicknameAvailable == true || nickname.isEmpty()) && isPasswordValid && isPasswordEqual && isUserIdAvailable == true,
            contentPadding = PaddingValues(12.dp),
            onClick = {
                onSignUpButtonClick()
            }
        )
    }
}

@Composable
private fun SignUpGeneralUserInfoInitialStep(
    userId: String,
    isUserIdAvailable: Boolean?,
    isUserIdValid: Boolean,
    password: String,
    passwordConfirm: String,
    showPassword: Boolean,
    isPasswordValid: Boolean,
    isPasswordEqual: Boolean,
    onUserIdChange: (String) -> Unit = {},
    checkUserIdAvailable: () -> Unit = {},
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
            value = userId,
            onValueChange = {
                onUserIdChange(it)
            }
        )

        Spacer(modifier = Modifier.width(16.dp))

        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            FilledButton(
                modifier = Modifier.widthIn(min = 86.dp),
                text = stringResource(R.string.sign_up_user_info_id_check_duplicate),
                textStyle = KoinTheme.typography.regular10,
                enabled = userId.isNotEmpty() && isUserIdAvailable != true,
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                onClick = {
                    checkUserIdAvailable()
                }
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    if (userId.isNotEmpty() && !isUserIdValid) {
        KoinSignUpTextFieldAlert(
            text = stringResource(R.string.sign_up_user_info_id_wrong_format),
            state = KoinSignUpTextFieldAlertState.Warning
        )
    }

    if (isUserIdAvailable != null) {
        KoinSignUpTextFieldAlert(
            text = if (isUserIdAvailable == true) stringResource(R.string.sign_up_user_info_id_available) else stringResource(R.string.sign_up_user_info_id_duplicate),
            state = if (isUserIdAvailable == true) KoinSignUpTextFieldAlertState.Success else KoinSignUpTextFieldAlertState.Warning
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
        onShowPasswordChange = { onShowPasswordChange(it) }
    )

    if (isPasswordValid) {
        Spacer(modifier = Modifier.height(16.dp))

        KoinSignUpPasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            hint = stringResource(R.string.sign_up_user_info_password_confirm_hint),
            value = passwordConfirm,
            onValueChange = { onPasswordConfirmChange(it) },
            showPassword = showPassword,
            onShowPasswordChange = { onShowPasswordChange(it) }
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

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
            onValueChange = {
                onNicknameChange(it)
            }
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

    Spacer(modifier = Modifier.height(8.dp))

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
        }
    )
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
            step = SignUpGeneralStep.NICKNANE_AND_EMAIL,
            userId = "userid",
            isUserIdAvailable = true,
            isUserIdValid = true,
            nickname = "nickname",
            isNicknameAvailable = true,
            password = "password",
            passwordConfirm = "password",
            showPassword = false,
            isPasswordValid = true,
            isPasswordEqual = true,
            email = "test@test.com"
        )
    }
}
