package `in`.koreatech.business.feature.signin

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextAlign
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
fun SignInScreen(
    modifier: Modifier = Modifier,
    navigateToSignUp: () -> Unit,
    navigateToFindPassword: () -> Unit,
    navigateToMain: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value

    SignInScreenImpl(
        modifier = modifier,
        id = state.id,
        password = state.password,
        errorMessage = state.errorMessage,
        notValidateField = state.notValidateField,
        onIdChange = viewModel::insertId,
        onPasswordChange = viewModel::insertPassword,
        onSignInClick = {
           viewModel.login()
                        },
        onSignUpClick = {viewModel.navigateToSignUp()},
        onFindPasswordClick = {viewModel.navigateToFindPassword()}
    )

    HandleSideEffects(viewModel, navigateToSignUp, navigateToFindPassword, navigateToMain)
}
@Composable
fun SignInScreenImpl(
    modifier: Modifier = Modifier,
    id: String = "",
    password: String = "",
    errorMessage: String = "",
    notValidateField: Boolean = false,
    onIdChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit  = {},
    onSignInClick: () -> Unit  = {},
    onSignUpClick: () -> Unit  = {},
    onFindPasswordClick: () -> Unit  = {}
    ){
    Column(modifier = modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 95.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = R.drawable.koin_owner_logo),
                contentDescription = "Koin Owner Logo",
                modifier = Modifier
                    .size(150.dp),
                alignment = Alignment.CenterStart
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
                        .fillMaxWidth()
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        if (id.isEmpty()) {
                            Text(
                                stringResource(R.string.phone_number),
                                color = Blue1,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                    Divider(
                        color = if (notValidateField) ColorAccent else Blue1,
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
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
                        color = if (notValidateField) ColorAccent else Blue1,
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(top = 4.dp)
            ,
            fontSize = 11.sp,
            text = if(notValidateField) errorMessage else "",
            color = ColorAccent
        )

        Button(
            onClick = onSignInClick,
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(ColorAccent),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(top = 40.dp)
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
                .padding(bottom = 24.dp)
                .height(44.dp)

        ) {
            Text(
                stringResource(R.string.sign_up),
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
                .padding(horizontal = 127.dp)
                .clickable {
                    onFindPasswordClick()
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = R.drawable.ic_password),
                contentDescription = "password_icon",
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp)
                    .padding(end = 4.dp)
            )

            Text(
                stringResource(R.string.password_find),
                color = ColorSecondaryText,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }
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
            is SignInSideEffect.ShowMessage -> {
                viewModel.setErrorMessage(sideEffect.message)
            }
            is SignInSideEffect.ShowNullMessage -> {
                val message = when (sideEffect.type) {
                    ErrorType.NullPhoneNumber -> context.getString(R.string.phone_number_is_null)
                    ErrorType.NullPassword -> context.getString(R.string.password_is_null)
                }
                viewModel.setErrorMessage(message)
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
    SignInScreenImpl(
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