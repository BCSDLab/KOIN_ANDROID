package `in`.koreatech.koin.feature.lostandfound.ui.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R

@Composable
fun LostAndFoundEntry(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.lost_and_found),
            style = KoinTheme.typography.medium18,
            color = KoinTheme.colors.primary500
        )
        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = KoinTheme.shapes.small)
                .background(color = KoinTheme.colors.neutral100)
                .clickable(onClick = onClick)
                .padding(
                    vertical = 10.dp,
                    horizontal = 10.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.lost_and_found_entry),
                style = KoinTheme.typography.medium14
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LostAndFoundEntryPreview() {
    LostAndFoundEntry()
}