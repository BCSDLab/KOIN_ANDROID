package `in`.koreatech.business.feature.signup.accountauth


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.signup.accountsetup.AccountSetupViewModel
import `in`.koreatech.business.feature.textfield.AuthTextField
import `in`.koreatech.business.ui.theme.ColorDescription
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.ColorUnarchived
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun EmailAuthScreen(
    modifier: Modifier = Modifier,
    email: String,
    verifyViewModel: EmailAuthViewModel = hiltViewModel(),
    sendEmailViewModel: AccountSetupViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {},
) {
    val state = verifyViewModel.collectAsState().value

    Column(
        modifier = modifier,
    ) {
        IconButton(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = { verifyViewModel.onBackButtonClicked() }
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
                    text = stringResource(id = R.string.account_authentication)
                )
                Text(text = stringResource(id = R.string.two_third))
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
                    end = Offset((size.width + 40) * 2 / 3, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row {
                Text(
                    text = email,
                    fontSize = 15.sp,
                    color = ColorSecondary,
                )
                Text(
                    text = stringResource(id = R.string.by),
                    fontSize = 15.sp,
                    color = ColorDescription,
                )
            }
            Text(
                text = stringResource(id = R.string.verification_code_prompt),
                fontSize = 15.sp,
                color = ColorDescription,
            )
            Spacer(modifier = Modifier.height(38.dp))
            AuthTextField(
                value = state.authCode,
                onValueChange = { verifyViewModel.onAuthCodeChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.enter_verification_code),
                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                isPassword = true,
                isError = state.signUpContinuationError != null,

                )
            Spacer(modifier = Modifier.height(8.dp))

            LaunchedEffect(key1 = state.timeLeft) {
                while (state.timeLeft > 0) {
                    delay(1000L)
                    verifyViewModel.onTimerTick()
                }
            }


            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.time_limit) + state.formattedTimeLeft
            )

            Spacer(modifier = Modifier.height(183.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(modifier = Modifier
                    .width(141.dp)
                    .height(44.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        disabledContentColor = Color.White,
                    ),
                    onClick = {
                        sendEmailViewModel.postEmailVerification(email, "", "")
                        verifyViewModel.onResendButtonClicked()
                    }
                ) {
                    Text(text = stringResource(id = R.string.resend))
                }
                Spacer(modifier = Modifier.width(14.dp))

                Button(modifier = Modifier
                    .width(141.dp)
                    .height(44.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        disabledContentColor = Color.White,
                    ),
                    onClick = {
                        verifyViewModel.verifyEmail(email, state.authCode)
                    }) {
                    Text(text = stringResource(id = R.string.next))
                }
            }
        }

        verifyViewModel.collectSideEffect {
            when (it) {
                is EmailAuthSideEffect.NavigateToNextScreen -> onNextClicked()
                EmailAuthSideEffect.NavigateToBackScreen -> onBackClicked()
            }
        }
    }
}
