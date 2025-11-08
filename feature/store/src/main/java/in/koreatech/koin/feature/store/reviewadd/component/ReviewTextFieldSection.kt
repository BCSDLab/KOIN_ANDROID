package `in`.koreatech.koin.feature.store.reviewadd.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.review.component.MenuTagChipGroup

@Composable
fun ReviewTextFieldSection(
    modifier: Modifier = Modifier,
    content: String = "",
    menuTag: String = "",
    menuTags: List<String> = emptyList(),
    onAddMenuTag: () -> Unit = { },
    onRemoveMenuTag: (Int) -> Unit = { },
    onContentChange: (String) -> Unit = { },
    onMenuTagChange: (String) -> Unit = { }
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicText(
                text = "내용",
                style = RebrandKoinTheme.typography.medium16
            )

            BasicText(
                text = "${content.length}/500",
                style = RebrandKoinTheme.typography.regular12.copy(color = RebrandKoinTheme.colors.neutral500)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        BasicTextField(
            value = content,
            onValueChange = { newValue ->
                if (newValue.length <= 500) {
                    onContentChange(newValue)
                }
            },
            textStyle = RebrandKoinTheme.typography.regular14,
            modifier = Modifier
                .fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = RebrandKoinTheme.colors.neutral300,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .background(
                            color = RebrandKoinTheme.colors.neutral0,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (content.isEmpty()) {
                        BasicText(
                            text = "리뷰를 작성해주세요.",
                            style = RebrandKoinTheme.typography.regular14.copy(color = RebrandKoinTheme.colors.neutral400)
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        BasicText(
            text = "주문 메뉴",
            style = RebrandKoinTheme.typography.medium16
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicText(
                text = "입력한 메뉴가 태그로 추가돼요.",
                style = RebrandKoinTheme.typography.regular12.copy(color = RebrandKoinTheme.colors.neutral500)
            )

            BasicText(
                text = "${menuTags.size}/5",
                style = RebrandKoinTheme.typography.regular12.copy(color = RebrandKoinTheme.colors.neutral500)
            )
        }

        if (menuTags.isNotEmpty()) {
            MenuTagChipGroup(
                menuTags = menuTags,
                modifier = Modifier.padding(vertical = 12.dp),
                isIconVisibility = true,
                onClick = onRemoveMenuTag
            )
        }

        if (menuTags.size < 5) {
            BasicTextField(
                value = menuTag,
                onValueChange = { newValue ->
                    if (newValue.length <= 500) {
                        onMenuTagChange(newValue)
                    }
                },
                textStyle = RebrandKoinTheme.typography.regular14,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onAddMenuTag()
                    }
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = RebrandKoinTheme.colors.neutral300,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .background(
                                color = RebrandKoinTheme.colors.neutral0,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (menuTag.isEmpty()) {
                            BasicText(
                                text = "메뉴명을 입력해주세요.",
                                style = RebrandKoinTheme.typography.regular14.copy(color = RebrandKoinTheme.colors.neutral400)
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewTextFieldSectionPreview() {
    val menuTags = listOf("1인 족발", "계란찜")
    ReviewTextFieldSection(
        content = "테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트테스트",
        menuTag = "테스트",
        menuTags = menuTags
    )
}
