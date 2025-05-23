package `in`.koreatech.koin.feature.signin.ui

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.FilledButtonColors
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.signin.DEEPLINK_FIND_PASSWORD
import `in`.koreatech.koin.feature.signin.DEEPLINK_SIGN_UP
import `in`.koreatech.koin.feature.signin.R
import `in`.koreatech.koin.feature.signin.component.KoinSignInBasicTextField
import `in`.koreatech.koin.feature.signin.component.KoinSignInPasswordTextField
import `in`.koreatech.koin.feature.signin.component.KoinSignInTextButton
import `in`.koreatech.koin.feature.signin.component.KoinSignInTextFieldAlert
import `in`.koreatech.koin.feature.signin.component.KoinSignInTextFieldAlertState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    nextRoute: () -> Unit = { },
    viewModel: SignInViewModel = hiltViewModel()
) {
    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect {
        handleSideEffect(
            sideEffect = it,
            nextRoute = nextRoute
        )
    }

    SignInScreenImpl(
        loginId = uiState.loginId,
        password = uiState.password,
        showPassword = uiState.showPassword,
        isError = uiState.loginError.isError,
        modifier = modifier,
        setLoginId = {
            viewModel.setLoginId(it)
        },
        setPassword = {
            viewModel.setPassword(it)
        },
        setShowPassword = {
            viewModel.setShowPassword(it)
        },
        signIn = {
            viewModel.signIn()
        },
        nextRoute = nextRoute
    )
}

@Composable
fun SignInScreenImpl(
    loginId: String,
    password: String,
    showPassword: Boolean,
    isError: Boolean,
    modifier: Modifier = Modifier,
    setLoginId: (String) -> Unit = { },
    setPassword: (String) -> Unit = { },
    setShowPassword: (Boolean) -> Unit = { },
    signIn: () -> Unit = { },
    nextRoute: () -> Unit = { }
) {
    val scrollState = rememberScrollState()

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp)
            .verticalScroll(scrollState)
            .imePadding(),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = modifier.weight(1f))

        Image(
            modifier = Modifier.height(60.dp),
            painter = painterResource(id = R.drawable.ic_logo_coin_color),
            contentDescription = "Koin logo"
        )

        KoinSignInBasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            value = loginId,
            onValueChange = setLoginId,
            hint = stringResource(R.string.sign_in_login_id_hint),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        KoinSignInPasswordTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            value = password,
            onValueChange = setPassword,
            hint = stringResource(R.string.sign_in_password_hint),
            singleLine = true,
            showPassword = showPassword,
            onShowPasswordChange = setShowPassword
        )

        Box(
            modifier = Modifier.height(48.dp)
        ) {
            if (isError) {
                KoinSignInTextFieldAlert(
                    text = stringResource(R.string.sign_in_error),
                    state = KoinSignInTextFieldAlertState.Warning
                )
            }
        }

        FilledButton(
            modifier = Modifier.fillMaxWidth(),
            colors = FilledButtonColors.Warning,
            shape = KoinTheme.shapes.small,
            text = stringResource(R.string.sign_in_sign_in),
            onClick = signIn
        )

        Spacer(modifier = Modifier.height(24.dp))

        FilledButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.sign_in_sign_up),
            shape = KoinTheme.shapes.small,
            onClick = {
                Intent(Intent.ACTION_VIEW).apply {
                    data = DEEPLINK_SIGN_UP.toUri()
                }.let {
                    context.startActivity(it)
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            KoinSignInTextButton(
                modifier = Modifier,
                text = stringResource(R.string.sign_in_find_login_id),
                icon = painterResource(R.drawable.ic_sign_in_find_login_id)
            ) {
                // TODO("Implement after find id feature is implemented")
            }

            Text(
                text = "|",
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            KoinSignInTextButton(
                modifier = Modifier,
                text = stringResource(R.string.sign_in_find_password),
                icon = painterResource(R.drawable.ic_sign_in_find_password)
            ) {
                Intent(Intent.ACTION_VIEW).apply {
                    data = DEEPLINK_FIND_PASSWORD.toUri()
                }.let {
                    context.startActivity(it)
                }
            }

            Text(
                text = "|",
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            KoinSignInTextButton(
                modifier = Modifier,
                text = stringResource(R.string.sign_in_tour),
                icon = painterResource(R.drawable.ic_sign_in_tour)
            ) {
                nextRoute()
            }
        }

        Spacer(modifier = modifier.weight(1f))

        Column(
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = stringResource(R.string.sign_in_business),
                color = KoinTheme.colors.sub500,
                textAlign = TextAlign.Center,
                style = KoinTheme.typography.regular18,
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable {
                    }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                style = KoinTheme.typography.regular12,
                text = stringResource(R.string.copy_right),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SignInScreenPreview() {
    SignInScreenImpl(
        loginId = "",
        password = "",
        showPassword = false,
        isError = true
    )
}

fun handleSideEffect(
    sideEffect: SignInSideEffect,
    nextRoute: () -> Unit
) {
    when (sideEffect) {
        is SignInSideEffect.SignInSuccess -> {
            nextRoute()
        }
    }
}
