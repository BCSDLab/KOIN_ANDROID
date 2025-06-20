package `in`.koreatech.koin.feature.user.ui.findpassword.verification

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
import `in`.koreatech.koin.feature.user.PHONE_NUMBER_LENGTH
import `in`.koreatech.koin.feature.user.R
import `in`.koreatech.koin.feature.user.VERIFICATION_CODE_LENGTH
import `in`.koreatech.koin.feature.user.component.KoinUserBasicTextField
import `in`.koreatech.koin.feature.user.component.KoinUserProgressHeader
import `in`.koreatech.koin.feature.user.component.KoinUserProgressIndicator
import `in`.koreatech.koin.feature.user.component.KoinUserTextFieldAlert
import `in`.koreatech.koin.feature.user.component.KoinUserTextFieldAlertState
import `in`.koreatech.koin.feature.user.model.VerificationCodeState
import `in`.koreatech.koin.feature.user.model.VerificationMethodState
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
        verificationMethod = uiState.verificationMethod,
        verificationMethodState = uiState.verificationMethodState,
        verificationCode = uiState.verificationCode,
        verificationCodeState = uiState.verificationCodeState,
        verificationTimeLeft = uiState.verificationTimeLeft,
        isSms = uiState.isSms,
        onLoginIdChange = { viewModel.updateLoginId(it) },
        onVerificationMethodChange = { viewModel.updatePhoneNumber(it) },
        onVerificationCodeChange = { viewModel.updateVerificationCode(it) },
        onVerificationCodeRequest = { viewModel.checkVerificationMethodExists() },
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
    verificationMethod: String,
    verificationMethodState: VerificationMethodState,
    verificationCode: String,
    verificationCodeState: VerificationCodeState,
    verificationTimeLeft: Int,
    isSms: Boolean,
    modifier: Modifier = Modifier,
    onLoginIdChange: (String) -> Unit = {},
    onVerificationMethodChange: (String) -> Unit = {},
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
        KoinUserProgressHeader(
            currentStep = 1,
            maxStep = 2,
            text = stringResource(R.string.find_password_step_1)
        )

        Spacer(modifier = Modifier.height(8.dp))

        KoinUserProgressIndicator(
            currentStep = 1,
            maxStep = 2
        )

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = stringResource(R.string.find_password_id),
            style = KoinTheme.typography.medium18
        )

        Spacer(modifier = Modifier.height(12.dp))

        KoinUserBasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = loginId,
            onValueChange = onLoginIdChange,
            hint = stringResource(R.string.find_password_id_hint)
        )

        if (!loginIdValid) {
            Spacer(modifier = Modifier.height(8.dp))

            KoinUserTextFieldAlert(
                text = stringResource(R.string.find_password_id_invalid),
                state = KoinUserTextFieldAlertState.Warning
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
            KoinUserBasicTextField(
                modifier = Modifier.weight(1f),
                value = verificationMethod,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (isSms) KeyboardType.Number else KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                hint = stringResource(if (isSms) R.string.find_password_phone_number_hint else R.string.find_password_email_hint),
                onValueChange = onVerificationMethodChange,
                maxLength = if (isSms) PHONE_NUMBER_LENGTH else Int.MAX_VALUE
            )

            Spacer(modifier = Modifier.width(16.dp))

            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                FilledButton(
                    modifier = Modifier.widthIn(min = 86.dp),
                    text = if (verificationMethodState is VerificationMethodState.Sent) stringResource(R.string.find_password_resend) else stringResource(R.string.find_password_send),
                    textStyle = KoinTheme.typography.regular10,
                    contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                    onClick = onVerificationCodeRequest,
                    enabled = verificationMethod.isNotBlank() && verificationCodeState !is VerificationCodeState.Valid
                )
            }
        }

        when (verificationMethodState) {
            VerificationMethodState.CountExceeded -> VerificationMethodRequestCountExceeded()
            is VerificationMethodState.Sent -> VerificationCodeSentSuccessMessage(
                verificationMethodState.remainingCount,
                verificationMethodState.totalCount
            )

            VerificationMethodState.WrongFormat -> VerificationMethodWrongFormat(isSms)
            VerificationMethodState.NotFound -> VerificationMethodInvalidMessage(isSms)
            is VerificationMethodState.Failed,
            VerificationMethodState.None,
            VerificationMethodState.AlreadySignedUp,
            VerificationMethodState.Available -> {
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (verificationMethodState is VerificationMethodState.Sent) {
            SignUpVerificationCodeVerificationStep(
                verificationCode = verificationCode,
                verificationCodeState = verificationCodeState,
                verificationTimeLeft = verificationTimeLeft,
                onVerificationCodeChange = onVerificationCodeChange,
                checkVerificationCode = onVerificationCodeVerify
            )
        }

        if (verificationMethod.isBlank() && isSms) {
            EmailMessage(
                navigateToEmailScreen = navigateToEmailScreen
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Spacer(modifier = Modifier.weight(1f))

        FilledButton(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.find_password_next),
            contentPadding = PaddingValues(12.dp),
            onClick = navigateToPasswordScreen,
            enabled = verificationCodeState is VerificationCodeState.Valid
        )

        Spacer(modifier = Modifier.height(40.dp))

        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
    }
}

@Composable
fun SignUpVerificationCodeVerificationStep(
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
            KoinUserBasicTextField(
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
                enabled = verificationCodeState !is VerificationCodeState.Valid
            )

            if (verificationCodeState is VerificationCodeState.None || verificationCodeState != VerificationCodeState.Valid) {
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
                enabled = verificationCode.isNotBlank() && verificationCodeState != VerificationCodeState.Valid,
                contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                onClick = {
                    checkVerificationCode()
                }
            )
        }
    }

    if (verificationCodeState != VerificationCodeState.None) {
        KoinUserTextFieldAlert(
            text = when (verificationCodeState) {
                VerificationCodeState.Valid -> stringResource(R.string.find_password_verification_code_correct)
                VerificationCodeState.NotValid -> stringResource(R.string.find_password_verification_code_incorrect)
                VerificationCodeState.Expired -> stringResource(R.string.find_password_verification_code_timeout)
                else -> "" // Not used
            },
            state = when (verificationCodeState) {
                VerificationCodeState.Valid -> KoinUserTextFieldAlertState.Success
                VerificationCodeState.NotValid -> KoinUserTextFieldAlertState.Warning
                VerificationCodeState.Expired -> KoinUserTextFieldAlertState.Warning
                else -> KoinUserTextFieldAlertState.Warning // Not used
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
private fun VerificationMethodWrongFormat(
    isSms: Boolean
) {
    Spacer(modifier = Modifier.height(8.dp))

    KoinUserTextFieldAlert(
        text = stringResource(if (isSms) R.string.find_password_phone_number_wrong_format else R.string.find_password_email_wrong_format),
        state = KoinUserTextFieldAlertState.Warning
    )
}

@Composable
private fun VerificationMethodInvalidMessage(
    isSms: Boolean
) {
    Spacer(modifier = Modifier.height(8.dp))

    KoinUserTextFieldAlert(
        text = stringResource(if (isSms) R.string.find_password_phone_number_invalid else R.string.find_password_email_invalid),
        state = KoinUserTextFieldAlertState.Warning
    )
}

@Composable
private fun VerificationMethodRequestCountExceeded() {
    Spacer(modifier = Modifier.height(8.dp))

    KoinUserTextFieldAlert(
        text = stringResource(R.string.find_password_phone_number_code_request_exceeded),
        state = KoinUserTextFieldAlertState.Error
    )
}

@Composable
private fun VerificationCodeSentSuccessMessage(
    remainingCount: Int,
    totalCount: Int
) {
    Row {
        KoinUserTextFieldAlert(
            text = stringResource(R.string.find_password_verification_code_sent),
            state = KoinUserTextFieldAlertState.Success
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
        verificationMethod = "01012345678",
        verificationMethodState = VerificationMethodState.Available,
        verificationCode = "123456",
        verificationCodeState = VerificationCodeState.None,
        verificationTimeLeft = 180,
        isSms = true
    )
}
