package `in`.koreatech.koin.feature.findid.ui.verification

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.text.KeyboardOptions
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
import `in`.koreatech.koin.feature.findid.R
import `in`.koreatech.koin.feature.findid.component.KoinFindIdBasicTextField
import `in`.koreatech.koin.feature.findid.component.KoinFindIdTextFieldAlert
import `in`.koreatech.koin.feature.findid.component.KoinFindIdTextFieldAlertState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun FindIdVerification(
    viewModel: FindIdVerificationViewModel = hiltViewModel(),
    navigateToCompleteScreen: (loginId: String) -> Unit = { _ -> }
) {
    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect {
        handleSideEffect(
            sideEffect = it,
            onStartTimer = { viewModel.startTimer() },
            onStopTimer = { viewModel.stopTimer() },
            navigateToCompleteScreen = { loginId ->
                navigateToCompleteScreen(loginId)
            }
        )
    }

    FindIdVerificationImpl(
        isSms = uiState.isSms,
        verificationMethod = uiState.verificationMethod,
        verificationMethodState = uiState.verificationMethodState,
        verificationCode = uiState.verificationCode,
        verificationCodeState = uiState.verificationCodeState,
        verificationTimeLeft = uiState.verificationTimeLeft,
        onVerificationMethodChange = { viewModel.updateVerificationMethod(it) },
        onVerificationCodeChange = { viewModel.updateVerificationCode(it) },
        onVerificationCodeRequest = { viewModel.checkVerificationMethodExists() },
        onVerificationCodeVerify = { viewModel.checkVerificationCode() },
        navigateToCompleteScreen = { viewModel.getLoginId() },
        navigateToEmailScreen = { viewModel.updateIsSms(false) }
    )
}

@Composable
fun FindIdVerificationImpl(
    isSms: Boolean,
    verificationMethod: String,
    verificationMethodState: PhoneNumber,
    verificationCode: String,
    verificationCodeState: VerificationCode,
    verificationTimeLeft: Int,
    modifier: Modifier = Modifier,
    onVerificationMethodChange: (String) -> Unit = {},
    onVerificationCodeChange: (String) -> Unit = {},
    onVerificationCodeRequest: () -> Unit = {},
    onVerificationCodeVerify: () -> Unit = {},
    navigateToCompleteScreen: () -> Unit = { },
    navigateToEmailScreen: () -> Unit = { }
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .imePadding()
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = stringResource(if (isSms) R.string.find_id_phone_number else R.string.find_id_email),
            style = KoinTheme.typography.medium18
        )

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KoinFindIdBasicTextField(
                modifier = Modifier.weight(1f),
                value = verificationMethod,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                hint = stringResource(if (isSms) R.string.find_id_phone_number_hint else R.string.find_id_email_hint),
                onValueChange = onVerificationMethodChange,
                maxLength = if (isSms) 11 else Int.MAX_VALUE
            )

            Spacer(modifier = Modifier.width(16.dp))

            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                FilledButton(
                    modifier = Modifier.widthIn(min = 86.dp),
                    text = stringResource(R.string.find_id_send),
                    textStyle = KoinTheme.typography.regular10,
                    contentPadding = PaddingValues(vertical = 6.dp, horizontal = 12.dp),
                    onClick = onVerificationCodeRequest,
                    enabled = verificationMethod.isNotBlank() && verificationCodeState !is VerificationCode.Valid
                )
            }
        }

        when (verificationMethodState) {
            PhoneNumber.CountExceeded -> VerificationMethodRequestCountExceeded()
            is PhoneNumber.Sent -> VerificationCodeSentSuccessMessage(
                verificationMethodState.remainingCount,
                verificationMethodState.totalCount
            )

            PhoneNumber.WrongFormat -> VerificationMethodInvalidMessage(isSms)
            is PhoneNumber.Failed,
            PhoneNumber.None,
            PhoneNumber.AlreadySignedUp,
            PhoneNumber.Available -> {
            }
        }

        if (verificationMethod.isBlank() && isSms) {
            EmailMessage(
                navigateToEmailScreen = navigateToEmailScreen
            )
        }

        Spacer(modifier = Modifier.height(64.dp))

        if (verificationMethodState is PhoneNumber.Sent) {
            Text(
                text = stringResource(R.string.find_id_verification_code),
                style = KoinTheme.typography.medium18
            )

            Spacer(modifier = Modifier.height(12.dp))

            SignUpVerificationCodeVerificationStep(
                verificationCode = verificationCode,
                verificationCodeState = verificationCodeState,
                verificationTimeLeft = verificationTimeLeft,
                onVerificationCodeChange = onVerificationCodeChange,
                checkVerificationCode = onVerificationCodeVerify
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        FilledButton(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.find_id_next),
            contentPadding = PaddingValues(12.dp),
            onClick = navigateToCompleteScreen,
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
            KoinFindIdBasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = verificationCode,
                maxLength = 6,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                onValueChange = { onVerificationCodeChange(it) },
                hint = stringResource(R.string.find_id_verification_code_field_hint),
                enabled = verificationCodeState !is VerificationCode.Valid
            )

            if (verificationCodeState !is VerificationCode.None && verificationCodeState !is VerificationCode.Valid) {
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
                text = stringResource(R.string.find_id_verification_code_check),
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
        KoinFindIdTextFieldAlert(
            text = when (verificationCodeState) {
                VerificationCode.Valid -> stringResource(R.string.find_id_verification_code_correct)
                VerificationCode.NotValid -> stringResource(R.string.find_id_verification_code_incorrect)
                VerificationCode.Expired -> stringResource(R.string.find_id_verification_code_timeout)
                else -> "" // Not used
            },
            state = when (verificationCodeState) {
                VerificationCode.Valid -> KoinFindIdTextFieldAlertState.Success
                VerificationCode.NotValid -> KoinFindIdTextFieldAlertState.Warning
                VerificationCode.Expired -> KoinFindIdTextFieldAlertState.Warning
                else -> KoinFindIdTextFieldAlertState.Warning // Not used
            }
        )
    }
}

