package `in`.koreatech.business.feature.signup.completesignup

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorDescription
import `in`.koreatech.business.ui.theme.ColorDisabledButton
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorUnarchived
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CompleteSignupScreen(
    modifier: Modifier = Modifier,
    viewModel: CompleteSignupViewModel= hiltViewModel(),
    onNavigateToLoginScreen: () -> Unit = {},
    onBackClicked: () -> Unit = {}
) {
    Column(
        modifier = modifier,
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            IconButton(
                onClick = { viewModel.onBackButtonClicked() },
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
                fontWeight = Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp),
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
                    fontWeight = Bold,
                    text = stringResource(id = R.string.business_auth),
                )
                Text(
                    text = stringResource(id = R.string.three_third),
                    color = ColorPrimary,
                    fontWeight = Bold,
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
                    end = Offset(size.width + 35, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = ColorPrimary,
                    start = Offset(-40f, 0f),
                    end = Offset((size.width + 40), size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

        }
        Spacer(modifier = Modifier.height(123.dp))

        Column(
            modifier = Modifier.padding(horizontal = 32.dp),
            verticalArrangement = Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(276.dp),
                painter = painterResource(id = R.drawable.complete_signup),
                contentDescription = stringResource(id = R.string.signup_request_complete),
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.signup_request_complete),
                fontSize = 24.sp,
                color = ColorPrimary,
                fontWeight = Bold,
            )
            Spacer(modifier = Modifier.height(18.dp))

            Text(
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.signup_request_complete_description),
                fontSize = 16.sp,
                color = ColorDescription,
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .height(44.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorPrimary,
                    disabledBackgroundColor = ColorDisabledButton,
                    contentColor = Color.White,
                    disabledContentColor = Color.White,
                ),
                onClick = { viewModel.onNavigateToLoginScreen() }) {
                Text(
                    text = stringResource(id = R.string.navigate_to_login_screen),
                    fontSize = 15.sp,
                    color = Color.White,

                    )
            }

        }
        viewModel.collectSideEffect {
            when (it) {
                is CompleteSignupSideEffect.NavigateToLoginScreen -> {
                    onNavigateToLoginScreen()
                }
                is CompleteSignupSideEffect.NavigateToBackScreen -> {
                    onBackClicked()
                }
            }
        }

    }
}


@Composable
@Preview
fun Preview() {
    Surface(
        modifier = Modifier
            .fillMaxSize()

    ) {
            CompleteSignupScreen(
                onBackClicked = {  }
            )

    }
}
