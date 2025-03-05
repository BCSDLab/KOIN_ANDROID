package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.R

@Composable
fun TimetableInputField(
    text: String,
    title: String,
    isError: Boolean = false,
    modifier: Modifier = Modifier,
    optional: Boolean = true,
    onValueChange: (text: String) -> Unit = {},
) {
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult =
        textMeasurer.measure(
            text = stringResource(id = R.string.timetable_input_field_option_character),
            style = KoinTheme.typography.regular16,
        )
    val optionalCharacterWidth = textLayoutResult.size.width
    Column(
        modifier = modifier,
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                when (title) {
                    "일정명" -> if (it.length <= 100) onValueChange(it)
                    "교수명" -> if (it.length <= 30) onValueChange(it)
                    "장소" -> if (it.length <= 30) onValueChange(it)
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(35.dp),
            maxLines = 1,
            cursorBrush = SolidColor(Color.Black),
            textStyle =
                KoinTheme.typography.regular12.copy(
                    color = KoinTheme.colors.neutral500,
                ),
            decorationBox = { innerTextField ->
                Row(
                    modifier =
                        Modifier
                            .height(IntrinsicSize.Min)
                            .fillMaxWidth()
                            .background(Color.White)
                            .border(
                                width = 1.dp,
                                color =
                                    if (isError) {
                                        KoinTheme.colors.sub500
                                    } else {
                                        KoinTheme.colors.neutral300
                                    },
                                shape = RoundedCornerShape(4.dp),
                            )
                            .padding(vertical = 4.dp, horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (optional) {
                        Spacer(modifier = Modifier.width(LocalDensity.current.run { optionalCharacterWidth.toDp() }))
                    } else {
                        Text(
                            text = stringResource(id = R.string.timetable_input_field_option_character),
                            style = KoinTheme.typography.regular16,
                            color = KoinTheme.colors.sub500,
                            modifier = Modifier.padding(end = 1.dp, bottom = 1.dp),
                        )
                    }
                    Text(
                        text = title,
                        style = KoinTheme.typography.bold12,
                        color = KoinTheme.colors.neutral800,
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    VerticalDivider(
                        thickness = 2.dp,
                        modifier = Modifier.fillMaxHeight(),
                        color = KoinTheme.colors.neutral300,
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Box {
                        text.ifEmpty {
                            Text(
                                text =
                                    stringResource(
                                        id = R.string.timetable_input_field_placeholder,
                                        title,
                                    ),
                                style = KoinTheme.typography.regular12,
                                color = KoinTheme.colors.neutral500,
                            )
                        }
                        innerTextField()
                    }
                }
            },
        )
        if (isError) {
            Text(
                text = stringResource(R.string.timetable_input_field_lecture_name_placeholder),
                style = KoinTheme.typography.regular12,
                color = KoinTheme.colors.sub500,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimetableInputFieldPreview() {
    TimetableInputField(
        text = "일정명",
        title = "일정명",
        isError = true,
        optional = false,
        modifier = Modifier.padding(2.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun TimetableInputFieldPreview_optional() {
    TimetableInputField(
        text = "일정명",
        title = "일정명",
        isError = false,
        optional = true,
        modifier = Modifier.padding(2.dp),
    )
}
