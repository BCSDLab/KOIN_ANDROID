package `in`.koreatech.business.feature.textfield

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.ui.theme.ColorMinor

@Composable
fun BorderTextField(
    startPadding: Dp = 10.dp,
    inputString: String = "",
    onStringChange: (String) -> Unit = {},
){
    Box(
        modifier = Modifier
            .padding(start = startPadding)
            .border(width = 1.dp, color = ColorMinor)
            .height(37.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = inputString,
            onValueChange = onStringChange,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 14.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun PreviewBorderTextField() {
    BorderTextField()
}