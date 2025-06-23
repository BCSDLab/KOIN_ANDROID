package `in`.koreatech.koin.feature.user.ui.signup.userinfo.student

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.util.ext.isNicknameFormat
import `in`.koreatech.koin.domain.util.ext.isValidStudentId
import `in`.koreatech.koin.feature.user.NICKNAME_MAX_LENGTH
import `in`.koreatech.koin.feature.user.R
import `in`.koreatech.koin.feature.user.component.KoinUserBasicTextField
import `in`.koreatech.koin.feature.user.component.KoinUserDropdown
import `in`.koreatech.koin.feature.user.component.KoinUserPasswordTextField
import `in`.koreatech.koin.feature.user.component.KoinUserProgressHeader
import `in`.koreatech.koin.feature.user.component.KoinUserProgressIndicator
import `in`.koreatech.koin.feature.user.component.KoinUserTextFieldAlert
import `in`.koreatech.koin.feature.user.component.KoinUserTextFieldAlertState
import `in`.koreatech.koin.feature.user.majorStringList
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SignUpStudentUserInfo(
    modifier: Modifier = Modifier,
    viewModel: SignUpStudentViewModel = hiltViewModel(),
    navigateToNextScreen: () -> Unit
) {
    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect {
        handleSideEffect(
            sideEffect = it,
            navigateToNextScreen = navigateToNextScreen
        )
    }

    SignUpStudentUserInfoImpl(
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
        department = uiState.department,
        studentNumber = uiState.studentNumber,
        isDropdownExpanded = uiState.isDropdownExpanded,
        isDepartmentSelected = uiState.isDepartmentSelected,
        email = uiState.email,
        isEmailAvailable = uiState.isEmailAvailable,
        enabled = uiState.isEnabled,
        modifier = modifier,
        onNicknameChange = { viewModel.setNickname(it) },
        checkNicknameDuplicate = { viewModel.checkNicknameDuplicate() },
        onLoginIdChange = { viewModel.setLoginId(it) },
        checkLoginIdDuplicate = { viewModel.checkLoginIdDuplicate() },
        onPasswordChange = { viewModel.setPassword(it) },
        onPasswordConfirmChange = { viewModel.setPasswordConfirm(it) },
        onShowPasswordChange = { viewModel.setPasswordVisibility(it) },
        onDropdownExpandChange = { viewModel.setDepartmentDropdownExpanded(it) },
        onDepartmentSelected = { viewModel.setDepartment(it) },
        onStudentNumberChange = { viewModel.setStudentNumber(it) },
        onEmailChange = { viewModel.setEmail(it) },
        onSignUpButtonClick = { viewModel.signUp() }
    )
}

