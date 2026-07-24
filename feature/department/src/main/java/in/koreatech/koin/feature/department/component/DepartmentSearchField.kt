package `in`.koreatech.koin.feature.department.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.department.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun DepartmentSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSearch: () -> Unit = {},
    placeholder: String = stringResource(R.string.department_search_placeholder)
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = RebrandKoinTheme.colors.neutral0,
                shape = RoundedCornerShape(40.dp)
            )
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            BasicTextField(
                modifier = Modifier.fillMaxWidth(),
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = RebrandKoinTheme.typography.regular12.copy(
                    color = RebrandKoinTheme.colors.neutral800
                ),
                cursorBrush = SolidColor(RebrandKoinTheme.colors.neutral800),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        onSearch()
                    }
                )
            )

            if (query.isEmpty()) {
                Text(
                    text = placeholder,
                    style = RebrandKoinTheme.typography.regular12,
                    color = RebrandKoinTheme.colors.neutral500
                )
            }
        }

        Icon(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(20.dp)
                .noRippleClickable {
                    keyboardController?.hide()
                    onSearch()
                },
            imageVector = ImageVector.vectorResource(R.drawable.ic_department_search),
            contentDescription = stringResource(R.string.department_search_content_description),
            tint = Color.Unspecified
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DepartmentSearchFieldPreview() {
    RebrandKoinTheme {
        DepartmentSearchField(
            modifier = Modifier.padding(16.dp),
            query = "",
            onQueryChange = {}
        )
    }
}