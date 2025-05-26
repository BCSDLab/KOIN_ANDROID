package `in`.koreatech.koin.feature.club.ui.detail.component.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R

@Composable
fun DetailQnaTextField(
    value: String,
    modifier: Modifier = Modifier,
    onSendClick: () -> Unit = {},
    onValueChange: (String) -> Unit = {},
    textStyle: TextStyle = KoinTheme.typography.regular14,
    textFieldColor: Color = KoinTheme.colors.primary500,
    hint: String = stringResource(R.string.detail_qna_text_field_hint),
    isSendIconVisible: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 10.dp, vertical = 7.dp)
) {
    BasicTextField(
        modifier = Modifier,
        value = value,
        textStyle = textStyle,
        onValueChange = { onValueChange(it) },
        decorationBox = { innerTextField ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = textFieldColor,
                        shape = KoinTheme.shapes.extraSmall
                    )
                    .background(KoinTheme.colors.neutral100)
                    .padding(contentPadding)
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            color = KoinTheme.colors.neutral600,
                            style = textStyle
                        )
                    }
                    innerTextField()
                }
                if (isSendIconVisible) {
                    Image(
                        painter = painterResource(id = R.drawable.icon_qna_send),
                        contentDescription = "QuestionDelete",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onSendClick() }
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun DetailQnaTextFieldTextPreview() {
    DetailQnaTextField(
        value = "답변"
    )
}

@Preview
@Composable
fun DetailQnaTextFieldHintPreview() {
    DetailQnaTextField(
        value = ""
    )
}

@Preview
@Composable
fun DetailQnaTextFieldNoIconPreview() {
    DetailQnaTextField(
        value = "",
        isSendIconVisible = false
    )
}
