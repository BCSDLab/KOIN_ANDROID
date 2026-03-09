package `in`.koreatech.koin.feature.callvan.ui.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.callvan.R
import `in`.koreatech.koin.feature.callvan.TEXT_FIELD_MAX_LENGTH

@Composable
fun ItemSearchTextField(
    value: String,
    modifier: Modifier = Modifier,
    maxLength: Int = TEXT_FIELD_MAX_LENGTH,
    hint: String = stringResource(R.string.callvan_search_hint),
    textStyle: TextStyle = KoinTheme.typography.regular14.copy(color = KoinTheme.colors.neutral600),
    onValueChange: (String) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = KoinTheme.colors.neutral100,
                shape = KoinTheme.shapes.extraSmall
            )
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            modifier = Modifier.weight(1f),
            value = value,
            textStyle = textStyle,
            singleLine = true,
            onValueChange = {
                if (it.length < maxLength) {
                    onValueChange(it)
                } else {
                    onValueChange(it.take(maxLength))
                }
            },
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            color = KoinTheme.colors.neutral600
                        )
                    }
                    innerTextField()
                }
            }
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_search_vector),
            contentDescription = ""
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ItemSearchTextFieldPreview() {
    ItemSearchTextField(
        value = "",
        hint = "검색어를 입력해주세요.",
        onValueChange = {}
    )
}
