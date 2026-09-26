package `in`.koreatech.koin.feature.timetable.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.timetable.R

@Composable
fun TimetableBottomSheetHeader(
    modifier: Modifier = Modifier,
    onSheetDismiss: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.timetable_bottom_sheet_extra_lecture),
            style = RebrandKoinTheme.typography.bold18.copy(color = RebrandKoinTheme.colors.primary500, fontWeight = FontWeight.SemiBold)
        )
        Icon(
            modifier = Modifier
                .size(24.dp)
                .noRippleClickable(onClick = onSheetDismiss),
            imageVector = ImageVector.vectorResource(R.drawable.ic_close_round),
            contentDescription = null,
            tint = RebrandKoinTheme.colors.neutral800
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TimetableBottomSheetHeaderPreview() {
    TimetableBottomSheetHeader()
}
