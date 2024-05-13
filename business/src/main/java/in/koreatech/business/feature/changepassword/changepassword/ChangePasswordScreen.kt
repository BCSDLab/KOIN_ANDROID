package `in`.koreatech.business.feature.changepassword.changepassword


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.Blue1
import `in`.koreatech.business.ui.theme.ColorError
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.Gray5
import `in`.koreatech.koin.core.toast.ToastUtil
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ChangePasswordScreenImpl(
    modifier: Modifier = Modifier,
    navigateToFinish: () -> Unit,
    onBackPressed: () -> Unit,
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value

    ChangePasswordScreen(
        password = state.password,
        passwordChecked = state.passwordChecked,
        notCoincidePassword = state.notCoincidePW,
        fillAllPasswords = state.fillAllPasswords,
        passwordIsEmpty = state.passwordIsBlank(),
        passwordCheckedIsEmpty = state.passwordCheckedIsBlank(),
        onPasswordChange = {
                newPassword -> viewModel.insertPassword(newPassword)
                           },
        onPasswordCheckedChange = {
                newCheckedPassword -> viewModel.insertPasswordChecked(newCheckedPassword)
                                  },
        onBackPressed = onBackPressed,
        onChangePasswordClick = {
            viewModel.changePassword(state.email.trim(), state.password.trim(), state.passwordChecked.trim())
        },
        modifier = modifier
    )

    HandleSideEffects(viewModel, navigateToFinish)
}
@Composable
fun ChangePasswordScreen(
    password: String,
    passwordChecked: String,
    notCoincidePassword: Boolean,
    fillAllPasswords :Boolean,
    passwordIsEmpty: Boolean,
    passwordCheckedIsEmpty: Boolean,
    onPasswordChange: (String) -> Unit,
    onPasswordCheckedChange: (String) -> Unit,
    onBackPressed: () -> Unit,
    onChangePasswordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = modifier
                .padding(top = 56.dp, start = 10.dp, bottom = 18.dp)
                .size(40.dp)
                .clickable { onBackPressed() }

        ) {
            Image(
                painter = painterResource(R.drawable.back_ic),
                contentDescription = stringResource(id = R.string.back_arrow),
                modifier = modifier
                    .size(24.dp)
            )
        }

        Text(
            text = stringResource(R.string.password_change),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(top = 32.dp, start = 32.dp, bottom = 32.dp)
        )

        BasicTextField(
            value =  password,
            onValueChange = onPasswordChange,
            maxLines = 1,
            textStyle = TextStyle(fontSize = 15.sp),
            visualTransformation = PasswordVisualTransformation(),
            decorationBox = { innerTextField ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = modifier.padding(bottom = 7.dp)
                    ){
                        if (passwordIsEmpty) {
                            Text(
                                text = stringResource(id = R.string.password_new),
                                color = Blue1,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = if (passwordIsEmpty) Blue1 else Color.Black
                    )
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(bottom = 8.dp)
                .height(30.dp)
        )

        Text(
            text = stringResource(R.string.password_condition),
            fontSize = 11.sp,
            color = Blue1,
            modifier = modifier
                .padding(start = 32.dp, bottom = 12.dp)
        )

        BasicTextField(
            value = passwordChecked,
            onValueChange = onPasswordCheckedChange,
            maxLines = 1,
            textStyle = TextStyle(fontSize = 15.sp),
            visualTransformation = PasswordVisualTransformation(),
            decorationBox = { innerTextField ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = modifier.padding(bottom = 7.dp)
                    ){
                        if (passwordCheckedIsEmpty) {
                            Text(
                                text = stringResource(id = R.string.password_confirm),
                                color = Blue1,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = if (passwordCheckedIsEmpty) Blue1 else Color.Black
                    )
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(bottom = 8.dp)
                .height(30.dp)
        )

        if(notCoincidePassword) {
            WaringNotCoincidePW(modifier)
        }

        Button(
            onClick = onChangePasswordClick,
            shape = RectangleShape,
            colors = if(fillAllPasswords) ButtonDefaults.buttonColors(ColorPrimary)
            else ButtonDefaults.buttonColors(Gray5),
            modifier = modifier
                .padding(horizontal = 32.dp)
                .padding(bottom = 80.dp, top = 217.5.dp)
                .fillMaxWidth()
                .height(44.dp)
        ) {
            Text(
                text = stringResource(id = R.string.password_changing),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun HandleSideEffects(viewModel: ChangePasswordViewModel, navigateToFinish: () -> Unit) {
    val context = LocalContext.current

    viewModel.collectSideEffect{
        when(it){
            is ChangePasswordSideEffect.GotoFinishScreen -> navigateToFinish()
            is ChangePasswordSideEffect.NotCoincidePassword -> viewModel.viewNotCoincidePassword()
            is ChangePasswordSideEffect.ToastNullEmail -> ToastUtil.getInstance().makeShort(context.getString(R.string.email_address_insert))
            is ChangePasswordSideEffect.ToastIsNotPasswordForm -> ToastUtil.getInstance().makeShort(context.getString(R.string.password_condition))
            is ChangePasswordSideEffect.ToastNullPassword -> ToastUtil.getInstance().makeShort(context.getString(R.string.password_input))
            is ChangePasswordSideEffect.ToastNullPasswordChecked -> ToastUtil.getInstance().makeShort(context.getString(R.string.password_confirm_input))
        }
    }
}

@Composable
fun WaringNotCoincidePW(
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 32.dp)
    ){
        Image(
            painter = painterResource(id = R.drawable.exclamation),
            contentDescription = "examationMark",
            modifier = modifier
                .padding(5.dp)
                .size(16.dp)
        )

        Text(
            text = stringResource(R.string.password_not_coincide),
            fontSize = 16.sp,
            color = ColorError
        )
    }
}

@Preview
@Composable
fun PreviewChangePassswordScreen() {
    ChangePasswordScreen(
        password = "",
        passwordChecked = "",
        notCoincidePassword = true,
        fillAllPasswords = true,
        passwordIsEmpty = true,
        passwordCheckedIsEmpty = true,
        onPasswordChange = {},
        onPasswordCheckedChange = {},
        onBackPressed = {},
        onChangePasswordClick = {},
        modifier = Modifier
    )
}