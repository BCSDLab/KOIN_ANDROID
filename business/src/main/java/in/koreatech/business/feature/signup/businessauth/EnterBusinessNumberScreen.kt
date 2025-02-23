package `in`.koreatech.business.feature.signup.businessauth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.textfield.LinedTextField
import `in`.koreatech.business.ui.theme.ColorDisabledButton
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorUnarchived
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun EnterBusinessNumberScreen(
    modifier: Modifier = Modifier,
    viewModel: BusinessAuthViewModel = hiltViewModel(),
    scrollState: ScrollState = rememberScrollState(),
    onBackClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {},
) {
    val state = viewModel.collectAsState().value

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            IconButton(
                onClick = viewModel::onNavigateToBackScreen,
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
                    fontWeight = Bold,
                    text = stringResource(id = R.string.business_auth)
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
                    end = Offset(size.width + 40, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = R.string.enter_business_registration_number),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(40.dp))

            LinedTextField(
                value = state.companyNumber,
                onValueChange = {
                    viewModel.onCompanyNumberChanged(it)
                },
                modifier = Modifier.fillMaxWidth(),
                isError = state.error != null,
                errorText = if(state.signupContinuationState == SignupContinuationState.CompanyNumberIsDuplicated ) stringResource(id = R.string.business_registration_number_is_duplicated)
                    else stringResource(id = R.string.error_network_unknown),
                label = stringResource(id = R.string.enter_business_registration_number),
                textStyle = TextStyle.Default.copy(fontSize = 15.sp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(4.dp),
                enabled = state.companyNumber.length == 10 && state.error == null,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorPrimary,
                    disabledBackgroundColor = ColorDisabledButton,
                    contentColor = Color.White,
                    disabledContentColor = Color.White,
                ),
                onClick = viewModel::onNavigateToNextScreen,
            )
            {
                Text(
                    text = stringResource(id = R.string.next),
                    fontSize = 15.sp,
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

        }

    }
    viewModel.collectSideEffect {
        when (it) {
            BusinessAuthSideEffect.NavigateToBackScreen -> {
                onBackClicked()
            }

            BusinessAuthSideEffect.NavigateToNextScreen -> {
                onNextClicked()
            }

            BusinessAuthSideEffect.NavigateToSearchStore -> {}
        }

    }

}
