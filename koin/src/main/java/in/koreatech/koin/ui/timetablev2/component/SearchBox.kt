package `in`.koreatech.koin.ui.timetablev2.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchBox(
    searchText: String,
    modifier: Modifier = Modifier,
    onSetting: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
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
                modifier = Modifier.clickable(onClick = onSetting)
            )
            Spacer(modifier = Modifier.width(5.dp))
            TextField(
                value = searchText,
                onValueChange = onSearchTextChanged,
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
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.DarkGray,
                    focusedIndicatorColor = Color.Black,
                    cursorColor = Color.Black,
                    backgroundColor = Color.White
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchBoxPreview() {
    SearchBox(
        searchText = "",
        onSearchTextChanged = {},
        onSetting = {}
    )
}