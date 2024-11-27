package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.timetable.R

@Composable
fun TimetableDownloadBox(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .background(Color.White)
            .border(
                width = 1.dp,
                color = KoinTheme.colors.neutral300,
                shape = RoundedCornerShape(10.dp)
            )
            .noRippleClickable { onClick() }
            .padding(5.dp),
    ) {
        Text(
            text = "시간표 다운로드",
            style = KoinTheme.typography.regular14,
            color = KoinTheme.colors.neutral800
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_download),
            contentDescription = null,
            modifier = Modifier.size(20.dp).padding(1.dp),
            tint = KoinTheme.colors.neutral800
        )
    }
}

@Preview
@Composable
private fun TimetableDownloadBoxPreview() {
    TimetableDownloadBox()
}