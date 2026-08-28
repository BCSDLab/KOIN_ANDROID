package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruitmentDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = RebrandKoinTheme.colors.neutral0,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun RecruitmentDialogPreview() {
    RebrandKoinTheme {
        RecruitmentDialog(onDismiss = {}) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Dialog Title",
                    style = RebrandKoinTheme.typography.medium15,
                    color = RebrandKoinTheme.colors.neutral700
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Dialog message goes here.",
                    style = RebrandKoinTheme.typography.regular13,
                    color = RebrandKoinTheme.colors.neutral500
                )
            }
        }
    }
}