@Composable
fun SignUpStudentUserInfoImpl(
    step: SignUpStudentStep,
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
    department: String,
    studentNumber: String,
    isDropdownExpanded: Boolean,
    isDepartmentSelected: Boolean,
    email: String,
    isEmailAvailable: Boolean?,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onNicknameChange: (String) -> Unit = {},
    checkNicknameDuplicate: () -> Unit = {},
    onLoginIdChange: (String) -> Unit = {},
    checkLoginIdDuplicate: () -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onPasswordConfirmChange: (String) -> Unit = {},
    onShowPasswordChange: (Boolean) -> Unit = {},
    onDropdownExpandChange: (Boolean) -> Unit = {},
    onDepartmentSelected: (String) -> Unit = {},
    onStudentNumberChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onSignUpButtonClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp)
            .padding(bottom = 40.dp)
    ) {
        KoinUserProgressHeader(
            text = stringResource(R.string.sign_up_user_info),
            currentStep = 4,
            maxStep = 4
        )

        Spacer(modifier = Modifier.height(8.dp))

        KoinUserProgressIndicator(
            currentStep = 4,
            maxStep = 4
        )

        Spacer(modifier = Modifier.height(64.dp))

        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            SignUpStudentUserInfoInitialStep(
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

            if (step == SignUpStudentStep.NICKNAME_AND_EMAIL) {
                Spacer(modifier = Modifier.height(32.dp))

                SignUpStudentUserInfoNickNameEmailStep(
                    nickname = nickname,
                    isNicknameAvailable = isNicknameAvailable,
                    email = email,
                    isEmailAvailable = isEmailAvailable,
                    department = department,
                    isDepartmentSelected = isDepartmentSelected,
                    isDropdownExpanded = isDropdownExpanded,
                    studentNumber = studentNumber,
                    onDropdownExpandChange = { onDropdownExpandChange(it) },
                    onDepartmentSelected = { onDepartmentSelected(it) },
                    onStudentNumberChange = { onStudentNumberChange(it) },
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
private fun SignUpStudentUserInfoInitialStep(
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
        KoinUserBasicTextField(
            modifier = Modifier.weight(1f),
            hint = stringResource(R.string.sign_up_user_info_id_hint),
            value = loginId,
            onValueChange = {
                onLoginIdChange(it)
            },
            showTrailingClearButton = false,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.width(16.dp))

        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            FilledButton(
                modifier = Modifier.widthIn(min = 86.dp),
                text = stringResource(R.string.sign_up_user_info_id_check_duplicate),
                textStyle = KoinTheme.typography.regular10,
                enabled = loginId.isNotEmpty() && isLoginIdAvailable != true && isLoginIdValid,
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                onClick = {
                    checkLoginIdAvailable()
                }
            )
        }
    }

    if (loginId.isNotEmpty() && !isLoginIdValid) {
        KoinUserTextFieldAlert(
            text = stringResource(R.string.sign_up_user_info_id_wrong_format),
            state = KoinUserTextFieldAlertState.Warning
        )
    }

    if (isLoginIdAvailable != null) {
        KoinUserTextFieldAlert(
            text = if (isLoginIdAvailable == true) stringResource(R.string.sign_up_user_info_id_available) else stringResource(R.string.sign_up_user_info_id_duplicate),
            state = if (isLoginIdAvailable == true) KoinUserTextFieldAlertState.Success else KoinUserTextFieldAlertState.Warning
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    Text(
        text = stringResource(R.string.sign_up_user_info_password_title),
        style = KoinTheme.typography.medium16
    )

    Spacer(modifier = Modifier.height(16.dp))

    KoinUserPasswordTextField(
        modifier = Modifier.fillMaxWidth(),
        hint = stringResource(R.string.sign_up_user_info_password_hint),
        value = password,
        onValueChange = { onPasswordChange(it) },
        showPassword = showPassword,
        onShowPasswordChange = { onShowPasswordChange(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        )
    )

    if (isPasswordValid) {
        Spacer(modifier = Modifier.height(16.dp))

        KoinUserPasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            hint = stringResource(R.string.sign_up_user_info_password_confirm_hint),
            value = passwordConfirm,
            onValueChange = { onPasswordConfirmChange(it) },
            showPassword = showPassword,
            onShowPasswordChange = { onShowPasswordChange(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            )
        )
    }

    if (password.isNotEmpty() && !isPasswordValid) {
        KoinUserTextFieldAlert(
            text = stringResource(R.string.sign_up_user_info_password_not_valid),
            state = KoinUserTextFieldAlertState.Warning
        )
    }

    if (isPasswordValid && passwordConfirm.isNotEmpty()) {
        KoinUserTextFieldAlert(
            text = if (isPasswordEqual) stringResource(R.string.sign_up_user_info_password_confirm_correct) else stringResource(R.string.sign_up_user_info_password_confirm_incorrect),
            state = if (isPasswordEqual) KoinUserTextFieldAlertState.Success else KoinUserTextFieldAlertState.Warning
        )
    }
}

@Composable
private fun SignUpStudentUserInfoNickNameEmailStep(
    nickname: String,
    isNicknameAvailable: Boolean?,
    email: String,
    isEmailAvailable: Boolean?,
    department: String,
    isDepartmentSelected: Boolean,
    isDropdownExpanded: Boolean,
    studentNumber: String,
    onDropdownExpandChange: (Boolean) -> Unit,
    onDepartmentSelected: (String) -> Unit = {},
    onStudentNumberChange: (String) -> Unit = {},
    checkNicknameDuplicate: () -> Unit = {},
    onNicknameChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {}
) {
    Text(
        text = stringResource(R.string.sign_up_user_info_department_and_student_number_title),
        style = KoinTheme.typography.medium18
    )

    Spacer(modifier = Modifier.height(12.dp))

    KoinUserDropdown(
        text = department,
        hint = stringResource(R.string.sign_up_user_info_department_hint),
        isSelected = isDepartmentSelected,
        isDropdownExpanded = isDropdownExpanded,
        items = majorStringList,
        onDropdownExpandChange = onDropdownExpandChange,
        onItemSelected = {
            onDepartmentSelected(majorStringList[it])
        }
    )

    Spacer(modifier = Modifier.height(12.dp))

    KoinUserBasicTextField(
        modifier = Modifier.fillMaxWidth(),
        hint = stringResource(R.string.sign_up_user_info_student_number_hint),
        value = studentNumber,
        onValueChange = { onStudentNumberChange(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
    )

    if (studentNumber.isNotEmpty() && !studentNumber.isValidStudentId) {
        Spacer(modifier = Modifier.height(8.dp))

        KoinUserTextFieldAlert(
            text = stringResource(R.string.sign_up_user_info_student_number_wrong_format),
            state = KoinUserTextFieldAlertState.Warning
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically
    ) {
        KoinUserBasicTextField(
            modifier = Modifier.weight(1f),
            hint = stringResource(R.string.sign_up_user_info_nickname_hint),
            value = nickname,
            maxLength = NICKNAME_MAX_LENGTH,
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
                enabled = nickname.isNotEmpty() && nickname.isNicknameFormat() && isNicknameAvailable != true,
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                onClick = {
                    checkNicknameDuplicate()
                }
            )
        }
    }

    if (nickname.isNotEmpty() && !nickname.isNicknameFormat()) {
        Spacer(modifier = Modifier.height(8.dp))

        KoinUserTextFieldAlert(
            text = stringResource(R.string.sign_up_user_info_nickname_wrong_format),
            state = KoinUserTextFieldAlertState.Warning
        )
    }

    if (isNicknameAvailable != null) {
        KoinUserTextFieldAlert(
            text = if (isNicknameAvailable == true) stringResource(R.string.sign_up_user_info_nickname_available) else stringResource(R.string.sign_up_user_info_nickname_duplicate),
            state = if (isNicknameAvailable == true) KoinUserTextFieldAlertState.Success else KoinUserTextFieldAlertState.Warning
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        KoinUserBasicTextField(
            modifier = Modifier.weight(1f),
            hint = stringResource(R.string.sign_up_user_info_email_hint),
            value = email,
            onValueChange = {
                onEmailChange(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Ascii,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            style = KoinTheme.typography.regular14,
            color = KoinTheme.colors.neutral400,
            text = stringResource(R.string.sign_up_user_info_email_koreatech_suffix)
        )
    }

    if (email.isNotEmpty() && isEmailAvailable == false) {
        KoinUserTextFieldAlert(
            text = stringResource(R.string.sign_up_user_info_email_duplicate),
            state = KoinUserTextFieldAlertState.Warning
        )
    }
}

private fun handleSideEffect(
    sideEffect: SignUpStudentSideEffect,
    navigateToNextScreen: () -> Unit
) {
    when (sideEffect) {
        is SignUpStudentSideEffect.SignUpSuccess -> {
            navigateToNextScreen()
        }

        is SignUpStudentSideEffect.SignUpFailure -> {
            // TODO: Handle sign up failure
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpStudentUserInfoPreview() {
    KoinTheme {
        SignUpStudentUserInfoImpl(
            step = SignUpStudentStep.NICKNAME_AND_EMAIL,
            loginId = "loginId",
            isLoginIdAvailable = true,
            isLoginIdValid = true,
            email = "email",
            isEmailAvailable = true,
            nickname = "nickname",
            isNicknameAvailable = true,
            password = "password",
            passwordConfirm = "password",
            showPassword = false,
            isPasswordValid = true,
            isPasswordEqual = true,
            department = "컴퓨터공학과",
            studentNumber = "2000000000",
            isDropdownExpanded = false,
            isDepartmentSelected = false,
            enabled = true
        )
    }
}
