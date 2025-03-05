package `in`.koreatech.business.feature.signup.accountsetup

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.textfield.ContactHelperMessage
import `in`.koreatech.business.feature.textfield.LinedTextField
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorUnarchived
import `in`.koreatech.business.ui.theme.Gray1
import `in`.koreatech.business.ui.theme.Gray11
import `in`.koreatech.business.ui.theme.Gray2
import `in`.koreatech.business.ui.theme.KOIN_ANDROIDTheme
import `in`.koreatech.koin.domain.error.owner.OwnerError
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun AccountSetupScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountSetupViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    val state = viewModel.collectAsState().value

    if (state.phoneNumberState == SignupContinuationState.RequestedSmsValidation || state.phoneNumberState == SignupContinuationState.RequestedSmsValidation && state.sendCodeError == null) {
        viewModel.onChangeSmsValidation(true)
    }
    var timerText by remember { mutableStateOf(180) }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            IconButton(
                onClick = viewModel::onBackButtonClicked,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = stringResource(id = R.string.back_icon),
                )
            }

            Text(
                text = stringResource(id = R.string.sign_up),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier,
                    color = ColorPrimary,
                    fontWeight = FontWeight.Medium,
                    text = stringResource(id = R.string.input_basic_information)
                )
                Text(
                    text = stringResource(id = R.string.two_third),
                    color = ColorPrimary,
                    fontWeight = FontWeight.Medium,
                )
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                drawLine(
                    color = ColorUnarchived,
                    start = Offset(-40f, 0f),
                    end = Offset(size.width + 40, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = ColorPrimary,
                    start = Offset(-40f, 0f),
                    end = Offset((size.width + 35) / 3 * 2, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(id = R.string.enter_phone_number),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(40.dp))

            LinedTextField(
                value = state.phoneNumber,
                onValueChange = { viewModel.onPhoneNumChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.enter_phone),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                errorText = if (state.sendCodeError == OwnerError.ExistsPhoneNumberException) stringResource(
                    id = R.string.phone_number_is_duplicated
                ) else stringResource(id = R.string.phone_number_error),
                isError = (state.phoneNumber.isNotEmpty() && state.phoneNumber.length != 11) || state.sendCodeError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(4.dp),
                enabled = state.phoneNumber.length == 11 && state.phoneNumberState != SignupContinuationState.RequestedSmsValidation && state.sendCodeError == null,
                colors = ButtonDefaults.buttonColors(
                    disabledBackgroundColor = Gray11,
                ),
                onClick = {
                    viewModel.checkExistsAccount()
                    viewModel.changeDialogVisibility(true)
                    AccountTimer.start { remainingTime ->
                        timerText = remainingTime
                    }
                }
            ) {
                Text(
                    text = stringResource(id = R.string.send_authentication_code),
                    fontSize = 16.sp,
                    color = if (state.phoneNumber.length == 11 && state.phoneNumberState != SignupContinuationState.RequestedSmsValidation) Color.White else Gray1,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (state.hasRequestedSmsValidation) {
                SendAuthCodeDialog(
                    onDismissRequest = {
                        viewModel.changeDialogVisibility(false)
                    },
                    dialogText = stringResource(id = R.string.send_authentication_code_dialog),
                    visibility = state.dialogVisibility
                )
                Text(
                    text = stringResource(id = R.string.enter_authentication_code),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(40.dp))

                Box {
                    LinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.authCode,
                        onValueChange = {
                            viewModel.verifySmsCode(
                                state.phoneNumber,
                                it
                            )
                        },
                        label = stringResource(id = R.string.enter_verification_code),
                        timerText = timerText,
                        textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                        errorText = if (timerText == 0) stringResource(id = R.string.sms_code_time_over) else stringResource(
                            id = R.string.sms_code_not_validate
                        ),
                        successText = stringResource(id = R.string.auth_code_equal),
                        isError = state.verifyState is SignupContinuationState.Failed || timerText == 0,
                        isSuccess = state.verifyState == SignupContinuationState.CheckComplete,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                if (state.verifyState != SignupContinuationState.CheckComplete) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = ColorPrimary,
                            disabledBackgroundColor = Gray2,
                        ),
                        onClick = {
                            viewModel.checkExistsAccount()
                            viewModel.changeDialogVisibility(true)
                            AccountTimer.start { remainingTime ->
                                timerText = remainingTime
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.resend_authentication_code),
                            fontSize = 16.sp,
                            color = White
                        )
                    }
                    if (AccountTimer.secondsRemaining <= 290L) {
                        Spacer(modifier = Modifier.height(4.dp))
                        ContactHelperMessage(
                            errorText = stringResource(id = R.string.resend_authentication_code_error),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .heightIn(50.dp),
                shape = RectangleShape,
                enabled = state.verifyState == SignupContinuationState.CheckComplete,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorPrimary,
                    contentColor = White,
                    disabledBackgroundColor = Gray2,
                    disabledContentColor = Gray1,
                ),
                onClick = viewModel::onNavigateToNextScreen
            ) {
                Text(
                    text = stringResource(id = R.string.next),
                    fontSize = 16.sp,
                    color = if (state.verifyState != SignupContinuationState.CheckComplete) Gray1 else White,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    viewModel.collectSideEffect {
        when (it) {
            is AccountSetupSideEffect.NavigateToNextScreen -> onNextClicked()
            AccountSetupSideEffect.NavigateToBackScreen -> onBackClicked()
        }
    }
}


@Composable
fun SendAuthCodeDialog(
    onDismissRequest: () -> Unit,
    dialogText: String,
    visibility: Boolean,
) {
    if (!visibility) return
    AlertDialog(
        modifier = Modifier
            .width(270.dp)
            .height(160.dp),
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = dialogText,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.Black,
                style = TextStyle(lineHeight = 25.sp)
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        buttons = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    modifier = Modifier
                        .widthIn(72.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.textButtonColors(
                        backgroundColor = ColorPrimary,
                        contentColor = Color.White
                    ),
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.check),
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun AccountSetupScreenPreview() {
    KOIN_ANDROIDTheme {
        AccountSetupScreen(
            onNextClicked = {},
            onBackClicked = {}
        )
    }
}
