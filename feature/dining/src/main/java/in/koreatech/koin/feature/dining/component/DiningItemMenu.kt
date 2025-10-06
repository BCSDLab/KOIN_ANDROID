package `in`.koreatech.koin.feature.dining.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun DiningItemMenu(
    menu: List<String>,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(12.dp)
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = menu.filterIndexed { index, _ -> index % 2 == 0 }.joinToString(separator = "\n"),
            style = KoinTheme.typography.regular14
        )
        Text(
            modifier = Modifier.weight(1f),
            text = menu.filterIndexed { index, _ -> index % 2 == 1 }.joinToString(separator = "\n"),
            style = KoinTheme.typography.regular14
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DiningItemMenuPreview() {
    DiningItemMenu(
        menu = listOf("밥", "국", "김치")
    )
}
