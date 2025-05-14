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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.feature.login.R
import `in`.koreatech.koin.feature.login.ui.component.*
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
                painter = painterResource(id = R.drawable.ic_color_horizontal_300x168),
                contentDescription = "Koin Icon",
                modifier = Modifier
                    .size(width = 107.dp, height = 60.dp)
            )
        }

        Column( // #1
            modifier = Modifier
                .padding(top = 296.dp, bottom = 32.dp, start = 48.dp, end = 48.dp)
                .fillMaxHeight()
                .navigationBarsPadding(),
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
                color = Color(0xFFF7941E),
                onClick = {
                    coroutineScope.launch { viewModel.login() }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            LoginTextButton(
                text = stringResource(R.string.signup_button_text),
                color = Color(0xFF175C8E),
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
                    icon = R.drawable.ic_zoom,
                    text = stringResource(R.string.find_id_button_text),
                    imgSize = 16.dp,
                    onClick = viewModel::findId
                )
                LoginTextView(
                    color = Color(0xFFA1A1A1),
                    text = stringResource(R.string.separator),
                    fontSize = 15
                )
                LoginTextImageButton(
                    icon = R.drawable.ic_password,
                    text = stringResource(R.string.find_pw_button_text),
                    onClick = viewModel::findPw
                )
                LoginTextView(
                    color = Color(0xFFA1A1A1),
                    text = stringResource(R.string.separator),
                    fontSize = 15
                )
                LoginTextImageButton(
                    icon = R.drawable.ic_face,
                    text = stringResource(R.string.tour_button_text),
                    onClick = viewModel::tour
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            LoginTextView(
                color = Color(0xFFF7941E),
                text = stringResource(R.string.guide),
                fontSize = 18,
                onClick = viewModel::business
            )
            Spacer(modifier = Modifier.height(24.dp))
            LoginTextView(
                color = Color(0xFF252525),
                text = stringResource(R.string.copyright),
                fontSize = 12
            )
        }
    }
}
