package `in`.koreatech.business.feature.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.R
import `in`.koreatech.business.ui.theme.Blue1
import `in`.koreatech.business.ui.theme.ColorAccent
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondaryText
import `in`.koreatech.koin.core.toast.ToastUtil
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect


@Composable
fun SignInScreenImpl(
    modifier: Modifier = Modifier,
    navigateToSignUp: () -> Unit,
    navigateToFindPassword: () -> Unit,
    navigateToMain: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value

    SignInScreen(
        modifier = modifier,
        id = state.id,
        password = state.password,
        onIdChange = {
            id -> viewModel.insertId(id)
        },
        onPasswordChange = {
            password -> viewModel.insertPassword(password)
        },
        onSignInClick = {
            if(state.isFilledIdEmailField) viewModel.login(state.id.trim(), state.password.trim())
            else viewModel.toastNullMessage()
                        },
        onSignUpClick = {viewModel.navigateToSignUp()},
        onFindPasswordClick = {viewModel.navigateToFindPassword()}
    )

    HandleSideEffects(viewModel, navigateToSignUp, navigateToFindPassword, navigateToMain)
}
@Composable
fun SignInScreen (
    modifier: Modifier,
    id: String,
    password: String,
    onIdChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onSignUpClick:() ->Unit,
    onFindPasswordClick:() -> Unit
    ){
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = modifier
                .padding(start = 32.dp, top = 88.dp, end = 32.dp, bottom = 80.dp)
                .fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_color_horizontal_300x168),
                contentDescription = "Koin Logo",
                modifier = modifier
                    .width(107.dp)
                    .height(60.dp)
            )
            Text(
                text = stringResource(id = R.string.for_business),
                fontSize = 20.sp,
                modifier = modifier
                    .padding(start = 8.dp, top = 30.dp)
            )
        }

        BasicTextField(
            value = id,
            onValueChange = onIdChange,
            maxLines = 1,
            textStyle = TextStyle(fontSize = 15.sp),
            decorationBox = { innerTextField ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        if (id.isEmpty()) {
                            Text(
                                stringResource(R.string.id),
                                color = Blue1,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                    Divider(
                        color = if (id.isEmpty()) Blue1 else Color.Black,
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(bottom = 28.dp)
        )

        BasicTextField(
            value = password,
            onValueChange = onPasswordChange,
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation(),
            textStyle = TextStyle(fontSize = 15.sp),
            decorationBox = { innerTextField ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        if (password.isEmpty()) {
                            Text(
                                stringResource(R.string.password),
                                color = Blue1,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                    Divider(
                        color = if (password.isEmpty()) Blue1 else Color.Black,
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(bottom = 40.dp)
        )

        Button(
            onClick = onSignInClick,
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(ColorAccent),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(bottom = 16.dp)
                .height(44.dp)

        ) {
            Text(
                stringResource(R.string.navigation_item_login),
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = onSignUpClick,
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(ColorPrimary),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(top = 16.dp, bottom = 24.dp)
                .height(44.dp)

        ) {
            Text(
                stringResource(R.string.sign_up),
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = onFindPasswordClick,
            colors = ButtonDefaults.buttonColors(Color.White),
            shape = RectangleShape,
            modifier = modifier
                .padding(horizontal = 127.dp)
                .padding(top = 24.dp)
                .fillMaxWidth()

        ) {
            Row(
                modifier = modifier.fillMaxWidth()
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_password),
                    contentDescription = "password_icon",
                    modifier = modifier
                        .height(20.dp)
                        .width(20.dp)
                )

                Text(
                    stringResource(R.string.password_find),
                    color = ColorSecondaryText,
                    fontSize = 13.sp,
                )
            }
        }

        Text(
            text = stringResource(R.string.copy_right),
            modifier = modifier
                .padding(top = 102.dp, bottom = 24.dp)
                .padding(horizontal = 64.dp)
        )

    }
}

@Composable
private fun HandleSideEffects(
    viewModel: SignInViewModel,
    navigateToSignUp: () -> Unit,
    navigateToFindPassword: () -> Unit,
    navigateToMain: () -> Unit)
{
    val context = LocalContext.current
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SignInSideEffect.NavigateToMain -> navigateToMain()
            is SignInSideEffect.NavigateToFindPassword -> navigateToFindPassword()
            is SignInSideEffect.NavigateToSignUp -> navigateToSignUp()
            is SignInSideEffect.ShowMessage -> ToastUtil.getInstance().makeShort(sideEffect.message)
            is SignInSideEffect.ShowNullMessage -> {
                val message = when (sideEffect.type) {
                    ErrorType.NullEmailOrPassword -> context.getString(R.string.login_required_field_not_filled)
                }
                ToastUtil.getInstance().makeShort(message)
            }
        }
    }
}

@Preview
@Composable
fun PreViewSignInScreen(){
    SignInScreen(
        modifier = Modifier,
        id = "",
        password = "",
        onIdChange = {},
        onPasswordChange = {},
        onSignInClick = {},
        onSignUpClick = {},
        onFindPasswordClick = {}
    )
}