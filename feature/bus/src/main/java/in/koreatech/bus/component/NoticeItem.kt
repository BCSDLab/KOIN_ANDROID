package `in`.koreatech.bus.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.bus.busNoticeUiStateMock
import `in`.koreatech.bus.viewstate.BusNoticeViewState
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.util.getMeasuredKoreanHeightDp

@Composable
internal fun NoticeItem(
    notice: BusNoticeViewState,
    onNoticeClick: (BusNoticeViewState) -> Unit,
    onCloseIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    noticeMaxLines: Int = 1,
    textStyle: TextStyle = KoinTheme.typography.medium14
) {
    val textHeightDp = textStyle.getMeasuredKoreanHeightDp()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable { onNoticeClick(notice) }
            .background(
                color = KoinTheme.colors.info100,
                shape = RoundedCornerShape(8.dp)
            ).padding(16.dp),
        verticalAlignment = if (noticeMaxLines == 1) Alignment.CenterVertically else Alignment.Top
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = notice.title,
            style = textStyle,
            color = KoinTheme.colors.primary500,
            maxLines = noticeMaxLines,
            overflow = TextOverflow.Ellipsis
        )
        Icon(
            modifier = Modifier.padding(start = 4.dp).padding(vertical = (textHeightDp - 16.dp) / 2).size(16.dp).noRippleClickable {
                onCloseIconClick()
            },
            imageVector = Icons.Rounded.Close,
            contentDescription = notice.title,
            tint = KoinTheme.colors.neutral400
        )
    }
}

@Composable
@Preview
private fun NoticeItemPreview() {
    KoinTheme {
        NoticeItem(
            notice = busNoticeUiStateMock.notice,
            onCloseIconClick = {},
            onNoticeClick = {}
        )
    }
}

@Composable
@Preview
private fun NoticeItem2Preview() {
    KoinTheme {
        NoticeItem(
            notice = busNoticeUiStateMock.notice,
            onCloseIconClick = {},
            onNoticeClick = {},
            noticeMaxLines = 2
        )
    }
}