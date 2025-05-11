package `in`.koreatech.koin.feature.signup.ui.verification

import android.content.Intent
import androidx.compose.foundation.layout.Box
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
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.domain.constant.CONTACT_URL
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.feature.signup.R
import `in`.koreatech.koin.feature.signup.SIGN_UP_PHONE_NUMBER_MAX_LENGTH
import `in`.koreatech.koin.feature.signup.SIGN_UP_VERIFICATION_CODE_MAX_LENGTH
import `in`.koreatech.koin.feature.signup.component.KoinSignUpBasicTextField
import `in`.koreatech.koin.feature.signup.component.KoinSignUpProgressHeader
import `in`.koreatech.koin.feature.signup.component.KoinSignUpProgressIndicator
import `in`.koreatech.koin.feature.signup.component.KoinSignUpSingleChoiceRadioGroup
import `in`.koreatech.koin.feature.signup.component.KoinSignUpTextFieldAlert
import `in`.koreatech.koin.feature.signup.component.KoinSignUpTextFieldAlertState
import `in`.koreatech.koin.feature.signup.util.secondToMinute
import kotlinx.collections.immutable.persistentListOf
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

val genderList = persistentListOf("남성", "여성")

@Composable
fun SignUpVerification(
    modifier: Modifier = Modifier,
    viewModel: SignUpVerificationViewModel = hiltViewModel(),
    navigateToNextScreen: (name: String, phoneNumber: String, gender: String) -> Unit = { _, _, _ -> }
) {
    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect {
        handleSideEffect(
            sideEffect = it,
            onStartTimer = {
                viewModel.startTimer()
            },
            onStopTimer = {
                viewModel.stopTimer()
            }
        )
    }

    SignUpVerificationImpl(
        step = uiState.currentStep,
        name = uiState.name,
        gender = uiState.gender,
        phoneNumber = uiState.phoneNumber,
        phoneNumberState = uiState.phoneNumberState,
        verificationCode = uiState.verificationCode,
        verificationCodeState = uiState.verificationCodeState,
        verificationTimeLeft = uiState.verificationTimeLeft,
        enabled = uiState.enabled,
        onNameChange = { viewModel.setName(it) },
        onGenderChange = { viewModel.setGender(it) },
        onPhoneNumberChange = { viewModel.setPhoneNumber(it) },
        onVerificationCodeSent = { viewModel.checkPhoneNumber() },
        onVerificationCodeChange = { viewModel.setVerificationCode(it) },
        checkVerificationCode = { viewModel.checkVerificationCode() },
        modifier = modifier,
        navigateToNextScreen = { name, phoneNumber, gender ->
            navigateToNextScreen(name, phoneNumber, if (gender is Gender.Man) "0" else "1")
        }
    )
}

@Composable
fun SignUpVerificationImpl(
    step: SignUpVerificationStep,
    name: String,
    gender: Gender,
    phoneNumber: String,
    phoneNumberState: SignupContinuationState?,
    verificationCode: String,
    verificationCodeState: SignupContinuationState?,
    verificationTimeLeft: Int,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onNameChange: (String) -> Unit = {},
    onGenderChange: (Int) -> Unit = {},
    onPhoneNumberChange: (String) -> Unit = {},
    onVerificationCodeSent: () -> Unit = {},
    onVerificationCodeChange: (String) -> Unit = {},
    checkVerificationCode: () -> Unit = {},
    navigateToNextScreen: (name: String, phoneNumber: String, gender: Gender) -> Unit = { _, _, _ -> }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(bottom = 40.dp)
    ) {
        KoinSignUpProgressHeader(
            text = stringResource(R.string.sign_up_verification),
            currentStep = 2,
            maxStep = 4
        )

        Spacer(modifier = Modifier.height(8.dp))

        KoinSignUpProgressIndicator(
            currentStep = 2,
            maxStep = 4
        )

        Spacer(modifier = Modifier.height(64.dp))

        SignUpVerificationInitialStep(
            name = name,
            gender = gender,
            onNameChange = onNameChange,
            onGenderChange = onGenderChange
        )

        if (step >= SignUpVerificationStep.PHONE_NUMBER) {
            SignUpVerificationPhoneNumberStep(
                phoneNumber = phoneNumber,
                phoneNumberState = phoneNumberState,
                verificationCodeState = verificationCodeState,
                onPhoneNumberChange = onPhoneNumberChange,
                onVerificationCodeSent = onVerificationCodeSent
            )
        }

        if (step >= SignUpVerificationStep.VERIFICATION_CODE) {
            SignUpVerificationCodeVerificationStep(
                verificationCode = verificationCode,
                verificationCodeState = verificationCodeState,
                verificationTimeLeft = verificationTimeLeft,
                onVerificationCodeChange = onVerificationCodeChange,
                checkVerificationCode = checkVerificationCode
            )
        }

        Spacer(modifier = Modifier.height(64.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.weight(1f))

        FilledButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.sign_up_next),
            enabled = enabled,
            contentPadding = PaddingValues(12.dp),
            onClick = { navigateToNextScreen(name, phoneNumber, gender) }
        )
    }
}

