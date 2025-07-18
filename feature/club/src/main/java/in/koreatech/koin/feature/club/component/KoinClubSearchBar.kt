package `in`.koreatech.koin.feature.club.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R

@Composable
fun KoinClubSearchBar(
    value: String,
    modifier: Modifier = Modifier,
    hint: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit = {}
) {
    CompositionLocalProvider(
        LocalTextStyle provides KoinTheme.typography.regular14.copy(
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.Both
            ),
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            )
        )
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = modifier
                .clip(KoinTheme.shapes.extraSmall)
                .background(KoinTheme.colors.neutral100, KoinTheme.shapes.extraSmall),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        propagateMinConstraints = true,
                        contentAlignment = Alignment.CenterStart
                    ) {
                        innerTextField()

                        if (value.isEmpty()) {
                            BasicText(
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
                            .padding(4.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_club_search),
                        contentDescription = null
                    )
                }
            }
        )
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
