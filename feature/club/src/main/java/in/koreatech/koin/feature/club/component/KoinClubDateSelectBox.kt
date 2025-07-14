package `in`.koreatech.koin.feature.club.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun KoinClubDateSelectBox(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = KoinTheme.typography.medium16,
    textAlign: TextAlign = TextAlign.Center,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                spotColor = KoinTheme.colors.neutral800.copy(alpha = 0.04f),
                ambientColor = KoinTheme.colors.neutral800.copy(alpha = 0.04f)
            )
            .shadow(
                elevation = 1.dp,
                spotColor = KoinTheme.colors.neutral800.copy(alpha = 0.02f),
                ambientColor = KoinTheme.colors.neutral800.copy(alpha = 0.02f)
            )
            .border(
                width = 1.dp,
                color = KoinTheme.colors.neutral300,
                shape = KoinTheme.shapes.small
            )
            .background(
                color = KoinTheme.colors.neutral50,
                shape = KoinTheme.shapes.small
            )
            .clip(shape = KoinTheme.shapes.small)
            .clickable { onClick() }
            .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            style = textStyle,
            textAlign = textAlign
        )
    }
}

@Preview
@Composable
private fun KoinClubDateSelectBoxPreview() {
    KoinClubDateSelectBox("2025년\n12월 30일 (화)")
}
