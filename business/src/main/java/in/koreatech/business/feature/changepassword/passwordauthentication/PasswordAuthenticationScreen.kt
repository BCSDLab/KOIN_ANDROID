package `in`.koreatech.business.feature.changepassword.passwordauthentication

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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.Blue1
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.Gray5
import `in`.koreatech.business.util.ext.clickableOnce
import `in`.koreatech.koin.core.toast.ToastUtil
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect


@Composable
fun PasswordAuthenticationScreenImpl(
    modifier: Modifier = Modifier,
    navigateToChangePassword: (email: String) -> Unit = {},
    onBackPressed: () -> Unit,
    viewModel: PasswordAuthenticationViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value

    PasswordAuthenticationScreen(
        modifier = modifier,
        email = state.email,
        authCode = state.authenticationCode,
        emailIsEmpty = state.emailIsEmpty(),
        authCodeIsEmpty = state.authCodeIsEmpty(),
        authenticationBtnIsClicked = state.authenticationBtnIsClicked,
        onBackPressed = onBackPressed,
        insertEmail = { email -> viewModel.insertEmail(email)},
        insertAuthCode = {authCode -> viewModel.insertAuthCode(authCode)},
        sendAuthCode = {viewModel.sendAuthCode(state.email.trim())},
        authenticateCode = { viewModel.authenticateCode(state.email.trim(), state.authenticationCode.trim()) }
    )

    HandleSideEffects(viewModel, state.email.trim(), navigateToChangePassword)
}
@Composable
fun PasswordAuthenticationScreen(
    modifier: Modifier,
    email: String,
    authCode: String,
    emailIsEmpty: Boolean,
    authCodeIsEmpty: Boolean,
    authenticationBtnIsClicked: Boolean,
    onBackPressed: () -> Unit,
    insertEmail: (String) -> Unit,
    insertAuthCode: (String) -> Unit,
    sendAuthCode: () -> Unit,
    authenticateCode: () -> Unit
) {
    
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
            text = stringResource(R.string.password_find),
            fontSize = 24.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(top = 32.dp, start = 32.dp, bottom = 32.dp)
        )

        BasicTextField(
            value = email,
            onValueChange = insertEmail,
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
                        if (emailIsEmpty) {
                            Text(
                                text = stringResource(R.string.email_input),
                                color = Blue1,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                    Divider(
                        modifier = modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = if (emailIsEmpty) Blue1 else Color.Black
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
                value = authCode,
                onValueChange = insertAuthCode,
                maxLines = 1,
                textStyle = TextStyle(fontSize = 15.sp),
                decorationBox = { innerTextField ->
                    Column() {
                        Box(
                            contentAlignment = Alignment.CenterStart,
                            modifier = modifier.padding(bottom = 7.dp)
                        ){
                            if (authCodeIsEmpty) {
                                Text(
                                    text = stringResource(R.string.auth_code_input),
                                    color = Blue1,
                                    fontSize = 15.sp
                                )
                            }
                            innerTextField()
                        }
                        Divider(
                            modifier = modifier.width(220.dp),
                            thickness = 1.dp,
                            color = if (authCodeIsEmpty) Blue1 else Color.Black
                        )
                    }

                },
                modifier = modifier
                    .padding(start = 32.dp, end = 16.dp)
                    .fillMaxHeight()
            )

            Button(
                onClick =  sendAuthCode,
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(ColorPrimary),
                contentPadding = PaddingValues(1.dp),
                modifier = modifier
                    .fillMaxSize()
                    .padding(end = 32.dp)
                    .clickableOnce { }
            ) {
                Text(text = if(authenticationBtnIsClicked)stringResource(R.string.auth_code_resend) else stringResource(R.string.auth_code_send),
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Button(
            onClick =  authenticateCode,
            shape = RectangleShape,
            colors = if(authCodeIsEmpty) ButtonDefaults.buttonColors(Gray5)
            else ButtonDefaults.buttonColors(ColorPrimary),
            modifier = modifier
                .padding(horizontal = 32.dp)
                .padding(bottom = 80.dp)
                .fillMaxWidth()
                .height(44.dp)
        ) {
            Text(text = stringResource(R.string.email_certification),
                fontSize = 15.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun HandleSideEffects(viewModel: PasswordAuthenticationViewModel, email: String, navigateToChangePassword: (email: String) -> Unit) {
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is PasswordAuthenticationSideEffect.GotoChangePasswordScreen -> navigateToChangePassword(email)
            is PasswordAuthenticationSideEffect.SendAuthCode -> ToastUtil.getInstance().makeShort(context.getString(R.string.auth_code_input_from_email))
            is PasswordAuthenticationSideEffect.ShowMessage -> {
                val message = when (sideEffect.type) {
                    ErrorType.NoEmail -> context.getString(R.string.email_address_insert)
                    ErrorType.IsNotEmail -> context.getString(R.string.email_address_incorrect)
                    ErrorType.NullAuthCode -> context.getString(R.string.auth_code_insert)
                    ErrorType.NotCoincideAuthCode -> context.getString(R.string.auth_code_not_equal)
                }
                ToastUtil.getInstance().makeShort(message)
            }
        }
    }
}

@Preview
@Composable
fun PreviewPasswordAuthenticationScreen() {
    Surface {
        PasswordAuthenticationScreen(
            modifier = Modifier,
            email = "",
            authCode = "",
            emailIsEmpty = true,
            authCodeIsEmpty = true,
            authenticationBtnIsClicked = true,
            onBackPressed = {  },
            insertEmail = { },
            insertAuthCode = {},
            sendAuthCode = {},
            authenticateCode = { }
        )
    }
}