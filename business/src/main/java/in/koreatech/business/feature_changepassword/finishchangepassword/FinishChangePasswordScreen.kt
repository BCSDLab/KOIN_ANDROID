package `in`.koreatech.business.feature_changepassword.finishchangepassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.Blue1
import `in`.koreatech.business.ui.theme.ColorPrimary

@Composable
fun FinishChangePasswordScreen(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = modifier
                .padding(top = 56.dp, start = 10.dp , bottom = 18.dp)
                .width(40.dp)
                .height(40.dp)
                .clickable {  }

        ) {
            Image(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "backArrow",
                modifier = modifier
                    .width(24.dp)
                    .height(24.dp)
                    .clickable { }
            )
        }

        Image(
            painter = painterResource(id = R.drawable.finish_6),
            contentDescription = "finish_mark",
            alignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .height(55.dp)
                .width(55.dp)
        )

        Text(
            text = stringResource(R.string.password_change_complete),
            fontSize = 24.sp,
            color = ColorPrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 46.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = stringResource(R.string.please_sign_in_with_new_password),
            fontSize = 16.sp,
            color = Blue1,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 46.dp)
                .padding(bottom = 51.dp)
        )

        Button(
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(ColorPrimary),
            shape = RectangleShape,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 33.dp)
                .height(44.dp)
        ) {
            Text(
                text = stringResource(id = R.string.go_login_activity),
                fontSize = 15.sp
            )
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