@Composable
private fun EmailMessage(
    navigateToEmailScreen: () -> Unit = {}
) {
    Spacer(modifier = Modifier.height(12.dp))

    Row {
        Text(
            text = stringResource(R.string.find_id_phone_number_not_registered),
            style = KoinTheme.typography.regular12,
            color = KoinTheme.colors.neutral500
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier.noRippleClickable {
                navigateToEmailScreen()
            },
            text = stringResource(R.string.find_id_with_email),
            color = KoinTheme.colors.primary500,
            style = KoinTheme.typography.regular12
        )
    }
}

@Composable
private fun VerificationMethodInvalidMessage(
    isSms: Boolean = true
) {
    Spacer(modifier = Modifier.height(8.dp))

    KoinFindIdTextFieldAlert(
        text = stringResource(if (isSms) R.string.find_id_phone_number_invalid else R.string.find_id_email_invalid),
        state = KoinFindIdTextFieldAlertState.Warning
    )
}

@Composable
private fun VerificationMethodRequestCountExceeded() {
    Spacer(modifier = Modifier.height(8.dp))

    KoinFindIdTextFieldAlert(
        text = stringResource(R.string.find_id_verification_code_request_exceeded),
        state = KoinFindIdTextFieldAlertState.Error
    )
}

@Composable
private fun VerificationCodeSentSuccessMessage(
    remainingCount: Int,
    totalCount: Int
) {
    Row {
        KoinFindIdTextFieldAlert(
            text = stringResource(R.string.find_id_verification_code_sent),
            state = KoinFindIdTextFieldAlertState.Success
        )

        Text(" ") // Space

        Text(
            text = stringResource(
                R.string.find_id_verification_code_remaining_count,
                remainingCount,
                totalCount
            ),
            color = KoinTheme.colors.neutral500,
            style = KoinTheme.typography.regular12
        )
    }
}

fun handleSideEffect(
    sideEffect: FindIdVerificationSideEffect,
    onStartTimer: () -> Unit = {},
    onStopTimer: () -> Unit = { },
    navigateToCompleteScreen: (String) -> Unit = { }
) {
    when (sideEffect) {
        FindIdVerificationSideEffect.StartTimer -> onStartTimer()
        FindIdVerificationSideEffect.StopTimer -> onStopTimer()
        is FindIdVerificationSideEffect.NavigateToCompleteScreen -> navigateToCompleteScreen(sideEffect.loginId)
    }
}

@Preview(showBackground = true)
@Composable
fun FindPasswordByVerificationMethodPreview() {
    FindIdVerificationImpl(
        isSms = true,
        verificationMethod = "test@test.com",
        verificationMethodState = PhoneNumber.None,
        verificationCode = "123456",
        verificationCodeState = VerificationCode.None,
        verificationTimeLeft = 300
    )
}
