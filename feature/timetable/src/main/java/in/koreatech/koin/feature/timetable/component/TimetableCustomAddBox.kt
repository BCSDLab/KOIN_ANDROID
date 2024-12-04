package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.component.icon.StableIcon
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.R

@Composable
fun TimetableCustomAddBox(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .height(35.dp)
            .fillMaxWidth()
            .background(Color.White)
            .border(
                width = 1.dp,
                color = KoinTheme.colors.neutral300,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(top = 5.dp, bottom = 5.dp, start = 13.dp, end = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.timetable_add_time_and_place),
            style = KoinTheme.typography.bold12,
            color = KoinTheme.colors.primary500
        )
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(24.dp)
        ) {
            StableIcon(
                drawableResId = R.drawable.ic_add,
                tint = KoinTheme.colors.primary500
            )
        }
    }
}

@Preview
@Composable
fun TimetableCustomAddBoxPreview() {
    TimetableCustomAddBox()
}