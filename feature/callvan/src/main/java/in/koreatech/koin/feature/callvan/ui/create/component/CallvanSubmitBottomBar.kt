package `in`.koreatech.koin.feature.callvan.ui.create.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R

@Composable
fun CallvanSubmitBottomBar(
    isFormComplete: Boolean,
    isSubmitting: Boolean,
    modifier: Modifier = Modifier,
    onSubmit: () -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(color = RebrandKoinTheme.colors.neutral100)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(RebrandKoinTheme.colors.neutral0)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.callvan_create_submit_notice),
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral500
            )
            FilledButton(
                text = stringResource(R.string.callvan_create_submit),
                onClick = onSubmit,
                enabled = isFormComplete && !isSubmitting,
                modifier = Modifier.fillMaxWidth(),
                shape = RebrandKoinTheme.shapes.small,
                textStyle = RebrandKoinTheme.typography.bold16,
                contentPadding = PaddingValues(vertical = 14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RebrandKoinTheme.colors.primary500,
                    disabledContainerColor = RebrandKoinTheme.colors.neutral400,
                    contentColor = RebrandKoinTheme.colors.neutral0,
                    disabledContentColor = RebrandKoinTheme.colors.neutral0
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanSubmitBottomBarEnabledPreview() {
    CallvanSubmitBottomBar(
        isFormComplete = true,
        isSubmitting = false,
        onSubmit = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanSubmitBottomBarDisabledPreview() {
    CallvanSubmitBottomBar(
        isFormComplete = false,
        isSubmitting = false,
        onSubmit = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanSubmitBottomBarLoadingPreview() {
    CallvanSubmitBottomBar(
        isFormComplete = true,
        isSubmitting = true,
        onSubmit = {}
    )
}
