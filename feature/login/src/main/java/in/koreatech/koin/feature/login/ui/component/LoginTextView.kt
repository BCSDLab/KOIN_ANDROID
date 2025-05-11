package `in`.koreatech.koin.feature.login.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp


@Composable
fun LoginTextView(
    color: Color = Color.Black,
    text: String,
    fontSize: Int,
    onClick: (() -> Unit) ?= null
) {
    val modifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier

    Text(
        text = text,
        color = color,
        modifier = modifier,
        style = TextStyle(
            fontWeight = FontWeight.W400,
            fontSize = fontSize.sp,
            lineHeight = 1f.em,
            letterSpacing = 0.sp
        )
    )
}
