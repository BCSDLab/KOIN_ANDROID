package `in`.koreatech.business.feature_changepassword.changepassword


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.textfield.LinedTextField
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
        onPasswordChange = { newPassword ->
            viewModel.insertPassword(newPassword)
        },
        onPasswordCheckedChange = { newCheckedPassword ->
            viewModel.insertPasswordChecked(newCheckedPassword)
        },
        onBackPressed = onBackPressed,
        onChangePasswordClick = {
            viewModel.changePassword(
                state.phoneNumber.trim(),
                state.password.trim(),
                state.passwordChecked.trim()
            )
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
    fillAllPasswords: Boolean,
    passwordIsEmpty: Boolean,
    passwordCheckedIsEmpty: Boolean,
    onPasswordChange: (String) -> Unit,
    onPasswordCheckedChange: (String) -> Unit,
    onBackPressed: () -> Unit,
    onChangePasswordClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                IconButton(
                    onClick = { onBackPressed() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = stringResource(id = R.string.back_icon),
                    )
                }

                Text(
                    text = stringResource(id = R.string.password_find),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier,
                        color = ColorPrimary,
                        fontWeight = FontWeight.Bold,
                        text = stringResource(R.string.change_password_step)
                    )
                    Text(
                        text = stringResource(id = R.string.two_half),
                        color = ColorPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    drawLine(
                        color = ColorPrimary,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = stringResource(R.string.new_password),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.padding(start = 8.dp, bottom = 8.dp),
                )


                LinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    isPassword = true,
                    helperText = stringResource(R.string.password_condition),
                    label = stringResource(R.string.input_new_password)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.confirm_password),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier.padding(start = 8.dp, bottom = 8.dp),
                )

                LinedTextField(
                    value = passwordChecked,
                    onValueChange = onPasswordCheckedChange,
                    isPassword = true,
                    isError = notCoincidePassword,
                    errorText = stringResource(R.string.password_not_coincide),
                    label = stringResource(R.string.input_new_password_again)
                )

                Button(
                    onClick = onChangePasswordClick,
                    colors = if (fillAllPasswords) ButtonDefaults.buttonColors(ColorPrimary)
                    else ButtonDefaults.buttonColors(Gray5),
                    modifier = modifier
                        .padding(bottom = 80.dp, top = 217.5.dp)
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.complete),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (fillAllPasswords) Color.White else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun HandleSideEffects(viewModel: ChangePasswordViewModel, navigateToFinish: () -> Unit) {
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is ChangePasswordSideEffect.GotoFinishScreen -> navigateToFinish()
            is ChangePasswordSideEffect.NotCoincidePassword -> viewModel.viewNotCoincidePassword()
            is ChangePasswordSideEffect.ToastNullEmail -> ToastUtil.getInstance()
                .makeShort(context.getString(R.string.email_address_insert))

            is ChangePasswordSideEffect.ToastIsNotPasswordForm -> ToastUtil.getInstance()
                .makeShort(context.getString(R.string.password_condition))

            is ChangePasswordSideEffect.ToastNullPassword -> ToastUtil.getInstance()
                .makeShort(context.getString(R.string.password_input))

            is ChangePasswordSideEffect.ToastNullPasswordChecked -> ToastUtil.getInstance()
                .makeShort(context.getString(R.string.password_confirm_input))
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
    ) {
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
        modifier = Modifier.background(Color.White)
    )
}