@Composable
private fun SignUpVerificationInitialStep(
    name: String,
    gender: Gender,
    modifier: Modifier = Modifier,
    onNameChange: (String) -> Unit = {},
    onGenderChange: (Int) -> Unit = {}
) {
    Column(modifier = modifier.padding(horizontal = 8.dp)) {
        Text(
            text = stringResource(R.string.sign_up_name_gender_title),
            style = KoinTheme.typography.medium16
        )

        Spacer(modifier = Modifier.height(16.dp))

        KoinSignUpBasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = name,
            onValueChange = { onNameChange(it) },
            hint = stringResource(R.string.sign_up_name_field_hint),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        KoinSignUpSingleChoiceRadioGroup(
            items = genderList,
            selected = when (gender) {
                Gender.Man -> 0
                Gender.Woman -> 1
                Gender.Unknown -> null
            },
            onSelectedItemChange = {
                onGenderChange(it)
            }
        )
    }
}

@Composable
fun SignUpVerificationPhoneNumberStep(
    phoneNumber: String,
    phoneNumberState: SignupContinuationState?,
    verificationCodeState: SignupContinuationState?,
    modifier: Modifier = Modifier,
    onPhoneNumberChange: (String) -> Unit = {},
    onVerificationCodeSent: () -> Unit = {}
) {
    Spacer(modifier = Modifier.height(64.dp))

    Text(
        text = stringResource(R.string.sign_up_phone_number_title),
        style = KoinTheme.typography.medium16
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically
    ) {
        KoinSignUpBasicTextField(
            modifier = Modifier.weight(1f),
            value = phoneNumber,
            maxLength = SIGN_UP_PHONE_NUMBER_MAX_LENGTH,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            onValueChange = { onPhoneNumberChange(it) },
            hint = stringResource(R.string.sign_up_phone_number_field_hint),
            showTrailingClearButton = verificationCodeState !is SignupContinuationState.SmsCodeIsValidated
        )

        Spacer(modifier = Modifier.width(16.dp))

        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            FilledButton(
                modifier = Modifier.widthIn(min = 86.dp),
                text = if (phoneNumberState is SignupContinuationState.RequestedSmsValidationWithRemainingCount) {
                    stringResource(R.string.sign_up_phone_number_resend_verification)
                } else {
                    stringResource(R.string.sign_up_phone_number_send_verification)
                },
                textStyle = KoinTheme.typography.regular10,
                enabled = (phoneNumber.isNotBlank() || phoneNumberState != SignupContinuationState.SmsCodeRequestCountIsExceeded) && verificationCodeState !is SignupContinuationState.SmsCodeIsValidated,
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                onClick = {
                    onVerificationCodeSent()
                }
            )
        }
    }

    when (phoneNumberState) {
        SignupContinuationState.PhoneNumberDuplicated -> PhoneNumberDuplicateMessage()
        SignupContinuationState.CheckPhoneNumberFormat -> PhoneNumberInvalidMessage()
        SignupContinuationState.SmsCodeRequestCountIsExceeded -> PhoneNumberRequestCountExceeded()
        is SignupContinuationState.RequestedSmsValidationWithRemainingCount -> {
            VerificationCodeSentSuccessMessage(
                remainingCount = phoneNumberState.remainingCount,
                totalCount = phoneNumberState.totalCount
            )
        }

        else -> {
            // Do nothing
        }
    }
}

