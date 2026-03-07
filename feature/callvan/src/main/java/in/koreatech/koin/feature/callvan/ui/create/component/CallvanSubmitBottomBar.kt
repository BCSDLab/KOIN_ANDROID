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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.callvan.R

@Composable
fun CallvanSubmitBottomBar(
    isFormComplete: Boolean,
    isSubmitting: Boolean,
    onSubmit: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(color = KoinTheme.colors.neutral100)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(KoinTheme.colors.neutral0)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.callvan_create_submit_notice),
                style = KoinTheme.typography.regular12,
                color = KoinTheme.colors.neutral500
            )
            FilledButton(
                text = stringResource(R.string.callvan_create_submit),
                onClick = onSubmit,
                enabled = isFormComplete && !isSubmitting,
                modifier = Modifier.fillMaxWidth(),
                shape = KoinTheme.shapes.small,
                textStyle = KoinTheme.typography.bold16,
                contentPadding = PaddingValues(vertical = 14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RebrandKoinTheme.colors.primary500,
                    disabledContainerColor = KoinTheme.colors.neutral400,
                    contentColor = Color.White,
                    disabledContentColor = Color.White
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
