package `in`.koreatech.koin.feature.findpassword.ui.verification

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsBottomHeight
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
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.util.secondToMinute
import `in`.koreatech.koin.domain.model.user.PhoneNumber
import `in`.koreatech.koin.domain.model.user.VerificationCode
import `in`.koreatech.koin.feature.findpassword.PHONE_NUMBER_LENGTH
import `in`.koreatech.koin.feature.findpassword.R
import `in`.koreatech.koin.feature.findpassword.VERIFICATION_CODE_LENGTH
import `in`.koreatech.koin.feature.findpassword.component.KoinFindPasswordBasicTextField
import `in`.koreatech.koin.feature.findpassword.component.KoinFindPasswordProgressHeader
import `in`.koreatech.koin.feature.findpassword.component.KoinFindPasswordProgressIndicator
import `in`.koreatech.koin.feature.findpassword.component.KoinFindPasswordTextFieldAlert
import `in`.koreatech.koin.feature.findpassword.component.KoinFindPasswordTextFieldAlertState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun FindPasswordVerification(
    viewModel: FindPasswordVerificationViewModel = hiltViewModel(),
    navigateToPasswordScreen: (loginId: String, phoneNumber: String) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        handleSideEffect(
            sideEffect = it,
            context = context,
            onStartTimer = { viewModel.startTimer() },
            onStopTimer = { viewModel.stopTimer() },
            onNavigateToChangePassword = {
                navigateToPasswordScreen(uiState.loginId, uiState.verificationMethod)
            }
        )
    }

    FindPasswordVerificationImpl(
        loginId = uiState.loginId,
        loginIdValid = uiState.loginIdValid,
        phoneNumber = uiState.verificationMethod,
        phoneNumberState = uiState.verificationMethodState,
        verificationCode = uiState.verificationCode,
        verificationCodeState = uiState.verificationCodeState,
        verificationTimeLeft = uiState.verificationTimeLeft,
        isSms = uiState.isSms,
        onLoginIdChange = { viewModel.updateLoginId(it) },
        onPhoneNumberChange = { viewModel.updatePhoneNumber(it) },
        onVerificationCodeChange = { viewModel.updateVerificationCode(it) },
        onVerificationCodeRequest = { viewModel.sendVerificationCode() },
        onVerificationCodeVerify = { viewModel.checkVerificationCode() },
        navigateToEmailScreen = { viewModel.updateIsSms(false) },
        navigateToPasswordScreen = {
            viewModel.checkIdExists()
        }
    )
}

@Composable
fun FindPasswordVerificationImpl(
    loginId: String,
    loginIdValid: Boolean,
    phoneNumber: String,
    phoneNumberState: PhoneNumber,
    verificationCode: String,
    verificationCodeState: VerificationCode,
    verificationTimeLeft: Int,
    isSms: Boolean,
    modifier: Modifier = Modifier,
    onLoginIdChange: (String) -> Unit = {},
    onPhoneNumberChange: (String) -> Unit = {},
    onVerificationCodeChange: (String) -> Unit = {},
    onVerificationCodeRequest: () -> Unit = {},
    onVerificationCodeVerify: () -> Unit = {},
    navigateToEmailScreen: () -> Unit = {},
    navigateToPasswordScreen: () -> Unit = { }
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        KoinFindPasswordProgressHeader(
            currentStep = 1,
            maxStep = 2,
            text = stringResource(R.string.find_password_step_1)
        )

        Spacer(modifier = Modifier.height(8.dp))

        KoinFindPasswordProgressIndicator(
            currentStep = 1,
            maxStep = 2
        )

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = stringResource(R.string.find_password_id),
            style = KoinTheme.typography.medium18
        )

        Spacer(modifier = Modifier.height(12.dp))

        KoinFindPasswordBasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = loginId,
            onValueChange = onLoginIdChange,
            hint = stringResource(R.string.find_password_id_hint)
        )

        if (!loginIdValid) {
            Spacer(modifier = Modifier.height(8.dp))

            KoinFindPasswordTextFieldAlert(
                text = stringResource(R.string.find_password_id_invalid),
                state = KoinFindPasswordTextFieldAlertState.Warning
            )
        }

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = stringResource(if (isSms) R.string.find_password_phone_number else R.string.find_password_email),
            style = KoinTheme.typography.medium18
        )

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KoinFindPasswordBasicTextField(
                modifier = Modifier.weight(1f),
                value = phoneNumber,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                hint = stringResource(if (isSms) R.string.find_password_phone_number_hint else R.string.find_password_email_hint),
                onValueChange = onPhoneNumberChange,
                maxLength = if (isSms) PHONE_NUMBER_LENGTH else Int.MAX_VALUE
            )

            Spacer(modifier = Modifier.width(16.dp))

            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                FilledButton(
                    modifier = Modifier.widthIn(min = 86.dp),
                    text = stringResource(R.string.find_password_send),
                    textStyle = KoinTheme.typography.regular10,
                    contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                    onClick = onVerificationCodeRequest,
                    enabled = phoneNumber.isNotBlank() && verificationCodeState !is VerificationCode.Valid
                )
            }
        }

        when (phoneNumberState) {
            PhoneNumber.CountExceeded -> PhoneNumberRequestCountExceeded()
            is PhoneNumber.Sent -> VerificationCodeSentSuccessMessage(
                phoneNumberState.remainingCount,
                phoneNumberState.totalCount
            )

            PhoneNumber.WrongFormat -> PhoneNumberInvalidMessage()
            is PhoneNumber.Failed -> PhoneNumberNotMatch()
            PhoneNumber.None,
            PhoneNumber.AlreadySignedUp,
            PhoneNumber.Available -> {
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (phoneNumberState is PhoneNumber.Sent) {
            SignUpVerificationCodeVerificationStep(
                verificationCode = verificationCode,
                verificationCodeState = verificationCodeState,
                verificationTimeLeft = verificationTimeLeft,
                onVerificationCodeChange = onVerificationCodeChange,
                checkVerificationCode = onVerificationCodeVerify
            )
        }

        if (phoneNumber.isBlank() && isSms) {
            EmailMessage(
                navigateToEmailScreen = navigateToEmailScreen
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        FilledButton(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.find_password_next),
            contentPadding = PaddingValues(12.dp),
            onClick = navigateToPasswordScreen,
            enabled = verificationCodeState is VerificationCode.Valid
        )

        Spacer(modifier = Modifier.height(40.dp))

        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}

@Composable
fun SignUpVerificationCodeVerificationStep(
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
            KoinFindPasswordBasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = verificationCode,
                maxLength = VERIFICATION_CODE_LENGTH,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                onValueChange = { onVerificationCodeChange(it) },
                hint = stringResource(R.string.find_password_verification_code_field_hint),
                enabled = verificationCodeState !is VerificationCode.Valid
            )

            if (verificationCodeState is VerificationCode.None || verificationCodeState != VerificationCode.Valid) {
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
                text = stringResource(R.string.find_password_verification_code_check),
                textStyle = KoinTheme.typography.regular10,
                enabled = verificationCode.isNotBlank() && verificationCodeState != VerificationCode.Valid,
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                onClick = {
                    checkVerificationCode()
                }
            )
        }
    }

    if (verificationCodeState != VerificationCode.None) {
        KoinFindPasswordTextFieldAlert(
            text = when (verificationCodeState) {
                VerificationCode.Valid -> stringResource(R.string.find_password_verification_code_correct)
                VerificationCode.NotValid -> stringResource(R.string.find_password_verification_code_incorrect)
                VerificationCode.Expired -> stringResource(R.string.find_password_verification_code_timeout)
                else -> "" // Not used
            },
            state = when (verificationCodeState) {
                VerificationCode.Valid -> KoinFindPasswordTextFieldAlertState.Success
                VerificationCode.NotValid -> KoinFindPasswordTextFieldAlertState.Warning
                VerificationCode.Expired -> KoinFindPasswordTextFieldAlertState.Warning
                else -> KoinFindPasswordTextFieldAlertState.Warning // Not used
            }
        )
    }
}

