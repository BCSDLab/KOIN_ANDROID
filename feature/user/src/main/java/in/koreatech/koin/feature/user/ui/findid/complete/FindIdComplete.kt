package `in`.koreatech.koin.feature.user.ui.findid.complete

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.FilledButtonColors
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.user.R
import `in`.koreatech.koin.feature.user.ui.findpassword.FindPasswordActivity
import `in`.koreatech.koin.feature.user.ui.signin.SignInActivity
import `in`.koreatech.koin.feature.user.util.stringResourceWithStyle

@Composable
fun FindIdComplete(
    loginId: String
) {
    val context = LocalContext.current

    BackHandler {
        (context as Activity).finish()
    }

    FindIdCompleteImpl(
        loginId = loginId
    )
}

@Composable
fun FindIdCompleteImpl(
    loginId: String
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.find_id_complete_title),
            style = KoinTheme.typography.bold20.copy(fontSize = 24.sp),
            color = KoinTheme.colors.primary500
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResourceWithStyle(
                R.string.find_id_complete_message,
                Pair(loginId, KoinTheme.typography.bold20.copy(color = KoinTheme.colors.neutral800).toSpanStyle())
            ),
            style = KoinTheme.typography.medium16,
            color = Color(0xFF8E8E8E) // TODO: Move to design system
        )

        Spacer(modifier = Modifier.height(64.dp))

        FilledButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.find_id_go_to_login),
            onClick = {
                Intent(context, SignInActivity::class.java).let {
                    context.startActivity(it)
                }
                (context as Activity).finish()
            },
            colors = FilledButtonColors.Primary,
            contentPadding = PaddingValues(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        FilledButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.find_id_go_to_find_password),
            onClick = {
                Intent(context, FindPasswordActivity::class.java).let {
                    context.startActivity(it)
                }
                (context as Activity).finish()
            },
            colors = FilledButtonColors.Warning,
            contentPadding = PaddingValues(12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FindIdCompletePreview() {
    FindIdCompleteImpl(
        loginId = "test"
    )
}
