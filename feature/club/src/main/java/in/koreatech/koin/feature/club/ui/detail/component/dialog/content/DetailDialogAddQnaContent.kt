package `in`.koreatech.koin.feature.club.ui.detail.component.dialog.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.ui.detail.component.textfield.DetailQnaTextField

@Composable
fun DetailDialogAddQnaContent(
    modifier: Modifier = Modifier,
    text: String = "",
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(8.dp)
    ) {
        DetailQnaTextField(
            value = text,
            hint = stringResource(R.string.detail_dialog_qna_content_hint),
            textStyle = KoinTheme.typography.regular12,
            onValueChange = onValueChange,
            isSendIconVisible = false,
            onSendClick = {},
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        )
        Text(
            text = stringResource(R.string.detail_dialog_qna_content_text),
            style = KoinTheme.typography.regular12,
            color = KoinTheme.colors.neutral800
        )
    }
}

@Preview
@Composable
fun DetailDialogAddQnaContentPreView() {
    DetailDialogAddQnaContent(
        modifier = Modifier
            .background(
                color = KoinTheme.colors.neutral0
            ),
        onValueChange = {}
    )
}
