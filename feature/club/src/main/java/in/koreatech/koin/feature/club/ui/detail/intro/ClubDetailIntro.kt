package `in`.koreatech.koin.feature.club.ui.detail.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R

@Composable
fun ClubDetailIntro(
    modifier: Modifier = Modifier,
    onFixIntroClick: () -> Unit = {},
    isManager: Boolean = false,
    userId: Int? = null
) {
    Column(
        modifier = modifier
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp
            ),
        horizontalAlignment = Alignment.End
    ) {
        userId?.let {
            if (isManager) {
                FilledButton(
                    text = stringResource(R.string.detail_edit_detail_intro_button),
                    onClick = onFixIntroClick,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
        }
    }
}

@Preview
@Composable
fun ClubDetailIntroPreview() {
    ClubDetailIntro(
        modifier = Modifier
            .background(
                color = KoinTheme.colors.neutral0
            ),
        onFixIntroClick = {},
        isManager = true,
        userId = 0
    )
}
