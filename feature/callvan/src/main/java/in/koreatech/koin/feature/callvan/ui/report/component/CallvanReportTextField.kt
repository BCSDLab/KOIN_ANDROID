package `in`.koreatech.koin.feature.callvan.ui.report.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
internal fun CallvanReportTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    maxLength: Int = Int.MAX_VALUE,
    readOnly: Boolean = false,
    contentAlignment: Alignment = Alignment.CenterStart,
    isError: Boolean = false
) {
    val borderColor = if (isError) RebrandKoinTheme.colors.sub500 else KoinTheme.colors.neutral300
    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, color = borderColor, shape = KoinTheme.shapes.extraSmall)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        value = value,
        readOnly = readOnly,
        textStyle = KoinTheme.typography.regular14.copy(color = KoinTheme.colors.neutral800),
        onValueChange = { onValueChange(it.take(maxLength)) },
        decorationBox = { innerTextField ->
            Box(contentAlignment = contentAlignment) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = KoinTheme.typography.regular14,
                        color = KoinTheme.colors.neutral500
                    )
                }
                innerTextField()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportTextFieldPreview() {
    CallvanReportTextField(
        value = "",
        onValueChange = {},
        placeholder = "내용을 입력해주세요."
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportTextFieldFilledPreview() {
    CallvanReportTextField(
        value = "입력된 내용 예시",
        onValueChange = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportTextFieldMultilinePreview() {
    CallvanReportTextField(
        modifier = Modifier.height(200.dp),
        value = "",
        onValueChange = {},
        placeholder = "신고 상황을 확인하기 위해 자세히 작성해주세요.",
        contentAlignment = Alignment.TopStart
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanReportTextFieldErrorPreview() {
    CallvanReportTextField(
        value = "",
        onValueChange = {},
        placeholder = "내용을 입력해주세요.",
        isError = true
    )
}
