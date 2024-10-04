package `in`.koreatech.business.feature.findpassword.finishchangepassword

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.Gray6

@Composable
fun FinishChangePasswordScreen(
    navigateToSignInScreen: () -> Unit ={},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                IconButton(
                    onClick = { /*TODO*/ },
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
            Spacer(modifier = Modifier.height(32.dp))
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
                        style = MaterialTheme.typography.h6,
                        text = stringResource(R.string.change_password_step)
                    )
                    Text(
                        text = stringResource(id = R.string.two_half),
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
                        color = ColorPrimary,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                Spacer(modifier = Modifier.height(66.dp))
                Image(
                    painter = painterResource(id = R.drawable.complete_change_password),
                    contentDescription = "finish_mark",
                    alignment = Alignment.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )
                Text(
                    text = stringResource(R.string.password_change_complete),
                    fontSize = 18.sp,
                    color = ColorPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 46.dp)
                )
                Text(
                    text = stringResource(R.string.password_please_sign_in_with_new_password),
                    fontSize = 16.sp,
                    color = Gray6,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { navigateToSignInScreen() },
                    colors = ButtonDefaults.buttonColors(ColorPrimary),
                    modifier = modifier
                        .padding(bottom = 24.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.go_to_login_screen),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun previewFinishScreen() {
    Surface {
        FinishChangePasswordScreen()
    }
}
