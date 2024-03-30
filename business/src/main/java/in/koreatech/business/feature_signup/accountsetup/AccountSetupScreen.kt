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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.feature_signup.textfield.LinedTextField
import `in`.koreatech.business.ui.theme.ColorActiveButton
import `in`.koreatech.business.ui.theme.ColorDisabledButton
import `in`.koreatech.business.ui.theme.ColorHelper
import `in`.koreatech.business.ui.theme.Gray1
import `in`.koreatech.business.ui.theme.KOIN_ANDROIDTheme
import `in`.koreatech.business.ui.theme.Orange


@Composable
fun AccountSetupScreen(modifier: Modifier = Modifier) {
    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var emailConfirm by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
    ) {
        IconButton(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = { }
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
                    color = Orange,
                    text = stringResource(id = R.string.input_basic_information),
                )
                Text(text = stringResource(id = R.string.one_third), color = Orange,)
            }
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                drawLine(
                    color = Gray1,
                    start = Offset(0f - 40, 0f),
                    end = Offset(size.width + 35, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Orange,
                    start = Offset(0f - 40, 0f),
                    end = Offset((size.width + 40) / 3, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            LinedTextField(
                value = id,
                onValueChange = { id = it },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.id),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                lineColor = ColorHelper,
            )

            LinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.password),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                lineColor = ColorHelper,
                isPassword = true,
                helperText = stringResource(id = R.string.password_requirements),
            )

            LinedTextField(
                value = passwordConfirm,
                onValueChange = { passwordConfirm = it },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.password_confirm),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                lineColor = ColorHelper,
                isPassword = true,
                errorText = stringResource(id = R.string.password_mismatch),
            )

            LinedTextField(
                value = emailConfirm,
                onValueChange = { emailConfirm = it },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.email_confirm),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                lineColor = ColorHelper,
            )

            Spacer(modifier = Modifier.height(78.dp))

            Button(modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorActiveButton,
                    contentColor = Color.White,
                    disabledContainerColor = ColorDisabledButton,
                    disabledContentColor = Color.White,
                ),
                onClick = { }) {
                Text(
                    text = stringResource(id = R.string.email_authentication),
                    fontSize = 15.sp,
                    color = Color.White,

                    )
            }
        }
    }

}