@Composable
fun SignUpVerificationCodeVerificationStep(
    verificationCode: String,
    verificationCodeState: SignupContinuationState?,
    verificationTimeLeft: Int,
    onVerificationCodeChange: (String) -> Unit = {},
    checkVerificationCode: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Spacer(modifier = Modifier.height(24.dp))

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
            KoinSignUpBasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = verificationCode,
                maxLength = SIGN_UP_VERIFICATION_CODE_MAX_LENGTH,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                onValueChange = { onVerificationCodeChange(it) },
                hint = stringResource(R.string.sign_up_verification_code_field_hint),
                enabled = verificationCodeState !is SignupContinuationState.SmsCodeIsValidated
            )

            if (verificationCodeState == null || verificationCodeState != SignupContinuationState.SmsCodeIsValidated) {
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
                modifier = Modifier.widthIn(min = 86.dp),
                text = stringResource(R.string.sign_up_verification_code_check),
                textStyle = KoinTheme.typography.regular10,
                enabled = verificationCode.isNotBlank() && verificationCodeState != SignupContinuationState.SmsCodeIsValidated,
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                onClick = {
                    checkVerificationCode()
                }
            )
        }
    }

    if (verificationCodeState != null) {
        KoinSignUpTextFieldAlert(
            text = when (verificationCodeState) {
                SignupContinuationState.SmsCodeIsValidated -> stringResource(R.string.sign_up_verification_code_correct)
                SignupContinuationState.SmsCodeIsNotValidate -> stringResource(R.string.sign_up_verification_code_incorrect)
                SignupContinuationState.SmsCodeIsExpired -> stringResource(R.string.sign_up_verification_code_timeout)
                else -> "" // Not used
            },
            state = when (verificationCodeState) {
                SignupContinuationState.SmsCodeIsValidated -> KoinSignUpTextFieldAlertState.Success
                SignupContinuationState.SmsCodeIsNotValidate -> KoinSignUpTextFieldAlertState.Warning
                SignupContinuationState.SmsCodeIsExpired -> KoinSignUpTextFieldAlertState.Warning
                else -> KoinSignUpTextFieldAlertState.Warning // Not used
            }
        )
    }
}

@Composable
private fun VerificationCodeSentSuccessMessage(
    remainingCount: Int,
    totalCount: Int
) {
    Row {
        KoinSignUpTextFieldAlert(
            text = stringResource(R.string.sign_up_verification_code_sent),
            state = KoinSignUpTextFieldAlertState.Success
        )

        Text(" ") // Space

        Text(
            text = stringResource(
                R.string.sign_up_verification_code_remaining_count,
                remainingCount,
                totalCount
            ),
            color = KoinTheme.colors.neutral500,
            style = KoinTheme.typography.regular12
        )
    }
}

@Composable
private fun PhoneNumberDuplicateMessage() {
    val context = LocalContext.current

    Spacer(modifier = Modifier.height(8.dp))

    Column {
        Row {
            KoinSignUpTextFieldAlert(
                text = stringResource(R.string.sign_up_phone_number_duplicated),
                state = KoinSignUpTextFieldAlertState.Error
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .noRippleClickable {
                        val intent =
                            Intent(Intent.ACTION_VIEW).apply {
                                data = "koin://login/login".toUri()
                            }
                        context.startActivity(intent)
                    },
                text = stringResource(R.string.sign_up_phone_number_already_sign_up),
                color = KoinTheme.colors.primary500,
                style = KoinTheme.typography.regular12
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Text(
                text = stringResource(R.string.sign_up_phone_number_never_sign_up_before),
                color = KoinTheme.colors.neutral500,
                style = KoinTheme.typography.regular12
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                modifier = Modifier.noRippleClickable {
                    val intent =
                        Intent(Intent.ACTION_VIEW).apply {
                            data = CONTACT_URL.toUri()
                        }
                    context.startActivity(intent)
                },
                text = stringResource(R.string.sign_up_phone_number_never_sign_up_before_inquiry),
                color = KoinTheme.colors.primary500,
                style = KoinTheme.typography.regular12
            )
        }
    }
}

@Composable
private fun PhoneNumberInvalidMessage() {
    KoinSignUpTextFieldAlert(
        text = stringResource(R.string.sign_up_phone_number_invalid),
        state = KoinSignUpTextFieldAlertState.Warning
    )
}

@Composable
private fun PhoneNumberRequestCountExceeded() {
    KoinSignUpTextFieldAlert(
        text = stringResource(R.string.sign_up_phone_number_code_request_exceeded),
        state = KoinSignUpTextFieldAlertState.Error
    )
}

private fun handleSideEffect(
    sideEffect: SignUpVerificationSideEffect,
    onStartTimer: () -> Unit,
    onStopTimer: () -> Unit
) {
    when (sideEffect) {
        SignUpVerificationSideEffect.StartTimer -> onStartTimer()
        SignUpVerificationSideEffect.StopTimer -> onStopTimer()
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpVerificationPreview() {
    SignUpVerificationImpl(
        name = "홍길동",
        gender = Gender.Man,
        phoneNumber = "01012345678",
        phoneNumberState = null,
        verificationCode = "123456",
        verificationCodeState = null,
        verificationTimeLeft = 180,
        step = SignUpVerificationStep.INITIAL,
        enabled = true
    )
}
