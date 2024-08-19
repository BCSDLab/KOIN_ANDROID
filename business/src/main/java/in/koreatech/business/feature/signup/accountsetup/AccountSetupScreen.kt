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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.textfield.LinedTextField
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.ColorUnarchived
import `in`.koreatech.business.ui.theme.Gray1
import `in`.koreatech.business.ui.theme.Gray2
import `in`.koreatech.business.ui.theme.KOIN_ANDROIDTheme
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
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                IconButton(
                    onClick = { viewModel.onBackButtonClicked() },
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
                    fontWeight = Bold,
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
                        fontWeight = Bold,
                        text = stringResource(id = R.string.input_basic_information)
                    )
                    Text(
                        text = stringResource(id = R.string.two_third),
                        color = ColorPrimary,
                        fontWeight = Bold,
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
        }
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(id = R.string.phone_number),
                fontSize = 14.sp,
                fontWeight = Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                LinedTextField(
                    value = state.phoneNumber,
                    onValueChange = { viewModel.onPhoneNumChanged(it) },
                    modifier = Modifier.width(203.dp),
                    label = stringResource(id = R.string.enter_phone_number),
                    textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                    errorText = if (state.phoneNumberState is SignupContinuationState.Failed) state.phoneNumberState.message else stringResource(
                        R.string.error_network_unknown
                    ),
                    successText = stringResource(R.string.success_send_sms_code),
                    isError = state.phoneNumberState is SignupContinuationState.Failed,
                    isSuccess = state.phoneNumberState == SignupContinuationState.RequestedSmsValidation,
                )

                Button(modifier = Modifier
                    .width(115.dp)
                    .height(41.dp),
                    shape = RoundedCornerShape(4.dp),
                    enabled = state.phoneNumber.isNotEmpty() && state.phoneNumberState !is SignupContinuationState.Failed,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = ColorPrimary,
                        contentColor = Color.White,
                        disabledBackgroundColor = Gray2,
                        disabledContentColor = Gray1,
                    ),
                    onClick = {
                        viewModel.checkExistsAccount(state.phoneNumber)

                    }) {
                    Text(
                        text = stringResource(id = R.string.send_authentication_code),
                        fontWeight = Bold,
                        fontSize = 13.sp,
                    )
                }

            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(id = R.string.authentication_code),
                fontSize = 14.sp,
                fontWeight = Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LinedTextField(
                    modifier = Modifier
                        .width(203.dp),
                    value = state.authCode,
                    onValueChange = { viewModel.onAuthCodeChanged(it) },
                    label = stringResource(id = R.string.enter_verification_code),
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                    errorText = stringResource(id = R.string.sms_code_not_validate),
                    successText = stringResource(id = R.string.auth_code_equal),
                    isError = state.verifyState is SignupContinuationState.Failed,
                    isSuccess = state.verifyState == SignupContinuationState.CheckComplete,
                )

                Button(
                    modifier = Modifier
                        .width(115.dp)
                        .height(41.dp),
                    shape = RoundedCornerShape(4.dp),
                    enabled = state.authCode.isNotEmpty() && state.verifyState != SignupContinuationState.CheckComplete,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if ( state.verifyState is SignupContinuationState.Failed) ColorSecondary else ColorPrimary,
                        contentColor = Color.White,
                        disabledBackgroundColor = Gray2,
                        disabledContentColor = Gray1,
                    ),
                    onClick = {
                        viewModel.verifySmsCode(
                            state.phoneNumber,
                            state.authCode
                        )
                    }
                ) {
                    Text(
                        text = stringResource(R.string.verify_sms),
                        fontWeight = Bold,
                        fontSize = 13.sp,
                    )
                }
            }


            Text(text = stringResource(id = R.string.password), fontSize = 14.sp, fontWeight = Bold)
            LinedTextField(
                value = state.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.enter_password),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                errorText = stringResource(id = R.string.password_not_validate),
                isPassword = true,
                isError = state.isPasswordError,
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(id = R.string.password_confirm),
                fontSize = 14.sp,
                fontWeight = Bold
            )
            LinedTextField(
                value = state.passwordConfirm,
                onValueChange = { viewModel.onPasswordConfirmChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.enter_password_confirm),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                isPassword = true,
                errorText = stringResource(id = R.string.password_mismatch),
                isError = state.isPasswordConfirmError,
            )




            Spacer(modifier = Modifier.height(55.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RectangleShape,
                enabled = state.isButtonEnabled && state.verifyState == SignupContinuationState.CheckComplete,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorPrimary,
                    contentColor = Color.White,
                    disabledBackgroundColor = Gray2,
                    disabledContentColor = Gray1,
                ),
                onClick = viewModel::onNavigateToNextScreen
            ) {
                Text(
                    text = stringResource(id = R.string.next),
                    fontSize = 13.sp,
                    fontWeight = Bold,
                )
            }
        }
    }

    viewModel.collectSideEffect {
        when (it) {
            is AccountSetupSideEffect.NavigateToNextScreen -> onNextClicked()
            AccountSetupSideEffect.NavigateToBackScreen -> onBackClicked()
        }
    }
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
