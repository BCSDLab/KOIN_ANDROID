package `in`.koreatech.koin.feature.signup.ui.complete

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.FilledButtonColors
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.signup.R

@Composable
fun SignUpCompleteScreen(
    finish: () -> Unit = { }
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.width(96.dp),
            painter = painterResource(id = R.drawable.ic_logo_coin_color),
            contentDescription = "Koin logo"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            style = KoinTheme.typography.medium18,
            text = stringResource(R.string.sign_up_complete)
        )

        Spacer(modifier = Modifier.height(60.dp))

        FilledButton(
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            text = stringResource(R.string.sign_up_complete_login),
            colors = FilledButtonColors.Warning,
            onClick = {
                finish()
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        FilledButton(
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            text = stringResource(R.string.sign_up_complete_home),
            colors = FilledButtonColors.Primary,
            onClick = {
                Intent(Intent.ACTION_VIEW).apply {
                    data = "koin://home/home".toUri()
                }.let {
                    context.startActivity(it)
                }
            }
        )
    }
}

@Preview
@Composable
fun SignUpCompleteScreenPreview() {
    SignUpCompleteScreen()
}