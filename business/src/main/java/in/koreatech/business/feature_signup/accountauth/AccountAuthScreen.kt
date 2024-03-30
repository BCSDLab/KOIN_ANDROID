package `in`.koreatech.business.feature_signup.accountauth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.feature_signup.accountsetup.AccountSetupScreen
import `in`.koreatech.business.feature_signup.textfield.AuthTextField
import `in`.koreatech.business.ui.theme.ColorActiveButton
import `in`.koreatech.business.ui.theme.ColorDescription
import `in`.koreatech.business.ui.theme.ColorDisabledButton
import `in`.koreatech.business.ui.theme.ColorEmphasis
import `in`.koreatech.business.ui.theme.ColorUnachieved
import kotlinx.coroutines.delay

@Composable
fun AccountAuthScreen(
    modifier: Modifier = Modifier,
    email: String,
    onBackClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {},
) {
    var authCode by remember { mutableStateOf("") }
    Column(
        modifier = modifier,
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
                    color = ColorEmphasis,
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
                    color = ColorUnachieved,
                    start = Offset(0f - 40, 0f),
                    end = Offset(size.width + 35, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = ColorEmphasis,
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
                    color = Color(0xFFF7941E),
                )
                Text(
                    text = stringResource(id = R.string.by),
                    fontSize = 15.sp,
                    color = Color(0xFFA1A1A1),
                )
            }
            Text(
                text = stringResource(id = R.string.verification_code_prompt),
                fontSize = 15.sp,
                color = ColorDescription,
            )

            Spacer(modifier = Modifier.height(38.dp))

            AuthTextField(
                value = authCode,
                onValueChange = { authCode = it },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.enter_verification_code),
                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                isPassword = true,
                isError = false,
            )

            Spacer(modifier = Modifier.height(8.dp))

            CountdownTimer()

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
                        containerColor = ColorActiveButton,
                        disabledContainerColor = ColorDisabledButton,
                        contentColor = Color.White,
                        disabledContentColor = Color.White,
                    ),
                    onClick = { }
                ) {
                    Text(text = stringResource(id = R.string.resend))
                }
                Spacer(modifier = Modifier.width(14.dp))

                Button(modifier = Modifier
                    .width(141.dp)
                    .height(44.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorActiveButton,
                        contentColor = Color.White,
                        disabledContainerColor = ColorDisabledButton,
                        disabledContentColor = Color.White,
                    ),
                    onClick = { onNextClicked() }) {
                    Text(text = stringResource(id = R.string.next))
                }
            }
        }
    }

}


@Composable
fun CountdownTimer() {

    var timeLeft by remember { mutableStateOf(300) }
    var minutes by remember { mutableStateOf(0) }
    var seconds by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = timeLeft) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
            minutes = timeLeft / 60
            seconds = timeLeft % 60
        }
    }

    val formattedTimeLeft = "%02d".format(minutes)
    val formattedSeconds = "%02d".format(seconds)

    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = stringResource(id = R.string.time_limit) + "$formattedTimeLeft : $formattedSeconds"
    )
}
