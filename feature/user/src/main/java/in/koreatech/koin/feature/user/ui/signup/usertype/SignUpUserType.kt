package `in`.koreatech.koin.feature.user.ui.signup.usertype

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.user.R
import `in`.koreatech.koin.feature.user.component.KoinUserProgressHeader
import `in`.koreatech.koin.feature.user.component.KoinUserProgressIndicator

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
        KoinUserProgressHeader(
            text = stringResource(R.string.sign_up_user_type),
            currentStep = 3,
            maxStep = 4
        )

        Spacer(modifier = Modifier.height(8.dp))

        KoinUserProgressIndicator(
            currentStep = 3,
            maxStep = 4
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .width(80.dp)
                    .height(60.dp),
                painter = painterResource(id = R.drawable.ic_bcsd_symbol),
                contentDescription = "Koin logo"
            )

            Spacer(modifier = Modifier.height(4.dp))

            Image(
                modifier = Modifier
                    .width(100.dp)
                    .height(30.dp),
                painter = painterResource(id = R.drawable.ic_koin_text),
                contentDescription = "Koin logo"
            )
        }

        Spacer(modifier = Modifier.height(80.dp))

        FilledButton(
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            text = stringResource(R.string.sign_up_user_type_student),
            colors = ButtonDefaults.buttonColors(
                containerColor = RebrandKoinTheme.colors.primary500
            ),
            shape = KoinTheme.shapes.small,
            onClick = {
                navigateToStudentScreen()
                EventLogger.logClickEvent(
                    EventAction.USER,
                    AnalyticsConstant.Label.CREATE_ACCOUNT,
                    "학생"
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            shape = KoinTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = RebrandKoinTheme.colors.neutral0
            ),
            border = BorderStroke(width = 1.dp, color = RebrandKoinTheme.colors.primary500),
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            onClick = {
                navigateToGeneralScreen()
                EventLogger.logClickEvent(
                    EventAction.USER,
                    AnalyticsConstant.Label.CREATE_ACCOUNT,
                    "외부인"
                )
            }
        ) {
            Text(
                text = stringResource(R.string.sign_up_user_type_general),
                color = RebrandKoinTheme.colors.primary500
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpUserTypePreview() {
    SignUpUserType()
}
