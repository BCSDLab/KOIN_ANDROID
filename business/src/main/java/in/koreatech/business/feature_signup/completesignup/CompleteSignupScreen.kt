package `in`.koreatech.business.feature_signup.completesignup

import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorActiveButton
import `in`.koreatech.business.ui.theme.ColorDisabledButton
import `in`.koreatech.business.ui.theme.ColorDescription
import `in`.koreatech.business.ui.theme.KOIN_ANDROIDTheme
import `in`.koreatech.business.ui.theme.ColorEmphasis

@Composable
fun CompleteSignupScreen(modifier: Modifier = Modifier, onBackClicked: () -> Unit = {}) {
    Column(
        modifier = modifier,
    ) {
        IconButton(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = {onBackClicked()}
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(id = R.string.back_icon),
            )
        }

        Spacer(modifier = Modifier.height(123.dp))

        Column(
            modifier = Modifier.padding(horizontal = 32.dp),
            verticalArrangement = Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(55.dp),
                painter = painterResource(id = R.drawable.signup_check),
                contentDescription = stringResource(id = R.string.check_icon),
                tint = ColorEmphasis
            )
            Spacer(modifier = Modifier.height(25.dp))

            Text(
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.signup_request_complete),
                fontSize = 24.sp,
                color = ColorActiveButton,
                fontWeight = Bold,
            )
            Spacer(modifier = Modifier.height(18.dp))

            Text(
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.signup_request_complete_description),
                fontSize = 16.sp,
                color = ColorDescription,
            )

            Spacer(modifier = Modifier.height(51.dp))
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ColorActiveButton,
                    disabledContainerColor = ColorDisabledButton,
                    contentColor = Color.White,
                    disabledContentColor = Color.White,
                ),
                onClick = {onBackClicked()}) {
                Text(
                    text = stringResource(id = R.string.navigate_to_login_screen),
                    fontSize = 15.sp,
                    color = Color.White,

                    )
            }

        }

    }
}
