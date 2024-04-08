package `in`.koreatech.business.feature_changepassword.changepassword


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.Blue1
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.Gray5
import `in`.koreatech.business.ui.theme.Red2
import `in`.koreatech.business.util.showMessage
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect


@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    navigateToFinish: () -> Unit,
    onBackPressed: () -> Unit,
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {

    val state = viewModel.collectAsState().value
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = modifier
                .padding(top = 56.dp, start = 10.dp, bottom = 18.dp)
                .width(40.dp)
                .height(40.dp)
                .clickable { onBackPressed() }

        ) {
            Image(
                painter = painterResource(R.drawable.back_ic),
                contentDescription = stringResource(id = R.string.back_arrow),
                modifier = modifier
                    .width(24.dp)
                    .height(24.dp)
            )
        }

        Text(
            text = stringResource(R.string.change_password),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(top = 32.dp, start = 32.dp, bottom = 32.dp)
        )

        BasicTextField(
            value = state.password,
            onValueChange = {
                viewModel.insertPassword(it)
            },
            maxLines = 1,
            textStyle = TextStyle(fontSize = 15.sp),
            decorationBox = { innerTextField ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = modifier.padding(bottom = 7.dp)
                    ){
                        if (state.password.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.new_password),
                                color = Blue1,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                    HorizontalDivider(
                        modifier = modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = if (state.password.isEmpty()) Blue1 else Color.Black
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
            value = state.passwordChecked,
            onValueChange = {
                viewModel.insertPasswordChecked(it)
            },
            maxLines = 1,
            textStyle = TextStyle(fontSize = 15.sp),
            decorationBox = { innerTextField ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = modifier.padding(bottom = 7.dp)
                    ){
                        if (state.passwordChecked.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.confirm_password),
                                color = Blue1,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                    HorizontalDivider(
                        modifier = modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = if (state.passwordChecked.isEmpty()) Blue1 else Color.Black
                    )
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(bottom = 8.dp)
                .height(30.dp)
        )

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 32.dp, bottom = 217.5.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.exclamation),
                contentDescription = "examationMark",
                modifier = modifier
                    .padding(end = 5.dp)
                    .width(16.dp)
                    .height(16.dp)
            )

            Text(
                text = stringResource(R.string.not_coincide_password),
                fontSize = 11.sp,
                color = Red2,
            )
        }

        Button(
            onClick = { viewModel.changePassword(state.email, state.password, state.passwordChecked) },
            shape = RectangleShape,
            colors = if(state.password.isBlank() || state.passwordChecked.isBlank()) ButtonDefaults.buttonColors(Gray5)
            else ButtonDefaults.buttonColors(ColorPrimary),
            modifier = modifier
                .padding(horizontal = 32.dp)
                .padding(bottom = 80.dp)
                .fillMaxWidth()
                .height(44.dp)
        ) {
            Text(
                text = stringResource(id = R.string.changing_password),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
    viewModel.collectSideEffect{
        when(it){
            is ChangePasswordSideEffect.GotoFinishScreen -> navigateToFinish()
            is ChangePasswordSideEffect.NotCoincidePassword -> showMessage("비밀번호가 일치하지 않습니다.")
            is ChangePasswordSideEffect.ToastIsNotPasswordForm -> showMessage("비밀번호는 특수문자 포함 영어와 숫자 조합 6~18 자리입니다.")
            is ChangePasswordSideEffect.ToastNullPassword -> showMessage("비밀번호를 입력해 주세요.")
            is ChangePasswordSideEffect.ToastNullPasswordChecked -> showMessage("확인 비밀번호를 입력해주세요.")
            else -> {}
        }
    }
}

@Preview
@Composable
fun PreviewChangePassswordScreen() {
    Surface {
        ChangePasswordScreen(navigateToFinish = {}, onBackPressed = {})
    }
}