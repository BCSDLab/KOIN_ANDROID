package `in`.koreatech.koin.feature.user.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun UserInfoHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = KoinTheme.colors.neutral100)
            .background(color = KoinTheme.colors.neutral50)
            .padding(vertical = 8.dp, horizontal = 24.dp)
    ) {
        Text(
            text = text,
            style = KoinTheme.typography.regular14.copy(color = KoinTheme.colors.neutral600)
        )
    }
}

@Preview
@Composable
private fun UserInfoHeaderPreview() {
    UserInfoHeader("회원정보")
}
