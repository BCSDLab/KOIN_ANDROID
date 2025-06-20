package `in`.koreatech.koin.feature.user.ui.findpassword.complete

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.user.R
import `in`.koreatech.koin.feature.user.ui.signin.SignInActivity

@Composable
fun FindPasswordCompleteScreen() {
    val context = LocalContext.current

    BackHandler {
        (context as Activity).finish()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(55.dp),
                contentDescription = null,
                imageVector = ImageVector.vectorResource(R.drawable.ic_find_password_complete)
            )

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = stringResource(R.string.find_password_change_password_success),
                style = KoinTheme.typography.bold20.copy(fontSize = 24.sp),
                color = KoinTheme.colors.primary500
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.find_password_change_password_success_message),
                style = KoinTheme.typography.medium16,
                color = Color(0xFF8E8E8E), // Not in KoinTheme.colors
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            FilledButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.find_password_change_password_login),
                onClick = {
                    Intent(context, SignInActivity::class.java).let {
                        context.startActivity(it)
                    }
                },
                shape = KoinTheme.shapes.small,
                contentPadding = PaddingValues(10.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FindPasswordCompleteScreenPreview() {
    FindPasswordCompleteScreen()
}
