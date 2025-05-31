package `in`.koreatech.koin.feature.club.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.component.button.FilledButtonColors
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KoinClubMessageDialog(
    modifier: Modifier = Modifier,
    title: String = "",
    content: @Composable () -> Unit = {},
    onPositive: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    titleStyle: TextStyle = KoinTheme.typography.medium18,
    buttonText: String = stringResource(id = R.string.club_dialog_ok),
    buttonColors: FilledButtonColors = FilledButtonColors.Primary
) {
    BasicAlertDialog(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = KoinTheme.colors.neutral0,
                shape = KoinTheme.shapes.large
            )
            .padding(horizontal = 12.dp, vertical = 24.dp),
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = titleStyle
            )
            Spacer(modifier = Modifier.height(16.dp))

            content()

            Spacer(modifier = Modifier.height(16.dp))

            FilledButton(
                modifier = Modifier.fillMaxWidth(),
                text = buttonText,
                onClick = onPositive,
                colors = buttonColors
            )
        }
    }
}

@Preview
@Composable
fun KoinClubMessageDialogPreview() {
    KoinTheme {
        KoinClubMessageDialog(
            title = "제목",
            onPositive = {},
            onDismissRequest = {}
        )
    }
}
