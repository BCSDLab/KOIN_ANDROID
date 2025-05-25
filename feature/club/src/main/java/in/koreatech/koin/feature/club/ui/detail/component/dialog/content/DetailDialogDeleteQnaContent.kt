package `in`.koreatech.koin.feature.club.ui.detail.component.dialog.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R

@Composable
fun DetailDialogDeleteQnaContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.detail_dialog_qna_delete_subtitle),
            style = KoinTheme.typography.regular12,
            color = KoinTheme.colors.neutral800
        )
        Text(
            text = stringResource(R.string.detail_dialog_qna_delete_description),
            style = KoinTheme.typography.regular12,
            color = KoinTheme.colors.neutral800
        )
    }
}

@Preview
@Composable
fun DetailDialogDeleteQnaContentPreView() {
    DetailDialogDeleteQnaContent(
        modifier = Modifier
            .background(
                color = KoinTheme.colors.neutral0
            )
    )
}