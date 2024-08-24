package `in`.koreatech.business.feature_changepassword.passwordauthentication

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.textfield.LinedTextField
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorUnarchived
import `in`.koreatech.business.ui.theme.Gray1
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
        phoneNumber = state.phoneNumber,
        authCode = state.authenticationCode,
        emailIsEmpty = state.phoneNumberIsEmpty(),
        authCodeIsEmpty = state.authCodeIsEmpty(),
        authenticationBtnIsClicked = state.authenticationBtnIsClicked,
        onBackPressed = onBackPressed,
        insertPhoneNumber = { email -> viewModel.insertPhoneNumber(email) },
        insertAuthCode = { authCode -> viewModel.insertAuthCode(authCode) },
        sendAuthCode = { viewModel.sendAuthCode(state.phoneNumber.trim()) },
        authenticateCode = {
            viewModel.authenticateCode(
                state.phoneNumber.trim(),
                state.authenticationCode.trim()
            )
        }
    )

    HandleSideEffects(viewModel, state.phoneNumber.trim(), navigateToChangePassword)
}

@Composable
fun PasswordAuthenticationScreen(
    modifier: Modifier,
    phoneNumber: String,
    authCode: String,
    emailIsEmpty: Boolean,
    authCodeIsEmpty: Boolean,
    authenticationBtnIsClicked: Boolean,
    onBackPressed: () -> Unit,
    insertPhoneNumber: (String) -> Unit,
    insertAuthCode: (String) -> Unit,
    sendAuthCode: () -> Unit,
    authenticateCode: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 12.dp),
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
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
        Column(
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 24.dp),
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
                    text = stringResource(R.string.account_verification_step),
                    style = MaterialTheme.typography.h6,
                )
                Text(
                    text = stringResource(id = R.string.one_half),
                    color = ColorPrimary,
                    style = MaterialTheme.typography.h6,
                    letterSpacing = 4.sp,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                drawLine(
                    color = ColorUnarchived,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = ColorPrimary,
                    start = Offset(0f, 0f),
                    end = Offset(size.width / 2, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = stringResource(R.string.phone_number),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(start = 8.dp, bottom = 8.dp),
            )

            LinedTextField(
                value = phoneNumber,
                onValueChange = insertPhoneNumber,
                label = stringResource(R.string.enter_phone_number),
            )

            Text(
                text = stringResource(R.string.authentication_code),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(start = 8.dp, bottom = 8.dp, top = 4.dp),
            )

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                LinedTextField(
                    modifier = Modifier.width(200.dp),
                    value = authCode,
                    onValueChange = insertAuthCode,
                    label = stringResource(R.string.input_auth_code)
                )

                Button(
                    onClick = sendAuthCode,
                    colors = ButtonDefaults.buttonColors(ColorPrimary),
                    modifier = modifier
                        .width(124.dp)
                        .height(41.dp)
                        .clickableOnce { }
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.Center),
                        text = if (authenticationBtnIsClicked) stringResource(R.string.auth_code_resend) else stringResource(
                            R.string.auth_code_send
                        ),
                        letterSpacing = (-0.3).sp,
                        fontSize = 13.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = authenticateCode,
                colors = if (authCodeIsEmpty) ButtonDefaults.buttonColors(Gray5)
                else ButtonDefaults.buttonColors(ColorPrimary),
                modifier = modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                Text(
                    text = stringResource(R.string.next),
                    fontSize = 15.sp,
                    color = if (authCodeIsEmpty) Gray1 else Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun HandleSideEffects(
    viewModel: PasswordAuthenticationViewModel,
    email: String,
    navigateToChangePassword: (email: String) -> Unit
) {
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is PasswordAuthenticationSideEffect.GotoChangePasswordScreen -> navigateToChangePassword(
                email
            )

            is PasswordAuthenticationSideEffect.SendAuthCode -> ToastUtil.getInstance()
                .makeShort(context.getString(R.string.auth_code_input_from_email))

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
            phoneNumber = "",
            authCode = "",
            emailIsEmpty = true,
            authCodeIsEmpty = true,
            authenticationBtnIsClicked = true,
            onBackPressed = { },
            insertPhoneNumber = { },
            insertAuthCode = {},
            sendAuthCode = {},
            authenticateCode = { }
        )
    }
}