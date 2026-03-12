package `in`.koreatech.koin.feature.callvan.ui.create.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun CallvanSectionHeader(
    label: String,
    hint: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = RebrandKoinTheme.typography.medium16,
            color = RebrandKoinTheme.colors.primary500
        )
        Text(
            text = hint,
            style = RebrandKoinTheme.typography.regular12,
            color = RebrandKoinTheme.colors.neutral500
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CallvanSectionHeaderPreview() {
    RebrandKoinTheme {
        CallvanSectionHeader(
            label = "출발일",
            hint = "출발 날짜를 선택해주세요."
        )
    }
}
