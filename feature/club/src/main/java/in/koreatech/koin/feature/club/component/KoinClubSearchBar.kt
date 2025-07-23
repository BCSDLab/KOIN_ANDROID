package `in`.koreatech.koin.feature.club.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KoinClubSearchBar(
    value: String,
    modifier: Modifier = Modifier,
    hint: String = "",
    expanded: Boolean = false,
    suggestions: List<String> = emptyList(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit = {},
    onSearch: (String) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    CompositionLocalProvider(
        LocalTextStyle provides KoinTheme.typography.regular14
    ) {
        val dropdownCorner: Dp by animateDpAsState(
            targetValue = if (expanded) 0.dp else 4.dp
        )

        ExposedDropdownMenuBox(
            modifier = modifier,
            expanded = expanded,
            onExpandedChange = {}
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = LocalTextStyle.current,
                modifier = Modifier
                    .clip(
                        KoinTheme.shapes.extraSmall.copy(
                            bottomStart = CornerSize(dropdownCorner),
                            bottomEnd = CornerSize(dropdownCorner)
                        )
                    )
                    .background(
                        color = KoinTheme.colors.neutral100,
                        shape = KoinTheme.shapes.extraSmall.copy(
                            bottomStart = CornerSize(dropdownCorner),
                            bottomEnd = CornerSize(dropdownCorner)
                        )
                    )
                    .menuAnchor(MenuAnchorType.PrimaryEditable),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .padding(start = 4.dp, end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(
                            modifier = Modifier
                                .animateContentSize()
                                .width(if (expanded) 0.dp else 12.dp)
                        )

                        Image(
                            modifier = Modifier
                                .animateContentSize()
                                .width(if (expanded) 20.dp else 0.dp)
                                .height(20.dp)
                                .noRippleClickable {
                                    onDismiss()
                                    focusManager.clearFocus()
                                },
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_club_search_back),
                            contentDescription = null
                        )

                        Box(
                            modifier = Modifier.weight(1f),
                            propagateMinConstraints = true,
                            contentAlignment = Alignment.CenterStart
                        ) {
                            innerTextField()

                            if (value.isEmpty()) {
                                Text(
                                    text = hint,
                                    maxLines = 1,
                                    style = LocalTextStyle.current.copy(
                                        color = KoinTheme.colors.neutral600
                                    )
                                )
                            }
                        }

                        Image(
                            modifier = Modifier
                                .size(24.dp)
                                .noRippleClickable {
                                    onSearch(value)
                                    focusManager.clearFocus()
                                }
                                .padding(4.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_club_search),
                            contentDescription = null
                        )
                    }
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {},
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                shape = KoinTheme.shapes.extraSmall.copy(
                    topStart = CornerSize(dropdownCorner),
                    topEnd = CornerSize(dropdownCorner)
                ),
                containerColor = KoinTheme.colors.neutral100
            ) {
                suggestions.forEach {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .clickable {
                                onSearch(it)
                                focusManager.clearFocus()
                            }
                    ) {
                        BasicText(
                            text = it,
                            style = LocalTextStyle.current.copy(
                                color = KoinTheme.colors.neutral500
                            )
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Image(
                            modifier = Modifier
                                .noRippleClickable {
                                    onValueChange(it)
                                }
                                .padding(1.dp)
                                .size(16.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_club_search_keyword_select),
                            contentDescription = null
                        )
                    }
                }

                BackHandler { onDismiss() }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KoinClubSearchBarPreview() {
    KoinClubSearchBar(
        modifier = Modifier.fillMaxWidth(),
        value = "",
        hint = "검색"
    )
}

@Preview(showBackground = true)
@Composable
private fun KoinClubSearchBarPreviewTwo() {
    KoinClubSearchBar(
        modifier = Modifier.fillMaxWidth(),
        value = "asdf"
    )
}
