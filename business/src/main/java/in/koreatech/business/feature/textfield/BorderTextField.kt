package `in`.koreatech.business.feature.textfield

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.ui.theme.ColorMinor
import `in`.koreatech.koin.core.R

@Composable
fun MenuBorderTextField(
    modifier: Modifier = Modifier,
    inputString: String = "",
    index: Int = 0,
    getStringResource: Int = 0,
    onStringChange: (Pair<Int, String>) -> Unit = {},
){
    Box(
        modifier = modifier
            .border(width = 1.dp, color = ColorMinor)
            .height(37.dp)
            .width(170.dp)
        ,
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            modifier = Modifier.padding(start = 8.dp),
            value = if(inputString != stringResource(id = R.string.temp_price)) inputString else "",
            onValueChange = { newValue ->
                onStringChange(Pair(index, newValue))
            },
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 14.sp
            ),
            decorationBox = { innerTextField ->
                if ( inputString == stringResource(id = R.string.temp_price)) {
                    Text(
                        text = stringResource(id = getStringResource),
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    )
                }
                innerTextField()
            }
        )
    }
}

@Preview
@Composable
fun PreviewBorderTextField() {
    MenuBorderTextField()
}