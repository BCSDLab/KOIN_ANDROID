package `in`.koreatech.koin.feature.club.ui.clubdetail.component.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
    hintColor: Color = KoinTheme.colors.neutral600,
    isSendIconVisible: Boolean = true,
    sendIcon: Painter = painterResource(id = R.drawable.icon_qna_send),
    sendIconModifier: Modifier = Modifier,
    isError: Boolean = false,
    errorColor: Color = KoinTheme.colors.sub500,
    errorMessage: String = "",
    errorTextStyle: TextStyle = KoinTheme.typography.regular14,
    errorIcon: Painter = painterResource(id = R.drawable.icon_exclamation),
    errorIconModifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 10.dp, vertical = 7.dp)
) {
    val inputMaxLenght = 255
    Column(
        verticalArrangement = Arrangement
            .spacedBy(4.dp)
    ) {
        BasicTextField(
            modifier = Modifier.heightIn(max = 300.dp),
            value = value,
            textStyle = textStyle,
            onValueChange = {
                if (it.length < inputMaxLenght) {
                    onValueChange(it)
                } else {
                    onValueChange(it.take(inputMaxLenght))
                }
            },
            decorationBox = { innerTextField ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = if (isError) errorColor else textFieldColor,
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
                                color = hintColor,
                                style = textStyle
                            )
                        }
                        innerTextField()
                    }
                    if (isSendIconVisible) {
                        Image(
                            painter = sendIcon,
                            contentDescription = "TextSend",
                            modifier = sendIconModifier
                                .size(20.dp)
                                .clickable { onSendClick() }
                        )
                    }
                }
            }
        )
        if (isError) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = errorIcon,
                    contentDescription = "TextField Error",
                    modifier = errorIconModifier
                        .size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = errorMessage,
                    style = errorTextStyle,
                    color = errorColor
                )
            }
        }
    }
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

@Preview
@Composable
fun DetailQnaTextFieldErrorPreview() {
    DetailQnaTextField(
        value = "",
        isSendIconVisible = false,
        isError = true,
        errorMessage = "필수 입력란입니다."
    )
}
