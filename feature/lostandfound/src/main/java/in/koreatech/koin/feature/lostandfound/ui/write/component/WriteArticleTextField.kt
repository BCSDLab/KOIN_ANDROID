package `in`.koreatech.koin.feature.lostandfound.ui.write.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun WriteArticleTextField(
    value: String,
    hint: String,
    modifier: Modifier = Modifier,
    textPaddingValues: PaddingValues = PaddingValues(0.dp),
    onValueChange: (String) -> Unit,
) {
    Box(modifier = modifier) {
        if (value.isEmpty()) {
            Text(
                modifier = Modifier.padding(textPaddingValues),
                text = hint,
                color = KoinTheme.colors.neutral500,
                style = KoinTheme.typography.regular12
            )
        }

        BasicTextField(
            modifier = Modifier
                .padding(textPaddingValues)
                .fillMaxWidth(),
            value = value,
            textStyle = KoinTheme.typography.regular14,
            onValueChange = onValueChange
        )
    }
}
