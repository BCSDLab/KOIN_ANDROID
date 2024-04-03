package `in`.koreatech.business.feature_changepassword.changepassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.Blue1
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.Gray5
import `in`.koreatech.business.ui.theme.Red2

@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    onChangePwButtonClicked: () -> Unit,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    var password  by remember { mutableStateOf("") }
    var passwordChecked  by remember { mutableStateOf("") }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = modifier
                .padding(top = 56.dp, start = 10.dp , bottom = 18.dp)
                .width(40.dp)
                .height(40.dp)
                .clickable { onBackPressed() }

        ) {
            Image(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "backArrow",
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
            value = password,
            onValueChange = {
                password = it
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
                        if (password.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.new_password),
                                color = Blue1,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                    Divider(
                        color = if (password.isEmpty()) Blue1 else Color.Black,
                        thickness = 1.dp,
                        modifier = modifier.fillMaxWidth()
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
            text = context.getString(R.string.password_condition),
            fontSize = 11.sp,
            color = Blue1,
            modifier = modifier
                .padding(start = 32.dp, bottom = 12.dp)
        )

        BasicTextField(
            value = passwordChecked,
            onValueChange = {
                passwordChecked = it
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
                        if (passwordChecked.isEmpty()) {
                            Text(
                                text = stringResource(id = R.string.confirm_password),
                                color = Blue1,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                    Divider(
                        color = if (passwordChecked.isEmpty()) Blue1 else Color.Black,
                        thickness = 1.dp,
                        modifier = modifier.fillMaxWidth()
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
                text = context.getString(R.string.not_coincide_password),
                fontSize = 11.sp,
                color = Red2,
            )
        }

        Button(
            onClick = { onChangePwButtonClicked() },
            shape = RectangleShape,
            colors = if(password.isBlank() || passwordChecked.isBlank()) ButtonDefaults.buttonColors(Gray5)
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
}

@Preview
@Composable
fun previewChangePassswordScreen() {
    Surface {
        ChangePasswordScreen(onChangePwButtonClicked = {}, onBackPressed = {})
    }
}