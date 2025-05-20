package `in`.koreatech.koin.feature.login.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun LoginTextButton(
    modifier: Modifier = Modifier,
    text: String,
    color:Color,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = KoinTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
    ) {
        Text(
            text = text,
            color = KoinTheme.colors.neutral0,
            style = KoinTheme.typography.regular15
        )
    }
}
