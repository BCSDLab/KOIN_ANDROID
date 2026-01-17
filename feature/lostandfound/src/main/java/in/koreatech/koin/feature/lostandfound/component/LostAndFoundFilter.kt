package `in`.koreatech.koin.feature.lostandfound.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.lostandfound.R

@Composable
fun LostAndFoundContainer(
    modifier: Modifier = Modifier,
    onFilterClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LostAndFoundChip(onClick = onFilterClick)
    }
}

@Composable
fun LostAndFoundChip(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .height(34.dp),
        shape = RoundedCornerShape(24.dp),
        color = KoinTheme.colors.info200,
        contentColor = KoinTheme.colors.neutral600
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)

        ) {
            Text(
                text = stringResource(R.string.filter_container),
                style = KoinTheme.typography.bold14
            )
            Icon(
                painter = painterResource(R.drawable.ic_list_filter),
                contentDescription = "",
                modifier = Modifier.size(16.dp),
                tint = Color.Unspecified
            )
        }
    }
}

@Preview(name = "Chip Only", showBackground = true)
@Composable
private fun LostAndFoundChipPreview() {
    KoinTheme {
        LostAndFoundChip(onClick = {})
    }
}