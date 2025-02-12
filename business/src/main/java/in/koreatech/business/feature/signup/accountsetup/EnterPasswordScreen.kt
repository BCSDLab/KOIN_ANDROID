package `in`.koreatech.business.feature.signup.accountsetup

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.textfield.LinedTextField
import `in`.koreatech.business.feature.textfield.PasswordTextField
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.ColorUnarchived
import `in`.koreatech.business.ui.theme.Gray1
import `in`.koreatech.business.ui.theme.Gray11
import `in`.koreatech.business.ui.theme.Gray2
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.util.ext.isNotValidPassword
import `in`.koreatech.koin.domain.util.ext.isValidPassword
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun EnterPasswordScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountSetupViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    val state = viewModel.collectAsState().value

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
                    onClick = viewModel::onBackButtonClicked,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = stringResource(id = R.string.back_icon),
                    )
                }

                Text(
                    text = stringResource(id = R.string.sign_up),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
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
                        fontWeight = FontWeight.Medium,
                        text = stringResource(id = R.string.input_basic_information)
                    )
                    Text(
                        text = stringResource(id = R.string.two_third),
                        color = ColorPrimary,
                        fontWeight = FontWeight.Medium,
                    )
                }

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    drawLine(
                        color = ColorUnarchived,
                        start = Offset(-40f, 0f),
                        end = Offset(size.width + 40, size.height),
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = ColorPrimary,
                        start = Offset(-40f, 0f),
                        end = Offset((size.width + 35) / 3 * 2, size.height),
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(id = R.string.enter_password),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(40.dp))

            PasswordTextField(
                value = state.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.enter_password_condition),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                errorText = stringResource(id = R.string.enter_password_condition),
                successText = stringResource(id = R.string.available_password),
                isError = state.password.isNotValidPassword() && state.password.isNotEmpty(),
                isSuccess = state.password.isValidPassword(),
            )

            PasswordTextField(
                value = state.passwordConfirm,
                onValueChange = { viewModel.onPasswordConfirmChanged(it) },
                modifier = Modifier.fillMaxWidth().padding(top=20.dp),
                label = stringResource(id = R.string.enter_password_confirm),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                errorText = stringResource(id = R.string.password_not_coincide),
                successText = stringResource(id = R.string.match_password),
                isError = state.password != state.passwordConfirm && state.passwordConfirm.isNotEmpty(),
                isSuccess = state.password == state.passwordConfirm && state.passwordConfirm.isNotEmpty(),
            )

            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .height(50.dp),
                shape = RectangleShape,
                enabled = state.password == state.passwordConfirm && state.passwordConfirm.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorPrimary,
                    contentColor = White,
                    disabledBackgroundColor = Gray2,
                    disabledContentColor = Gray1,
                ),
                onClick = viewModel::onNavigateToNextScreen
            ) {
                Text(
                    text = stringResource(id = R.string.next),
                    fontSize = 16.sp,
                    color = if (state.password == state.passwordConfirm && state.passwordConfirm.isNotEmpty()) White else Gray1,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

        }


        viewModel.collectSideEffect {
            when (it) {
                is AccountSetupSideEffect.NavigateToNextScreen -> onNextClicked()
                AccountSetupSideEffect.NavigateToBackScreen -> onBackClicked()
            }
        }
    }
}