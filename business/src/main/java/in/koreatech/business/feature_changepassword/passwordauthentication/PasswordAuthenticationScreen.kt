package `in`.koreatech.business.feature_changepassword.passwordauthentication

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.Blue1
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.Gray5
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import org.orbitmvi.orbit.viewmodel.observe

@Composable
fun PasswordAuthenticationScreen(
    modifier: Modifier = Modifier,
    onAuthenticationButtonClicked: () -> Unit,
    onBackPressed: () -> Unit,
    viewModel: PasswordAuthenticationViewModel = viewModel()
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

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
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "backArrow",
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
                onClick = { /*viewModel.sendAuthCode(state.email)*/ },
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
                viewModel.goToPasswordChangeScreen()
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
            is PasswordAuthenticationSideEffect.AuthenticationBtnIsClicked -> viewModel.authenticationBtnClicked()
            is PasswordAuthenticationSideEffect.GotoChangePasswordScreen -> viewModel.goToPasswordChangeScreen()
            else -> {
                Toast.makeText(context, "안녕", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

@Preview
@Composable
fun previewPasswordAuthenticationScreen() {
    Surface {
        PasswordAuthenticationScreen(
            onAuthenticationButtonClicked = {},
            onBackPressed = {}
        )
    }
}