@Composable
private fun EmailMessage(
    navigateToEmailScreen: () -> Unit = {}
) {
    Row {
        Text(
            text = stringResource(R.string.find_password_phone_number_not_registered),
            style = KoinTheme.typography.regular12,
            color = KoinTheme.colors.neutral500
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.noRippleClickable {
                navigateToEmailScreen()
            },
            text = stringResource(R.string.find_password_with_email),
            color = KoinTheme.colors.primary500,
            style = KoinTheme.typography.regular12
        )
    }
}

@Composable
private fun PhoneNumberInvalidMessage() {
    Spacer(modifier = Modifier.height(8.dp))

    KoinFindPasswordTextFieldAlert(
        text = stringResource(R.string.find_password_phone_number_invalid),
        state = KoinFindPasswordTextFieldAlertState.Warning
    )
}

@Composable
private fun PhoneNumberNotMatch() {
    Spacer(modifier = Modifier.height(8.dp))

    KoinFindPasswordTextFieldAlert(
        text = stringResource(R.string.find_password_phone_number_not_correct),
        state = KoinFindPasswordTextFieldAlertState.Warning
    )
}

@Composable
private fun PhoneNumberRequestCountExceeded() {
    Spacer(modifier = Modifier.height(8.dp))

    KoinFindPasswordTextFieldAlert(
        text = stringResource(R.string.find_password_phone_number_code_request_exceeded),
        state = KoinFindPasswordTextFieldAlertState.Error
    )
}

@Composable
private fun VerificationCodeSentSuccessMessage(
    remainingCount: Int,
    totalCount: Int
) {
    Row {
        KoinFindPasswordTextFieldAlert(
            text = stringResource(R.string.find_password_verification_code_sent),
            state = KoinFindPasswordTextFieldAlertState.Success
        )

        Text(" ") // Space

        Text(
            text = stringResource(
                R.string.find_password_verification_code_remaining_count,
                remainingCount,
                totalCount
            ),
            color = KoinTheme.colors.neutral500,
            style = KoinTheme.typography.regular12
        )
    }
}

fun handleSideEffect(
    sideEffect: FindPasswordVerificationSideEffect,
    context: Context,
    onStartTimer: () -> Unit = { },
    onStopTimer: () -> Unit = { },
    onNavigateToChangePassword: () -> Unit = { }
) {
    when (sideEffect) {
        FindPasswordVerificationSideEffect.StartTimer -> onStartTimer()
        FindPasswordVerificationSideEffect.StopTimer -> onStopTimer()
        FindPasswordVerificationSideEffect.NavigateToChangePassword -> onNavigateToChangePassword()
        FindPasswordVerificationSideEffect.UnknownError -> Toast.makeText(context, R.string.find_password_unknown_error, Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun FindPasswordBySmsPreview() {
    FindPasswordVerificationImpl(
        loginId = "testUser",
        loginIdValid = true,
        phoneNumber = "01012345678",
        phoneNumberState = PhoneNumber.Available,
        verificationCode = "123456",
        verificationCodeState = VerificationCode.None,
        verificationTimeLeft = 180,
        isSms = true
    )
}
