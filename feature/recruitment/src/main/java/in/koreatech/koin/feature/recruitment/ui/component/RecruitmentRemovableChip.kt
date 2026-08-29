package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun RecruitmentRemovableChip(
    text: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(RebrandKoinTheme.colors.neutral100, RoundedCornerShape(20.dp))
            .padding(start = 12.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = RebrandKoinTheme.typography.medium13,
            color = RebrandKoinTheme.colors.neutral700
        )
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "삭제",
            tint = RebrandKoinTheme.colors.neutral500,
            modifier = Modifier
                .size(16.dp)
                .noRippleClickable { onRemove() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecruitmentRemovableChipPreview() {
    RebrandKoinTheme {
        RecruitmentRemovableChip(text = "Kotlin/Java", onRemove = {})
    }
}
