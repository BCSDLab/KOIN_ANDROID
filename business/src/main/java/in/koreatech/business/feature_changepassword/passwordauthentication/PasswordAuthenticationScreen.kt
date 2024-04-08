package `in`.koreatech.business.feature_changepassword.passwordauthentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.Blue1
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.Gray5
import `in`.koreatech.business.util.showMessage
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun PasswordAuthenticationScreen(
    modifier: Modifier = Modifier,
    navigateToChangePassword: (email: String) -> Unit = {},
    onBackPressed: () -> Unit,
    viewModel: PasswordAuthenticationViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value
    
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = modifier
                .padding(top = 56.dp, start = 10.dp, bottom = 18.dp)
                .width(40.dp)
                .height(40.dp)
                .clickable { onBackPressed() }

        ) {
            Image(
                painter = painterResource(R.drawable.back_ic),
                contentDescription = stringResource(id = R.string.back_arrow),
                modifier = modifier
                    .width(24.dp)
                    .height(24.dp)
            )
        }
        Text(
            text = stringResource(R.string.find_password),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(top = 32.dp, start = 32.dp, bottom = 32.dp)
        )

        BasicTextField(
            value = state.email,
            onValueChange = {
                viewModel.insertEmail(it)
            },
            maxLines = 1,
            textStyle = TextStyle(fontSize = 15.sp),
            decorationBox = { innerTextField ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = modifier.padding(bottom = 7.dp)
                    ){
                        if (state.email.isEmpty()) {
                            Text(
                                text = stringResource(R.string.input_email),
                                color = Blue1,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                    HorizontalDivider(
                        modifier = modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = if (state.email.isEmpty()) Blue1 else Color.Black
                    )
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(bottom = 20.dp)
                .height(30.dp)
        )

        Row(modifier = modifier
            .padding(bottom = 252.dp)
            .fillMaxWidth()
            .height(30.dp)
        ) {
            BasicTextField(
                value = state.authenticationCode,
                onValueChange = {
                    viewModel.insertAuthCode(it)
                },
                maxLines = 1,
                textStyle = TextStyle(fontSize = 15.sp),
                decorationBox = { innerTextField ->
                    Column() {
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = modifier.padding(bottom = 7.dp)
                        ){
                            if (state.authenticationCode.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.input_authentication_code),
                                    color = Blue1,
                                    fontSize = 15.sp
                                )
                            }
                            innerTextField()
                        }
                        HorizontalDivider(
                            modifier = modifier.width(220.dp),
                            thickness = 1.dp,
                            color = if (state.authenticationCode.isEmpty()) Blue1 else Color.Black
                        )
                    }

                },
                modifier = modifier
                    .padding(start = 32.dp, end = 16.dp)
                    .fillMaxHeight()
            )

            Button(
                onClick = { viewModel.sendAuthCode(state.email) },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(ColorPrimary),
                contentPadding = PaddingValues(1.dp),
                modifier = modifier
                    .fillMaxSize()
                    .padding(end = 32.dp)
            ) {
                Text(text = if(state.authenticationBtnIsClicked)stringResource(R.string.resend) else stringResource(R.string.send_authentication_code),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Button(
            onClick =  {
                viewModel.authenticateCode(state.email, state.authenticationCode)
            } ,
            shape = RectangleShape,
            colors = if(state.authenticationCode.isBlank()) ButtonDefaults.buttonColors(Gray5)
            else ButtonDefaults.buttonColors(ColorPrimary),
            modifier = modifier
                .padding(horizontal = 32.dp)
                .padding(bottom = 80.dp)
                .fillMaxWidth()
                .height(44.dp)
        ) {
            Text(text = stringResource(R.string.email_certification),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    viewModel.collectSideEffect{
        when(it){
            is PasswordAuthenticationSideEffect.GotoChangePasswordScreen -> navigateToChangePassword(state.email)
            is PasswordAuthenticationSideEffect.ToastIsNotEmail -> showMessage("이메일 주소가 올바르지 않습니다")
            is PasswordAuthenticationSideEffect.ToastNoEmail -> showMessage("이메일을 입력해주세요")
            is PasswordAuthenticationSideEffect.SendAuthCode -> showMessage("이메일로 발송된 인증번호 6자리를 입력해 주세요.")
            is PasswordAuthenticationSideEffect.ToastNotCoincideAuthCode -> showMessage("인증번호가 일치하지 않습니다.")
            else -> {}
        }
    }
    //여기 부분이 livedata 부분

}

@Preview
@Composable
fun PreviewPasswordAuthenticationScreen() {
    Surface {
        PasswordAuthenticationScreen(
            navigateToChangePassword = {},
            onBackPressed = {}
        )
    }
}