package `in`.koreatech.koin.feature.signup.ui.usertype

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.FilledButtonColors
import `in`.koreatech.koin.feature.signup.R
import `in`.koreatech.koin.feature.signup.component.KoinSignUpProgressHeader
import `in`.koreatech.koin.feature.signup.component.KoinSignUpProgressIndicator

@Composable
fun SignUpUserType(
    navigateToStudentScreen: () -> Unit = { },
    navigateToGeneralScreen: () -> Unit = { }
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        KoinSignUpProgressHeader(
            text = stringResource(R.string.sign_up_user_type),
            currentStep = 3,
            maxStep = 4
        )

        Spacer(modifier = Modifier.height(8.dp))

        KoinSignUpProgressIndicator(
            currentStep = 3,
            maxStep = 4
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = Modifier.width(96.dp),
            painter = painterResource(id = R.drawable.ic_logo_coin_color),
            contentDescription = "Koin logo"
        )

        Spacer(modifier = Modifier.height(80.dp))

        FilledButton(
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            text = stringResource(R.string.sign_up_user_type_student),
            colors = FilledButtonColors.Warning,
            onClick = { navigateToStudentScreen() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        FilledButton(
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            text = stringResource(R.string.sign_up_user_type_general),
            colors = FilledButtonColors.Primary,
            onClick = { navigateToGeneralScreen() }
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpUserTypePreview() {
    SignUpUserType()
}
