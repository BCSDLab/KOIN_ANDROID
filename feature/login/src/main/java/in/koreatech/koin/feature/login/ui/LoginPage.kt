package `in`.koreatech.koin.feature.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.login.R
import `in`.koreatech.koin.feature.login.ui.component.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Composable
fun LoginPage(
    viewModel: LoginViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    val id by viewModel.id.collectAsState()
    val password by viewModel.password.collectAsState()
    val isPasswordVisible by viewModel.isPasswordVisible.collectAsState()
    val isUserAlertVisible by viewModel.isUserAlertVisible.collectAsState()
    val isIdPwAlertVisible by viewModel.isIdPwAlertVisible.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .padding(start = 40.dp, top = 204.dp) // offset 대신 padding 사용
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_coin_color),
                contentDescription = "Koin Icon",
                modifier = Modifier
                    .size(width = 107.dp, height = 60.dp)
            )
        }

        Column( // #1
            modifier = Modifier
                .padding(top = 296.dp, bottom = 32.dp, start = 48.dp, end = 48.dp)
                .fillMaxHeight()
                .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoginInputField(
                label = stringResource(R.string.id_field_hint),
                text = id,
                onTextChanged = viewModel::onIdChanged,
                onClear = viewModel::clearId
            )
            AlertMessage(
                text = stringResource(R.string.userAlert),
                isVisible = isUserAlertVisible,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LoginPasswordField(
                label = stringResource(R.string.pw_field_hint),
                text = password,
                isVisible = isPasswordVisible,
                onTextChanged = viewModel::onPasswordChanged,
                onToggleVisibility = viewModel::togglePasswordVisibility
            )
            AlertMessage(
                text = stringResource(R.string.idPwAlert),
                isVisible = isIdPwAlertVisible,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(36.dp))
            LoginTextButton(
                text = stringResource(R.string.login_button_text),
                color = KoinTheme.colors.sub500,
                onClick = {
                    coroutineScope.launch { viewModel.login() }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            LoginTextButton(
                text = stringResource(R.string.signup_button_text),
                color = KoinTheme.colors.primary500,
                onClick = viewModel::signup
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .wrapContentSize(Alignment.Center),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                LoginTextImageButton(
                    icon = R.drawable.ic_login_find_login_id,
                    text = stringResource(R.string.find_id_button_text),
                    imgSize = 16.dp,
                    onClick = viewModel::findId
                )
                LoginTextView(
                    color = KoinTheme.colors.neutral500,
                    text = stringResource(R.string.separator),
                    fontSize = 15
                )
                LoginTextImageButton(
                    icon = R.drawable.ic_login_find_password,
                    text = stringResource(R.string.find_pw_button_text),
                    onClick = viewModel::findPw
                )
                LoginTextView(
                    color = KoinTheme.colors.neutral500,
                    text = stringResource(R.string.separator),
                    fontSize = 15
                )
                LoginTextImageButton(
                    icon = R.drawable.ic_login_tour,
                    text = stringResource(R.string.tour_button_text),
                    onClick = viewModel::tour
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            LoginTextView(
                color = KoinTheme.colors.sub500,
                text = stringResource(R.string.guide),
                fontSize = 18,
                onClick = viewModel::business
            )
            Spacer(modifier = Modifier.height(24.dp))
            LoginTextView(
                color = KoinTheme.colors.neutral800,
                text = stringResource(R.string.copyright),
                fontSize = 12
            )
        }
    }
}
