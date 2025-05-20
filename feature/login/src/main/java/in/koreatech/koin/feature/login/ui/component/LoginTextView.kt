package `in`.koreatech.koin.feature.login.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle


@Composable
fun LoginTextView(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    text: String,
    style: TextStyle,
    onClick: () -> Unit = { }
) {
    Text(
        text = text,
        color = color,
        modifier = modifier.clickable { onClick() },
        style = style
    )
}
