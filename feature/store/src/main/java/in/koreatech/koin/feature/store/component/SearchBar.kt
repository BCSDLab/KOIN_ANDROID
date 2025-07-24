package `in`.koreatech.koin.feature.store.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = stringResource(R.string.store_search_hint)
) {
    val focusRequester = remember { FocusRequester() }

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RebrandKoinTheme.shapes.medium,
                ambientColor = RebrandKoinTheme.colors.neutral400,
                spotColor = RebrandKoinTheme.colors.neutral500
            )
            .clip(RebrandKoinTheme.shapes.medium)
            .background(RebrandKoinTheme.colors.neutral0, shape = RebrandKoinTheme.shapes.medium)
            .focusRequester(focusRequester),
        textStyle = RebrandKoinTheme.typography.regular14,
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "검색",
                    tint = RebrandKoinTheme.colors.neutral500
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box {
                    innerTextField()
                    if (query.isEmpty()) {
                        Text(
                            text = hint,
                            style = RebrandKoinTheme.typography.regular14.copy(color = RebrandKoinTheme.colors.neutral400)
                        )
                    }
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview
@Composable
private fun SearchBarPreview() {
    RebrandKoinTheme {
        SearchBar(
            query = "",
            onQueryChange = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
