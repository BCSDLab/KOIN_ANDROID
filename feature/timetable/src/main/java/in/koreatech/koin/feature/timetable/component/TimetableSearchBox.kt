package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.sp

@Composable
fun TimetableSearchBox(
    modifier: Modifier = Modifier,
    searchText: String,
    onSettingClick: () -> Unit = {},
    onSearchTextChange: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .padding(end = 24.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                modifier = Modifier.clickable(onClick = onSettingClick)
            )
            Spacer(modifier = Modifier.width(5.dp))
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
                        fontSize = 15.sp,
                        color = Color.LightGray
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Blue,
                        shape = RoundedCornerShape(10.dp)
                    ),

                )
        }
    }
}

@Preview(showBackground = true, heightDp = 100)
@Composable
private fun TimetableSearchBoxPreview() {
    var text by remember { mutableStateOf("") }
    TimetableSearchBox(
        searchText = text,
        onSearchTextChange = { text = it }
    )
}