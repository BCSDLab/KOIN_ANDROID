package `in`.koreatech.koin.feature.club.ui.detail.component.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R

@Composable
fun DetailQnaTextField(
    value: String,
    hint: String = "답변을 입력해주세요.",
    onValueChange: () -> Unit,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .border(
            width = 1.dp,
            color = Color(0xFFCE86FD),
            shape = RoundedCornerShape(size = 4.dp)
        )
        .background(KoinTheme.colors.neutral100)
        .padding(
            horizontal = 10.dp,
            vertical = 10.dp
        )
) {
    var text by remember { mutableStateOf("$value") }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        if (text.isEmpty()) {
            Text(
                text = hint,
                color = KoinTheme.colors.neutral600,
                style = KoinTheme.typography.regular14
            )
        }
        BasicTextField(
            value = text,
            onValueChange = { onValueChange() },
            textStyle = KoinTheme.typography.regular14,
            modifier = Modifier
                .weight(1f)
        )
        Image(
            painter = painterResource(id = R.drawable.finish_6),
            contentDescription = "QuestionDelete",
            modifier = Modifier
                .size(20.dp)
                .clickable { onButtonClick() }
        )
    }
}


@Preview
@Composable
fun DetailQnaTextFieldTextPreview() {
    DetailQnaTextField(
        value = "답변",
        onValueChange = {},
        onButtonClick = {}
    )
}
@Preview
@Composable
fun DetailQnaTextFieldHintPreview() {
    DetailQnaTextField(
        value = "",
        onValueChange = {},
        onButtonClick = {}
    )
}