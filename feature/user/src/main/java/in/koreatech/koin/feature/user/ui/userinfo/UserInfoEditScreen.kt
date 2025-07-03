package `in`.koreatech.koin.feature.user.ui.userinfo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.FilledButtonColors
import `in`.koreatech.koin.core.designsystem.component.dialog.ChoiceDialog
import `in`.koreatech.koin.core.designsystem.component.topbar.KoinTopAppBar
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.util.KRPhoneNumberVisualTransformation
import `in`.koreatech.koin.core.util.secondToMinute
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.UserType
import `in`.koreatech.koin.feature.user.NICKNAME_MAX_LENGTH
import `in`.koreatech.koin.feature.user.PHONE_NUMBER_LENGTH
import `in`.koreatech.koin.feature.user.R
import `in`.koreatech.koin.feature.user.VERIFICATION_CODE_LENGTH
import `in`.koreatech.koin.feature.user.component.KoinUserBasicItem
import `in`.koreatech.koin.feature.user.component.KoinUserBasicTextField
import `in`.koreatech.koin.feature.user.component.KoinUserDropdown
import `in`.koreatech.koin.feature.user.component.KoinUserDropdownArrowDirection
import `in`.koreatech.koin.feature.user.component.KoinUserSingleChoiceRadioGroup
import `in`.koreatech.koin.feature.user.component.KoinUserTextFieldAlert
import `in`.koreatech.koin.feature.user.component.KoinUserTextFieldAlertState
import `in`.koreatech.koin.feature.user.component.KoinUserWithButtonItem
import `in`.koreatech.koin.feature.user.component.UserInfoHeader
import `in`.koreatech.koin.feature.user.genderList
import `in`.koreatech.koin.feature.user.model.NicknameState
import `in`.koreatech.koin.feature.user.model.VerificationCodeState
import `in`.koreatech.koin.feature.user.model.VerificationMethodState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoEditScreen(
    modifier: Modifier = Modifier,
    viewModel: UserInfoEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.collectAsState()
    val userState = uiState.userState
    val context = LocalContext.current

    viewModel.collectSideEffect {
        handleSideEffect(
            sideEffect = it,
            context = context,
            startTimer = viewModel::startTimer,
            stopTimer = viewModel::stopTimer
        )
    }

    Scaffold(
        topBar = {
            KoinTopAppBar(
                title = stringResource(R.string.user_info_title),
                onNavigationIconClick = {
                    (context as Activity).finish()
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
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.padding(contentPadding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
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
                        viewModel.withdraw()
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
                loginId = userState.loginId,
                name = userState.name,
                isNameValid = uiState.isNameValid,
                nickName = userState.nickname,
                isNicknameValid = uiState.isNicknameValid,
                phoneNumber = userState.phoneNumber,
                email = userState.email,
                isEmailValid = uiState.isEmailValid,
                gender = when (userState.gender) {
                    Gender.Man -> 0
                    Gender.Woman -> 1
                    else -> null
                },
                nicknameState = uiState.nicknameState,
                phoneNumberState = uiState.phoneNumberState,
                verificationCodeState = uiState.verificationCodeState,
                verificationCode = uiState.verificationCode,
                verificationTimeLeft = uiState.verificationTimeLeft,
                isPhoneNumberChanged = uiState.isPhoneNumberChanged,
                isNicknameChanged = uiState.isNicknameChanged,
                userType = uiState.userType,
                onLoginIdChange = { viewModel.updateLoginId(it) },
                onNameChange = { viewModel.updateName(it) },
                onNicknameChange = { viewModel.updateNickname(it) },
                onPhoneNumberChange = { viewModel.updatePhoneNumber(it) },
                onEmailChange = { viewModel.updateEmail(it) },
                onGenderChange = { viewModel.updateGender(it) },
                onRequestVerificationCode = { viewModel.checkPhoneNumber() },
                onVerificationCodeChange = { viewModel.updateVerificationCode(it) },
                checkVerificationCode = { viewModel.checkVerificationCode() },
                onNicknameDuplicateCheck = { viewModel.checkNicknameDuplicate() }
            )

            if (uiState.userType == UserType.STUDENT || uiState.userType == UserType.COUNCIL) {
                UserInfoHeader(stringResource(R.string.user_info_student_info_header))

                StudentUserInfo(
                    studentNumber = userState.studentNumber,
                    isStudentNumberValid = uiState.isStudentNumberValid,
                    major = userState.major,
                    majorList = uiState.majorList.toImmutableList(),
                    isMajorDropdownExpanded = uiState.isMajorDropdownExpanded,
                    onStudentNumberChange = { viewModel.updateStudentNumber(it) },
                    onMajorChange = { viewModel.updateMajor(it) },
                    onMajorDropdownExpandedChange = { viewModel.updateMajorDropdownExpanded(it) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Spacer(modifier = Modifier.weight(1f))

            FilledButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                text = stringResource(R.string.user_info_save),
                textStyle = KoinTheme.typography.medium15,
                enabled = uiState.canSave,
                onClick = viewModel::requestUserInfoEdit
            )

            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
        }
    }
}

@Composable
fun GeneralUserInfo(
    loginId: String,
    name: String,
    isNameValid: Boolean,
    nickName: String,
    isNicknameValid: Boolean,
    phoneNumber: String,
    email: String,
    isEmailValid: Boolean,
    gender: Int?,
    nicknameState: NicknameState,
    phoneNumberState: VerificationMethodState,
    verificationCodeState: VerificationCodeState,
    verificationCode: String,
    verificationTimeLeft: Int,
    isPhoneNumberChanged: Boolean,
    isNicknameChanged: Boolean,
    userType: UserType,
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
        KoinUserBasicItem(
            title = stringResource(R.string.user_info_general_user_info_login_id),
            value = loginId,
            onValueChange = onLoginIdChange,
            readOnly = true
        )

        Box(
            modifier = Modifier.height(32.dp)
        ) {
            Text(
                text = stringResource(R.string.user_info_general_user_info_login_id_hint),
                style = KoinTheme.typography.regular12,
                color = KoinTheme.colors.neutral400
            )
        }

        KoinUserBasicItem(
            title = stringResource(R.string.user_info_general_user_info_name),
            value = name,
            onValueChange = onNameChange
        )

        Box(
            modifier = Modifier.height(32.dp)
        ) {
            if (!isNameValid && name.isNotBlank()) {
                KoinUserTextFieldAlert(
                    text = stringResource(R.string.user_info_name_invalid),
                    state = KoinUserTextFieldAlertState.Warning
                )
            }
        }

        UserInfoNickname(
            nickName = nickName,
            isNicknameChanged = isNicknameChanged,
            isNicknameValid = isNicknameValid,
            nicknameState = nicknameState,
            onNicknameChange = onNicknameChange,
            onNicknameDuplicateCheck = onNicknameDuplicateCheck
        )

        UserInfoPhoneNumber(
            phoneNumber = phoneNumber,
            phoneNumberState = phoneNumberState,
            verificationCodeState = verificationCodeState,
            isPhoneNumberChanged = isPhoneNumberChanged,
            onPhoneNumberChange = onPhoneNumberChange,
            onRequestVerificationCode = onRequestVerificationCode
        )

        if (phoneNumberState is VerificationMethodState.Sent) {
            UserInfoVerificationCodeVerification(
                verificationCode = verificationCode,
                verificationCodeState = verificationCodeState,
                verificationTimeLeft = verificationTimeLeft,
                onVerificationCodeChange = onVerificationCodeChange,
                checkVerificationCode = checkVerificationCode
            )
        }

        if (userType == UserType.STUDENT || userType == UserType.COUNCIL) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                KoinUserBasicItem(
                    modifier = Modifier.weight(1f),
                    title = stringResource(R.string.user_info_general_user_info_email),
                    value = email.removeSuffix("@koreatech.ac.kr"),
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
            KoinUserBasicItem(
                title = stringResource(R.string.user_info_general_user_info_email),
                value = email,
                onValueChange = onEmailChange
            )
        }

        Box(
            modifier = Modifier.height(32.dp)
        ) {
            if (!isEmailValid) {
                KoinUserTextFieldAlert(
                    text = stringResource(R.string.user_info_email_wrong_format),
                    state = KoinUserTextFieldAlertState.Warning
                )
            }
        }

        Text(
            text = stringResource(R.string.user_info_general_user_info_gender),
            style = KoinTheme.typography.regular16
        )

        Spacer(modifier = Modifier.height(8.dp))

        KoinUserSingleChoiceRadioGroup(
            items = genderList,
            selected = gender,
            onSelectedItemChange = onGenderChange
        )
    }
}

@Composable
fun StudentUserInfo(
    studentNumber: String,
    isStudentNumberValid: Boolean,
    major: String,
    majorList: ImmutableList<String>,
    isMajorDropdownExpanded: Boolean,
    modifier: Modifier = Modifier,
    onStudentNumberChange: (String) -> Unit = {},
    onMajorChange: (String) -> Unit = {},
    onMajorDropdownExpandedChange: (Boolean) -> Unit = { _ -> }
) {
    Column(
        modifier = modifier.padding(vertical = 16.dp, horizontal = 32.dp)
    ) {
        KoinUserBasicItem(
            title = stringResource(R.string.user_info_student_info_student_number),
            value = studentNumber,
            onValueChange = onStudentNumberChange
        )

        Box(
            modifier = Modifier.height(32.dp)
        ) {
            if (!isStudentNumberValid) {
                Spacer(modifier = Modifier.height(8.dp))

                KoinUserTextFieldAlert(
                    text = stringResource(R.string.user_info_student_number_wrong_format),
                    state = KoinUserTextFieldAlertState.Warning
                )
            }
        }

        KoinUserDropdown(
            text = major,
            hint = stringResource(R.string.user_info_student_info_major),
            isSelected = major.isNotBlank(),
            isDropdownExpanded = isMajorDropdownExpanded,
            items = majorList,
            arrowDirection = KoinUserDropdownArrowDirection.UP,
            onItemSelected = {
                onMajorChange(majorList[it])
            },
            onDropdownExpandChange = {
                onMajorDropdownExpandedChange(it)
            }
        )
    }
}

@Composable
fun UserInfoNickname(
    nickName: String,
    isNicknameChanged: Boolean,
    isNicknameValid: Boolean,
    nicknameState: NicknameState,
    onNicknameChange: (String) -> Unit = {},
    onNicknameDuplicateCheck: () -> Unit = {}
) {
    KoinUserWithButtonItem(
        title = stringResource(R.string.user_info_general_user_info_nickname),
        value = nickName,
        buttonText = stringResource(R.string.user_info_general_user_info_nickname_button),
        maxLength = NICKNAME_MAX_LENGTH,
        onValueChange = onNicknameChange,
        onButtonAction = onNicknameDuplicateCheck,
        buttonEnabled = isNicknameChanged && nicknameState !is NicknameState.NicknameAvailable
    )

    Box(modifier = Modifier.height(32.dp)) {
        if (!isNicknameValid) {
            KoinUserTextFieldAlert(
                text = stringResource(R.string.user_info_nickname_wrong_format),
                state = KoinUserTextFieldAlertState.Warning
            )
        }

        when (nicknameState) {
            is NicknameState.NicknameAvailable -> {
                KoinUserTextFieldAlert(
                    text = stringResource(R.string.user_info_nickname_available),
                    state = KoinUserTextFieldAlertState.Success
                )
            }

            is NicknameState.NicknameDuplicated -> {
                KoinUserTextFieldAlert(
                    text = stringResource(R.string.user_info_nickname_duplicated),
                    state = KoinUserTextFieldAlertState.Warning
                )
            }

            NicknameState.Failed -> {}
            NicknameState.None -> {}
        }
    }
}

@Composable
fun UserInfoPhoneNumber(
    phoneNumber: String,
    phoneNumberState: VerificationMethodState,
    verificationCodeState: VerificationCodeState,
    isPhoneNumberChanged: Boolean,
    onPhoneNumberChange: (String) -> Unit = {},
    onRequestVerificationCode: () -> Unit = {}
) {
    KoinUserWithButtonItem(
        title = stringResource(R.string.user_info_general_user_info_phone_number),
        value = phoneNumber,
        buttonText = stringResource(
            if (phoneNumberState is VerificationMethodState.Sent) {
                R.string.user_info_general_user_info_phone_number_resend
            } else {
                R.string.user_info_general_user_info_phone_number_send
            }
        ),
        onValueChange = onPhoneNumberChange,
        onButtonAction = onRequestVerificationCode,
        buttonEnabled = isPhoneNumberChanged && verificationCodeState !is VerificationCodeState.Valid && phoneNumber.isNotBlank(),
        maxLength = PHONE_NUMBER_LENGTH,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        visualTransformation = KRPhoneNumberVisualTransformation()
    )

    Box(modifier = Modifier.height(32.dp)) {
        when (phoneNumberState) {
            is VerificationMethodState.CountExceeded -> {
                KoinUserTextFieldAlert(
                    text = stringResource(R.string.user_info_phone_number_limit_exceeded),
                    state = KoinUserTextFieldAlertState.Error
                )
            }

            is VerificationMethodState.Sent -> {
                Row {
                    KoinUserTextFieldAlert(
                        text = stringResource(R.string.user_info_phone_number_sent),
                        state = KoinUserTextFieldAlertState.Success
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

            is VerificationMethodState.AlreadySignedUp -> {
                KoinUserTextFieldAlert(
                    text = stringResource(R.string.user_info_phone_number_already_signed_up),
                    state = KoinUserTextFieldAlertState.Error
                )
            }

            is VerificationMethodState.WrongFormat -> {
                KoinUserTextFieldAlert(
                    text = stringResource(R.string.user_info_phone_number_wrong_format),
                    state = KoinUserTextFieldAlertState.Warning
                )
            }

            VerificationMethodState.NotFound,
            VerificationMethodState.None,
            VerificationMethodState.Available,
            is VerificationMethodState.Failed -> {}
        }
    }
}

@Composable
fun UserInfoVerificationCodeVerification(
    verificationCode: String,
    verificationCodeState: VerificationCodeState,
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
            KoinUserBasicTextField(
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
                maxLength = VERIFICATION_CODE_LENGTH,
                enabled = verificationCodeState !is VerificationCodeState.Valid
            )

            if (verificationCodeState != VerificationCodeState.Valid) {
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
                enabled = verificationCode.isNotBlank() && verificationCodeState != VerificationCodeState.Valid,
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                onClick = {
                    checkVerificationCode()
                }
            )
        }
    }

    when (verificationCodeState) {
        VerificationCodeState.None -> { }
        VerificationCodeState.Valid -> {
            KoinUserTextFieldAlert(
                text = stringResource(R.string.user_info_code_correct),
                state = KoinUserTextFieldAlertState.Success
            )
        }

        VerificationCodeState.NotValid -> {
            KoinUserTextFieldAlert(
                text = stringResource(R.string.user_info_code_incorrect),
                state = KoinUserTextFieldAlertState.Warning
            )
        }
        VerificationCodeState.Expired -> {
            KoinUserTextFieldAlert(
                text = stringResource(R.string.user_info_code_timeout),
                state = KoinUserTextFieldAlertState.Warning
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

fun handleSideEffect(
    sideEffect: UserInfoEditSideEffect,
    context: Context,
    startTimer: () -> Unit = {},
    stopTimer: () -> Unit = {}
) {
    when (sideEffect) {
        UserInfoEditSideEffect.UpdateUserInfoSuccess -> {
            (context as Activity).finish()
        }

        UserInfoEditSideEffect.StartTimer -> startTimer()
        UserInfoEditSideEffect.StopTimer -> stopTimer()
        UserInfoEditSideEffect.InvalidDataError -> {
            Toast.makeText(context, R.string.user_info_invalid_data_error, Toast.LENGTH_SHORT).show()
        }
        UserInfoEditSideEffect.NicknameOrEmailConflictError -> {
            // API sent 409 when either nickname or email is already in use.
            // Since we allow the user to press the save button only after the nickname conflict check passes,
            // we assume the conflict is due to the email.
            Toast.makeText(context, R.string.user_info_email_conflict_error, Toast.LENGTH_SHORT).show()
        }
        UserInfoEditSideEffect.PhoneNumberValidateRequiredError -> {
            Toast.makeText(context, R.string.user_info_phone_number_validate_required_error, Toast.LENGTH_SHORT).show()
        }
        UserInfoEditSideEffect.UnknownError -> {
            Toast.makeText(context, R.string.user_info_unknown_error, Toast.LENGTH_SHORT).show()
        }
        UserInfoEditSideEffect.UnknownUserError -> {
            Toast.makeText(context, R.string.user_info_user_not_found_error, Toast.LENGTH_SHORT).show()
        }

        UserInfoEditSideEffect.WithdrawalError -> {
            Toast.makeText(context, R.string.user_info_withdraw_error, Toast.LENGTH_SHORT).show()
        }
        UserInfoEditSideEffect.WithdrawalSuccess -> {
            Toast.makeText(context, R.string.user_info_withdraw_success, Toast.LENGTH_SHORT).show()
            Intent(Intent.ACTION_VIEW).apply {
                data = "koin://main/activity".toUri()
            }.let {
                context.startActivity(it)
            }
            (context as Activity).finish()
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
                isNicknameValid = true,
                phoneNumber = "010-1234-5678",
                email = "",
                gender = 0,
                nicknameState = NicknameState.None,
                phoneNumberState = VerificationMethodState.None,
                verificationCodeState = VerificationCodeState.NotValid,
                verificationCode = "",
                verificationTimeLeft = 180,
                isPhoneNumberChanged = true,
                isNicknameChanged = true,
                userType = UserType.STUDENT,
                isNameValid = true,
                isEmailValid = true
            )
        }
    }
}
