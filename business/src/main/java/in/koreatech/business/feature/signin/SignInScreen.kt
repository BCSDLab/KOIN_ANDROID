package `in`.koreatech.business.feature.signin

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
           viewModel.login(state.id.trim(), state.password.trim())
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
            modifier = Modifier
                .padding(start = 32.dp, top = 88.dp, end = 32.dp, bottom = 80.dp)
                .fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_color_horizontal_300x168),
                contentDescription = "Koin Logo",
                modifier = Modifier
                    .width(107.dp)
                    .height(60.dp)
            )
            Text(
                text = stringResource(id = R.string.for_business),
                fontSize = 20.sp,
                modifier = Modifier
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
            modifier = Modifier
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(bottom = 40.dp)
        )

        Button(
            onClick = onSignInClick,
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(ColorAccent),
            modifier = Modifier
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
            modifier = Modifier
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
            modifier = Modifier
                .padding(horizontal = 127.dp)
                .padding(top = 24.dp)
                .fillMaxWidth()

        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_password),
                    contentDescription = "password_icon",
                    modifier = Modifier
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
            modifier = Modifier
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
fun PreViewSignInScreen(
    modifier: Modifier = Modifier,
    id: String = "",
    password: String = "",
    onIdChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit  = {},
    onSignInClick: () -> Unit  = {},
    onSignUpClick: () -> Unit  = {},
    onFindPasswordClick: () -> Unit  = {}
){
    SignInScreen(
        modifier = modifier,
        id = id,
        password = password,
        onIdChange = onIdChange,
        onPasswordChange = onPasswordChange,
        onSignInClick = onSignInClick,
        onSignUpClick = onSignUpClick,
        onFindPasswordClick = onFindPasswordClick
    )
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Composable
fun PreViewDarkModeSignInScreen(
    modifier: Modifier = Modifier,
    id: String = "",
    password: String = "",
    onIdChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit  = {},
    onSignInClick: () -> Unit  = {},
    onSignUpClick: () -> Unit  = {},
    onFindPasswordClick: () -> Unit  = {}
){
    SignInScreen(
        modifier = modifier,
        id = id,
        password = password,
        onIdChange = onIdChange,
        onPasswordChange = onPasswordChange,
        onSignInClick = onSignInClick,
        onSignUpClick = onSignUpClick,
        onFindPasswordClick = onFindPasswordClick
    )
}