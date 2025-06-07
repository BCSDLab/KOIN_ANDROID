package `in`.koreatech.koin.feature.userinfo.ui.userinfoedit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.FilledButtonColors
import `in`.koreatech.koin.core.designsystem.component.dialog.ChoiceDialog
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.PhoneNumber
import `in`.koreatech.koin.domain.model.user.VerificationCode
import `in`.koreatech.koin.domain.util.ext.isValidEmail
import `in`.koreatech.koin.feature.userinfo.R
import `in`.koreatech.koin.feature.userinfo.component.KoinUserInfoBasicItem
import `in`.koreatech.koin.feature.userinfo.component.KoinUserInfoBasicTextField
import `in`.koreatech.koin.feature.userinfo.component.KoinUserInfoDropdown
import `in`.koreatech.koin.feature.userinfo.component.KoinUserInfoSingleChoiceRadioGroup
import `in`.koreatech.koin.feature.userinfo.component.KoinUserInfoTextFieldAlert
import `in`.koreatech.koin.feature.userinfo.component.KoinUserInfoTextFieldAlertState
import `in`.koreatech.koin.feature.userinfo.component.KoinUserInfoWithButtonItem
import `in`.koreatech.koin.feature.userinfo.component.UserInfoHeader
import `in`.koreatech.koin.feature.userinfo.genderList
import `in`.koreatech.koin.feature.userinfo.majorStringList
import `in`.koreatech.koin.feature.userinfo.util.secondToMinute
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoEditScreen(
    modifier: Modifier = Modifier,
    viewModel: UserInfoEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.collectAsState()

    Scaffold(
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.user_info_title),
                onNavigationIconClick = {
                },
                actions = {
                    TextButton(
                        modifier = Modifier,
                        onClick = {
                            viewModel.updateWithdrawalDialog(!uiState.showWithdrawalDialog)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.user_info_withdraw),
                            style = KoinTheme.typography.medium12,
                            color = KoinTheme.colors.neutral800
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = KoinTheme.colors.neutral0
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(contentPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            if (uiState.showWithdrawalDialog) {
                ChoiceDialog(
                    title = stringResource(R.string.user_info_withdraw_title),
                    description = stringResource(R.string.user_info_withdraw_message),
                    onPositive = {
                        viewModel.updateWithdrawalDialog(false)
                    },
                    onNegative = {
                        viewModel.updateWithdrawalDialog(false)
                    },
                    positiveButtonText = stringResource(R.string.user_info_withdraw_positive),
                    negativeButtonText = stringResource(R.string.user_info_withdraw_negative),
                    positiveButtonColors = FilledButtonColors.Danger
                )
            }

            UserInfoHeader(stringResource(R.string.user_info_general_user_info_header))

            GeneralUserInfo(
                loginId = uiState.loginId,
                name = uiState.name,
                nickName = uiState.nickname,
                phoneNumber = uiState.phoneNumber,
                email = uiState.email,
                gender = when (uiState.gender) {
                    Gender.Man -> 0
                    Gender.Woman -> 1
                    else -> null
                },
                phoneNumberState = uiState.phoneNumberState,
                verificationCodeState = uiState.verificationCodeState,
                verificationCode = uiState.verificationCode,
                verificationTimeLeft = uiState.verificationTimeLeft,
                isPhoneNumberChanged = uiState.isPhoneNumberChanged,
                isNicknameChanged = uiState.isNicknameChanged,
                onLoginIdChange = { viewModel.updateLoginId(it) },
                onNameChange = { viewModel.updateName(it) },
                onNicknameChange = { viewModel.updateNickname(it) },
                onPhoneNumberChange = { viewModel.updatePhoneNumber(it) },
                onEmailChange = { viewModel.updateEmail(it) },
                onGenderChange = { viewModel.updateGender(it) },
                onRequestVerificationCode = { },
                onVerificationCodeChange = { viewModel.updateVerificationCode(it) },
                checkVerificationCode = { viewModel.requestVerificationCode() },
                onNicknameDuplicateCheck = { }
            )

            UserInfoHeader(stringResource(R.string.user_info_student_info_header))

            StudentUserInfo(
                studentNumber = uiState.studentNumber,
                major = uiState.major,
                isMajorDropdownExpanded = uiState.isMajorDropdownExpanded,
                onStudentNumberChange = { viewModel.updateStudentNumber(it) },
                onMajorChange = { viewModel.updateMajor(it) },
                onMajorDropdownExpandedChange = { viewModel.updateMajorDropdownExpanded(it) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Spacer(modifier = Modifier.weight(1f))

            FilledButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                text = stringResource(R.string.user_info_save),
                textStyle = KoinTheme.typography.medium15,
                enabled = uiState.isModified,
                onClick = {}
            )

            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

@Composable
fun GeneralUserInfo(
    loginId: String,
    name: String,
    nickName: String,
    phoneNumber: String,
    email: String,
    gender: Int?,
    phoneNumberState: PhoneNumber,
    verificationCodeState: VerificationCode,
    verificationCode: String,
    verificationTimeLeft: Int,
    isPhoneNumberChanged: Boolean,
    isNicknameChanged: Boolean,
    modifier: Modifier = Modifier,
    onLoginIdChange: (String) -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onNicknameChange: (String) -> Unit = {},
    onPhoneNumberChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit = {},
    onGenderChange: (Int) -> Unit = {},
    onRequestVerificationCode: () -> Unit = {},
    onVerificationCodeChange: (String) -> Unit = {},
    checkVerificationCode: () -> Unit = {},
    onNicknameDuplicateCheck: () -> Unit = {}
) {
    Column(
        modifier = modifier.padding(vertical = 16.dp, horizontal = 32.dp)
    ) {
        KoinUserInfoBasicItem(
            title = stringResource(R.string.user_info_general_user_info_login_id),
            value = loginId,
            onValueChange = onLoginIdChange
        )

        Spacer(modifier = Modifier.height(32.dp))

        KoinUserInfoBasicItem(
            title = stringResource(R.string.user_info_general_user_info_name),
            value = name,
            onValueChange = onNameChange
        )

        Spacer(modifier = Modifier.height(32.dp))

        KoinUserInfoWithButtonItem(
            title = stringResource(R.string.user_info_general_user_info_nickname),
            value = nickName,
            buttonText = stringResource(R.string.user_info_general_user_info_nickname_button),
            onValueChange = onNicknameChange,
            onButtonAction = onNicknameDuplicateCheck,
            buttonEnabled = isNicknameChanged
        )

        Spacer(modifier = Modifier.height(32.dp))

        UserInfoPhoneNumber(
            phoneNumber = phoneNumber,
            phoneNumberState = phoneNumberState,
            isPhoneNumberChanged = isPhoneNumberChanged,
            onPhoneNumberChange = onPhoneNumberChange,
            onRequestVerificationCode = onRequestVerificationCode
        )

        if (phoneNumberState is PhoneNumber.Sent) {
            UserInfoVerificationCodeVerification(
                verificationCode = verificationCode,
                verificationCodeState = verificationCodeState,
                verificationTimeLeft = verificationTimeLeft,
                onVerificationCodeChange = onVerificationCodeChange,
                checkVerificationCode = checkVerificationCode
            )
        }

        if (email.isValidEmail()) { // If koreatech email
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                KoinUserInfoBasicItem(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.user_info_general_user_info_email),
                    value = email.replace("@koreatech.ac.kr", ""),
                    onValueChange = {
                        onEmailChange(
                            if (it.isBlank()) "" else "${it.trim()}@koreatech.ac.kr"
                        )
                    }
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Placeholder for alignment
                    Text(
                        text = "",
                        style = KoinTheme.typography.regular16
                    )

                    Text(
                        text = stringResource(R.string.user_info_student_email_suffix),
                        style = KoinTheme.typography.regular14
                    )
                }
            }
        } else {
            KoinUserInfoBasicItem(
                title = stringResource(R.string.user_info_general_user_info_email),
                value = email,
                onValueChange = onEmailChange
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.user_info_general_user_info_gender),
            style = KoinTheme.typography.regular16
        )

        Spacer(modifier = Modifier.height(8.dp))

        KoinUserInfoSingleChoiceRadioGroup(
            items = genderList,
            selected = gender,
            onSelectedItemChange = onGenderChange
        )
    }
}

@Composable
fun StudentUserInfo(
    studentNumber: String,
    major: String,
    isMajorDropdownExpanded: Boolean,
    modifier: Modifier = Modifier,
    onStudentNumberChange: (String) -> Unit = {},
    onMajorChange: (String) -> Unit = {},
    onMajorDropdownExpandedChange: (Boolean) -> Unit = { _ -> }
) {
    Column(
        modifier = modifier.padding(vertical = 16.dp, horizontal = 32.dp)
    ) {
        KoinUserInfoBasicItem(
            title = stringResource(R.string.user_info_student_info_student_number),
            value = studentNumber,
            onValueChange = onStudentNumberChange
        )

        Spacer(modifier = Modifier.height(32.dp))

        KoinUserInfoDropdown(
            text = major,
            hint = stringResource(R.string.user_info_student_info_major),
            isSelected = major.isNotBlank(),
            isDropdownExpanded = isMajorDropdownExpanded,
            items = majorStringList,
            onItemSelected = {
                onMajorChange(majorStringList[it])
            },
            onDropdownExpandChange = {
                onMajorDropdownExpandedChange(it)
            }
        )
    }
}

@Composable
fun UserInfoPhoneNumber(
    phoneNumber: String,
    phoneNumberState: PhoneNumber,
    isPhoneNumberChanged: Boolean,
    onPhoneNumberChange: (String) -> Unit = {},
    onRequestVerificationCode: () -> Unit = {}
) {
    KoinUserInfoWithButtonItem(
        title = stringResource(R.string.user_info_general_user_info_phone_number),
        value = phoneNumber,
        buttonText = stringResource(R.string.user_info_general_user_info_phone_number_button),
        onValueChange = onPhoneNumberChange,
        onButtonAction = onRequestVerificationCode,
        buttonEnabled = isPhoneNumberChanged,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )

    Box(modifier = Modifier.height(32.dp)) {
        when (phoneNumberState) {
            is PhoneNumber.CountExceeded -> {
                KoinUserInfoTextFieldAlert(
                    text = stringResource(R.string.user_info_phone_number_limit_exceeded),
                    state = KoinUserInfoTextFieldAlertState.Error
                )
            }

            is PhoneNumber.Sent -> {
                Row {
                    KoinUserInfoTextFieldAlert(
                        text = stringResource(R.string.user_info_code_correct),
                        state = KoinUserInfoTextFieldAlertState.Success
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(
                            R.string.user_info_phone_number_sent_remain_count,
                            phoneNumberState.remainingCount,
                            phoneNumberState.totalCount
                        ),
                        style = KoinTheme.typography.regular12,
                        color = KoinTheme.colors.neutral500
                    )
                }
            }

            is PhoneNumber.AlreadySignedUp -> {
                KoinUserInfoTextFieldAlert(
                    text = stringResource(R.string.user_info_phone_number_already_signed_up),
                    state = KoinUserInfoTextFieldAlertState.Error
                )
            }

            is PhoneNumber.WrongFormat -> {
                KoinUserInfoTextFieldAlert(
                    text = stringResource(R.string.user_info_phone_number_wrong_format),
                    state = KoinUserInfoTextFieldAlertState.Warning
                )
            }

            PhoneNumber.None,
            PhoneNumber.Available,
            is PhoneNumber.Failed -> {}
        }
    }
}

@Composable
fun UserInfoVerificationCodeVerification(
    verificationCode: String,
    verificationCodeState: VerificationCode,
    verificationTimeLeft: Int,
    onVerificationCodeChange: (String) -> Unit = {},
    checkVerificationCode: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            KoinUserInfoBasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = verificationCode,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                onValueChange = { onVerificationCodeChange(it) },
                hint = stringResource(R.string.user_info_code_field_hint),
                enabled = verificationCodeState !is VerificationCode.Valid
            )

            if (verificationCodeState != VerificationCode.Valid) {
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                    Text(
                        modifier = Modifier.padding(end = if (verificationCode.isBlank()) 8.dp else 28.dp),
                        text = verificationTimeLeft.secondToMinute(),
                        style = KoinTheme.typography.regular14,
                        color = KoinTheme.colors.neutral500
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            FilledButton(
                modifier = Modifier
                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                text = stringResource(R.string.user_info_code_check),
                textStyle = KoinTheme.typography.regular10,
                enabled = verificationCode.isNotBlank() && verificationCodeState != VerificationCode.Valid,
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                onClick = {
                    checkVerificationCode()
                }
            )
        }
    }

    when (verificationCodeState) {
        VerificationCode.None -> { }

        VerificationCode.Valid -> {
            KoinUserInfoTextFieldAlert(
                text = stringResource(R.string.user_info_code_correct),
                state = KoinUserInfoTextFieldAlertState.Success
            )
        }

        VerificationCode.NotValid -> {
            KoinUserInfoTextFieldAlert(
                text = stringResource(R.string.user_info_code_incorrect),
                state = KoinUserInfoTextFieldAlertState.Warning
            )
        }
        VerificationCode.Expired -> {
            KoinUserInfoTextFieldAlert(
                text = stringResource(R.string.user_info_code_timeout),
                state = KoinUserInfoTextFieldAlertState.Warning
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserInfoEditScreenImplPreview() {
    KoinTheme {
        Column {
            UserInfoHeader(stringResource(R.string.user_info_general_user_info_header))
            GeneralUserInfo(
                loginId = "20221234",
                name = "홍길동",
                nickName = "홍길동",
                phoneNumber = "010-1234-5678",
                email = "",
                gender = 0,
                phoneNumberState = PhoneNumber.None,
                verificationCodeState = VerificationCode.NotValid,
                verificationCode = "",
                verificationTimeLeft = 180,
                isPhoneNumberChanged = true,
                isNicknameChanged = true
            )
        }
    }
}
