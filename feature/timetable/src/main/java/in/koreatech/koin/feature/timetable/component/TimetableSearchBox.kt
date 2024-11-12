package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.icon.StableIcon
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.R

@Composable
fun TimetableSearchBox(
    searchText: String,
    modifier: Modifier = Modifier,
    onSearchTextChange: (String) -> Unit = {},
    onClickSettingIcon: () -> Unit = {},
    onClickSearchIcon: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onClickSettingIcon) {
            StableIcon(
                drawableResId = R.drawable.ic_filter,
                tint = Color(0xFFB5C1CD),
                modifier = Modifier.size(29.dp)
            )
        }
        Spacer(modifier = Modifier.width(31.dp))
        TextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White, // 배경색 (클릭 X)
                focusedContainerColor = Color.White, // 배경색 (클릭 O)
                unfocusedIndicatorColor = Color.Transparent, // 밑줄색 (클릭 X)
                focusedIndicatorColor = Color.Transparent, // 밑줄색 (클릭 O)
                cursorColor = Color.Black, // 클릭 시, 커서색
                focusedTextColor = Color.Black, // 클릭 시, 입력 텍스트 색
            ),
            maxLines = 1,
            placeholder = {
                Text(
                    text = "입력해주세요.",
                    style = KoinTheme.typography.regular15,
                    color = Color(0xFFE1E1E1)
                )
            },
            trailingIcon = {
                IconButton(onClick = onClickSearchIcon) {
                    StableIcon(
                        drawableResId = R.drawable.ic_search,
                        tint = Color(0xFFB5C1Cd),
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(
                    minHeight = 52.dp
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFFD2DAE2),
                    shape = RoundedCornerShape(4.dp)
                ),
        )
    }
}

@Preview(showBackground = true, heightDp = 100)
@Composable
private fun TimetableSearchBoxPreview() {
    var text by remember { mutableStateOf("") }
    KoinTheme {
        TimetableSearchBox(
            searchText = text,
            onSearchTextChange = { text = it }
        )
    }
}