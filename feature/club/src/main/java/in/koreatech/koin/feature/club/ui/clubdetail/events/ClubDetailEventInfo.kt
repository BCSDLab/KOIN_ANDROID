package `in`.koreatech.koin.feature.club.ui.clubdetail.events

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.feature.club.R
import `in`.koreatech.koin.feature.club.component.KoinClubExtraSmallDialogDanger
import `in`.koreatech.koin.feature.club.model.EventStatus
import `in`.koreatech.koin.feature.club.model.ParcelizeClubEvent
import `in`.koreatech.koin.feature.club.model.toStringForm
import java.time.LocalDateTime
import okhttp3.internal.immutableListOf

@Composable
fun ClubDetailEventInfo(
    clubEvent: ParcelizeClubEvent,
    modifier: Modifier = Modifier,
    isManager: Boolean = false,
    onEventModifyClick: () -> Unit = {},
    onEventDeleteClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    BackHandler {
        onBackPressed()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = 16.dp,
                start = 24.dp,
                end = 24.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(30.dp).noRippleClickable { onBackPressed() },
                imageVector = Icons.AutoMirrored.Sharp.KeyboardArrowLeft,
                contentDescription = stringResource(`in`.koreatech.koin.core.designsystem.R.string.navigate_up_content_description)
            )
            if (isManager) {
                Row(
                    horizontalArrangement = Arrangement
                        .spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledButton(
                        text = stringResource(R.string.detail_dialog_event_delete),
                        textStyle = KoinTheme.typography.medium14,
                        onClick = onEventDeleteClick,
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 5.dp),
                        colors = KoinClubExtraSmallDialogDanger.positiveButtonColors()
                    )
                    FilledButton(
                        text = stringResource(R.string.detail_dialog_event_modify),
                        textStyle = KoinTheme.typography.medium14,
                        onClick = onEventModifyClick,
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 5.dp)
                    )
                }
            }
        }
        Column {
            Row(
                modifier = Modifier.wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    modifier = Modifier.weight(1f, fill = false),
                    text = clubEvent.name,
                    style = KoinTheme.typography.bold20,
                    color = KoinTheme.colors.primary600,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val stateColor = when (clubEvent.status) {
                    EventStatus.SOON -> KoinTheme.colors.primary400
                    EventStatus.ONGOING -> KoinTheme.colors.primary300
                    EventStatus.UPCOMING -> KoinTheme.colors.primary500
                    EventStatus.ENDED -> KoinTheme.colors.primary500
                    else -> KoinTheme.colors.primary400
                }
                Row(
                    modifier = Modifier
                        .background(color = stateColor, shape = KoinTheme.shapes.extraLarge)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(clubEvent.status.strRes),
                        style = KoinTheme.typography.regular10.copy(fontSize = 11.sp),
                        color = KoinTheme.colors.neutral0,
                        maxLines = 1
                    )
                    if (clubEvent.status == EventStatus.UPCOMING) {
                        Spacer(Modifier.width(8.dp))
                        Image(
                            modifier = Modifier.size(15.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.ic_club_event_bell),
                            contentDescription = null
                        )
                    }
                }
            }
            Text(
                text = stringResource(
                    R.string.club_event_period,
                    clubEvent.startDateTime.toStringForm(),
                    clubEvent.endDateTime.toStringForm()
                ),
                style = KoinTheme.typography.medium15,
                color = KoinTheme.colors.neutral600,
                maxLines = 1
            )
        }
        if (
            clubEvent.status == EventStatus.UPCOMING ||
            clubEvent.status == EventStatus.SOON
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.detail_dialog_event_notification)
                )
                Image(
                    painter = if (clubEvent.isSubscribed) {
                        painterResource(R.drawable.icon_notification_true)
                    } else {
                        painterResource(R.drawable.icon_notification_false)
                    },
                    contentDescription = "Notification Icon",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 4.dp)
                        .clickable {
                            onNotificationClick()
                        }
                )
            }
        }
        if (clubEvent.imageUrls.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val pagerState = rememberPagerState(pageCount = { clubEvent.imageUrls.size })
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.wrapContentSize()
                ) { page ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(5f / 4f)
                            .clip(KoinTheme.shapes.extraLarge)
                            .border(1.dp, Color.Unspecified, KoinTheme.shapes.extraLarge)
                            .background(KoinTheme.colors.neutral200),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box {
                            SubcomposeAsyncImage(
                                model = clubEvent.imageUrls[page],
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                loading = {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            )
                        }
                    }
                }
                Row(
                    modifier =
                    Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    clubEvent.imageUrls.indices.forEach { index ->
                        Image(
                            modifier =
                            if (index == pagerState.currentPage) {
                                Modifier.size(6.dp)
                            } else {
                                Modifier.size(
                                    4.dp
                                )
                            },
                            painter =
                            if (index == pagerState.currentPage) {
                                painterResource(R.drawable.ic_image_page_indicator_filled)
                            } else {
                                painterResource(id = R.drawable.ic_image_page_indicator_not_filled)
                            },
                            contentDescription = null
                        )
                    }
                }
            }
        }
        Text(
            text = stringResource(R.string.detail_event_info_introduce, clubEvent.introduce),
            style = KoinTheme.typography.regular15
        )
        if (clubEvent.content.isNotBlank()) {
            Text(
                text = stringResource(R.string.detail_event_info_content, clubEvent.content),
                style = KoinTheme.typography.regular15
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClubDetailEventInfoManagerPreview() {
    ClubDetailEventInfo(
        ParcelizeClubEvent(
            id = 1,
            name = "행사 명",
            imageUrls = immutableListOf(),
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusDays(1),
            introduce = "동아리 소개 문구",
            content = "동아리 상세 소개",
            status = EventStatus.SOON,
            isSubscribed = false
        ),
        isManager = true
    )
}
