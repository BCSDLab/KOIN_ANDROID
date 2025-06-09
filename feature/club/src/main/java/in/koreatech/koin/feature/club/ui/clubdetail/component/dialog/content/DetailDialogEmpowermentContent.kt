package `in`.koreatech.koin.feature.club.ui.clubdetail.component.dialog.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.ui.clubdetail.component.textfield.DetailQnaTextField

@Composable
fun DetailDialogEmpowermentContent(
    clubName: String,
    managerId: String,
    modifier: Modifier = Modifier,
    text: String = "",
    isError: Boolean = false,
    errorMessage: String = "",
    onValueChange: (String) -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.detail_dialog_empowerment_club_name),
                style = KoinTheme.typography.medium18,
                color = KoinTheme.colors.neutral800
            )
            Text(
                text = clubName,
                style = KoinTheme.typography.medium18,
                color = KoinTheme.colors.neutral800
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.detail_dialog_empowerment_old_id),
                style = KoinTheme.typography.medium16,
                color = KoinTheme.colors.neutral800
            )
            Text(
                text = managerId,
                style = KoinTheme.typography.medium16,
                color = KoinTheme.colors.neutral800
            )
        }
        Row(
            verticalAlignment = if (isError) Alignment.Top else Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.detail_dialog_empowerment_new_id),
                style = KoinTheme.typography.medium16,
                color = KoinTheme.colors.neutral800
            )
        }
        DetailQnaTextField(
            value = text,
            hint = stringResource(R.string.detail_dialog_empowerment_hint),
            textStyle = KoinTheme.typography.regular14,
            hintColor = KoinTheme.colors.neutral700,
            onValueChange = onValueChange,
            isSendIconVisible = false,
            isError = isError,
            errorMessage = errorMessage,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 7.dp)
        )
    }
}

@Preview
@Composable
fun DetailDialogEmpowermentContentPreView() {
    DetailDialogEmpowermentContent(
        clubName = "bcsd",
        managerId = "6488",
        modifier = Modifier
            .background(
                color = KoinTheme.colors.neutral0
            )
    )
}

@Preview
@Composable
fun DetailDialogEmpowermentContentErrorPreView() {
    DetailDialogEmpowermentContent(
        clubName = "bcsd",
        managerId = "6488",
        modifier = Modifier
            .background(
                color = KoinTheme.colors.neutral0
            ),
        isError = true,
        errorMessage = "필수 입력란입니다."
    )
}
