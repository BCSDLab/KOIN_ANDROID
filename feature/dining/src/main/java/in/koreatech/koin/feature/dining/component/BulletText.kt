package `in`.koreatech.koin.feature.dining.component

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

@Composable
fun BulletText(
    text: String,
    modifier: Modifier = Modifier,
    bullet: String = "\u2022",
    color: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current
) {
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult = remember(bullet, style) {
        textMeasurer.measure(text = "$bullet ", style = style)
    }
    val density = LocalDensity.current

    val indentSp = with(density) {
        textLayoutResult.size.width.toSp()
    }

    Text(
        text = buildAnnotatedString {
            withStyle(
                ParagraphStyle(
                    textIndent = TextIndent(
                        firstLine = 0.sp,
                        restLine = indentSp
                    )
                )
            ) {
                append(bullet)
                append(" ")
                append(text)
            }
        },
        modifier = modifier,
        color = color,
        style = style
    )
}
