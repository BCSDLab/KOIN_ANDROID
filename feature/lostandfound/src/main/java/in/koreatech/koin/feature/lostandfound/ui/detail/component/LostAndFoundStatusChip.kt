package `in`.koreatech.koin.feature.lostandfound.ui.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.lostandfound.R

@Composable
fun LostAndFoundStatusChip(
    isFound: Boolean,
    modifier: Modifier = Modifier
) {
    val chipState = if (isFound) {
        ChipState(
            text = stringResource(id = R.string.lost_and_found_found),
            backgroundColor = RebrandKoinTheme.colors.neutral100,
            textColor = RebrandKoinTheme.colors.neutral400,
            horizontalPadding = 9.5.dp
        )
    } else {
        ChipState(
            text = stringResource(id = R.string.lost_and_found_finding),
            backgroundColor = Color(0xFFFFA928),
            textColor = RebrandKoinTheme.colors.neutral0,
            horizontalPadding = 4.dp
        )
    }

    Box(
        modifier = modifier
            .clip(RebrandKoinTheme.shapes.extraSmall)
            .background(chipState.backgroundColor)
    ) {
        BasicText(
            text = chipState.text,
            modifier = Modifier.padding(horizontal = chipState.horizontalPadding, vertical = 2.dp),
            style = RebrandKoinTheme.typography.medium12.copy(color = chipState.textColor)
        )
    }
}

private data class ChipState(
    val text: String,
    val backgroundColor: Color,
    val textColor: Color,
    val horizontalPadding: Dp
)

@Preview(showBackground = false)
@Composable
private fun LostAndFoundStatusChipPreview() {
    LostAndFoundStatusChip(
        false
    )
}

@Preview(showBackground = false)
@Composable
private fun LostAndFoundStatusFoundChipPreview() {
    LostAndFoundStatusChip(
        true
    )
}
