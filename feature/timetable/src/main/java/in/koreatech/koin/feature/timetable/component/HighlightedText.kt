package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.ThemePreviews

@Composable
fun HighlightedText(
    texts: Array<String>,
    highlightIndices: List<Int>,
    defaultStyle: TextStyle,
    highlightStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    val annotatedString =
        buildAnnotatedString {
            pushStyle(
                ParagraphStyle(
                    lineBreak =
                        LineBreak(
                            strategy = LineBreak.Strategy.Balanced,
                            wordBreak = LineBreak.WordBreak.Phrase,
                            strictness = LineBreak.Strictness.Normal,
                        ),
                ),
            )
            texts.forEachIndexed { idx, text ->
                withStyle(
                    style =
                        highlightIndices.find { it == idx }
                            ?.let { highlightStyle.toSpanStyle() }
                            ?: defaultStyle.toSpanStyle(),
                ) {
                    append(text)
                }
            }
        }

    Text(
        text = annotatedString,
        style = defaultStyle,
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

@ThemePreviews
@Composable
private fun HighlightedTextPreview() {
    KoinTheme {
        HighlightedText(
            texts = arrayOf("안녕하세요 ", "강조", "입니다"),
            highlightIndices = listOf(1),
            defaultStyle = KoinTheme.typography.regular15,
            highlightStyle = KoinTheme.typography.bold15,
        )
    }
}
