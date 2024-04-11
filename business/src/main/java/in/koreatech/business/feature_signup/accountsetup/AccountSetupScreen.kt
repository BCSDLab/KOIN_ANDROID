package `in`.koreatech.business.feature_signup.accountsetup

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import `in`.koreatech.business.feature_signup.textfield.LinedTextField
import `in`.koreatech.business.ui.theme.ColorActiveButton
import `in`.koreatech.business.ui.theme.ColorDisabledButton
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.ColorHelper
import `in`.koreatech.business.ui.theme.ColorUnarchived
import `in`.koreatech.business.ui.theme.KOIN_ANDROIDTheme
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun AccountSetupScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountSetupViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {},
    onNextClicked: (String) -> Unit = {},
) {
    val state = viewModel.collectAsState().value

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        IconButton(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = { onBackClicked() }
        ) {
            Icon(
                modifier = Modifier.padding(start = 10.dp),
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(id = R.string.back_icon),
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = R.string.master_sign_up),
                fontSize = 24.sp,
                fontWeight = Bold,
            )
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier,
                    color = ColorSecondary,
                    text = stringResource(id = R.string.input_basic_information),
                )
                Text(text = stringResource(id = R.string.one_third), color = ColorSecondary)
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                drawLine(
                    color = ColorUnarchived,
                    start = Offset(0f - 40, 0f),
                    end = Offset(size.width + 35, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                drawLine(
                    color = ColorUnarchived,
                    start = Offset(0f - 40, 0f),
                    end = Offset(size.width + 35, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = ColorSecondary,
                    start = Offset(0f - 40, 0f),
                    end = Offset((size.width + 40) / 3, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            LinedTextField(
                value = state.id,
                onValueChange = { viewModel.onIdChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.id),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                lineColor = ColorHelper,
            )

            LinedTextField(
                value = state.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.password),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                lineColor = ColorHelper,
                isPassword = true,
                helperText = stringResource(id = R.string.password_requirements),
            )

            LinedTextField(
                value = state.passwordConfirm,
                onValueChange = { viewModel.onPasswordConfirmChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.password_confirm),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                lineColor = ColorHelper,
                isPassword = true,
                errorText = stringResource(id = R.string.password_mismatch),
                isError = state.signupContinuationState == SignupContinuationState.PasswordNotMatching,
            )

            LinedTextField(
                value = state.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.email_confirm),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                lineColor = ColorHelper,
                errorText = stringResource(id = R.string.email_not_validate),
                isError = state.signupContinuationState == SignupContinuationState.EmailIsNotValidate,
            )

            Spacer(modifier = Modifier.height(78.dp))

            Button(modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
                shape = RectangleShape,
                enabled = state.id.isNotEmpty() && state.password.isNotEmpty() && state.passwordConfirm.isNotEmpty() && state.email.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorActiveButton,
                    contentColor = Color.White,
                    disabledBackgroundColor = ColorDisabledButton,
                    disabledContentColor = Color.White,
                ),
                onClick = {
                    if (state.signupContinuationState == SignupContinuationState.CheckComplete)
                        viewModel.onNextButtonClicked()
                    viewModel.postEmailVerification(
                        state.email,
                        state.password,
                        state.passwordConfirm
                    )
                }) {
                Text(
                    text = stringResource(id = R.string.email_authentication),
                    fontSize = 15.sp,
                    color = Color.White,
                )
            }
        }
    }

    viewModel.collectSideEffect {
        when (it) {
            is AccountAuthSideEffect.NavigateToNextScreen -> onNextClicked(it.email)
            AccountAuthSideEffect.NavigateToBackScreen -> onBackClicked()
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
