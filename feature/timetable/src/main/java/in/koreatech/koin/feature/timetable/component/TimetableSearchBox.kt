package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.timetable.R

@Composable
fun TimetableSearchBox(
    searchText: String,
    modifier: Modifier = Modifier,
    onSearchTextChange: (String) -> Unit = {},
    onClickSettingIcon: (visible: Boolean) -> Unit = {},
    onClickSearchIcon: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier
                .weight(1f)
                .heightIn(max = 36.dp)
                .border(
                    width = 1.dp,
                    color = RebrandKoinTheme.colors.neutral300,
                    shape = RoundedCornerShape(40.dp)
                )
        ) { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = searchText,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = MutableInteractionSource(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 20.dp),
                placeholder = {
                    Text(
                        text = "검색어를 입력해주세요.",
                        style = RebrandKoinTheme.typography.regular12,
                        color = Color(0xFFE1E1E1)
                    )
                },
                trailingIcon = {
                    IconButton(
                        modifier = Modifier.size(20.dp),
                        onClick = onClickSearchIcon
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_search_timetable_box),
                            contentDescription = null,
                            tint = RebrandKoinTheme.colors.primary500
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White, // 배경색 (클릭 X)
                    focusedContainerColor = Color.White, // 배경색 (클릭 O)
                    unfocusedIndicatorColor = Color.Transparent, // 밑줄색 (클릭 X)
                    focusedIndicatorColor = Color.Transparent, // 밑줄색 (클릭 O)
                    cursorColor = Color.Black, // 클릭 시, 커서색
                    focusedTextColor = Color.Black // 클릭 시, 입력 텍스트 색
                )
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .border(1.dp, RebrandKoinTheme.colors.neutral300, CircleShape)
                .clickable { onClickSettingIcon(true) }
        ) {
            Icon(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .size(20.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_filter),
                contentDescription = null,
                tint = RebrandKoinTheme.colors.primary500
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimetableSearchBoxPreview() {
    var text by remember { mutableStateOf("") }
    RebrandKoinTheme {
        TimetableSearchBox(
            searchText = text,
            onSearchTextChange = { text = it }
        )
    }
}
