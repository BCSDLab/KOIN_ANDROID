package `in`.koreatech.business.feature.store.storedetail.event

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailState
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailViewModel
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.business.ui.theme.Gray1
import `in`.koreatech.business.ui.theme.Gray2
import `in`.koreatech.business.ui.theme.Gray6
import `in`.koreatech.koin.domain.model.store.ShopEvent
import `in`.koreatech.koin.domain.util.StoreUtil


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventItem(
    enabledScroll: Boolean,
    scrollState: ScrollState,
    viewModel: MyStoreDetailViewModel,
    state: MyStoreDetailState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(enabled = enabledScroll, state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        state.storeEvent?.forEachIndexed { index, item ->
            val eventOpenCloseTime = remember(item.startDate, item.endDate) {
                StoreUtil.generateOpenCloseTimeString(item.startDate, item.endDate)
            }
            val pagerState =
                rememberPagerState { state.storeEvent[index].thumbnailImages?.size ?: 1 }
            if (state.isEventExpanded[index]) {
                EventExpandedItem(state.storeEvent[index],
                    eventOpenCloseTime = eventOpenCloseTime,
                    pagerState = pagerState,
                    onCollapse = { viewModel.toggleEventItem(index) })
            } else {
                EventFoldedItem(
                    item = state.storeEvent[index],
                    eventOpenCloseTime = eventOpenCloseTime,
                    onClicked = {
                        if (!state.isEditMode) viewModel.toggleEventItem(index) else viewModel.onChangeEventSelected(
                            item.eventId
                        )
                    },
                    viewModel = viewModel,
                    state = state
                )
            }
            Divider(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .height(1.dp),
                color = ColorTextField,
            )
        }
    }
}

@Composable
fun EventFoldedItem(
    item: ShopEvent,
    eventOpenCloseTime: String,
    onClicked: () -> Unit = {},
    viewModel: MyStoreDetailViewModel,
    state: MyStoreDetailState,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = { onClicked() })
            .fillMaxWidth()
            .height(104.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        item.thumbnailImages?.getOrNull(0)?.let {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(4.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 4.dp, start = 10.dp)
                        .align(Alignment.Center)
                        .width(72.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    contentScale = ContentScale.FillBounds,
                    painter = if (item.thumbnailImages?.size == 0) painterResource(id = R.drawable.no_image) else rememberAsyncImagePainter(
                        model = item.thumbnailImages?.getOrNull(0)
                    ),
                    contentDescription = stringResource(R.string.event_default_image),
                )
                if (state.isEditMode) {
                    viewModel.initEventItem()
                    Image(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .height(24.dp)
                            .width(24.dp)
                            .clickable {
                                viewModel.onChangeEventSelected(item.eventId)
                            },
                        painter = if (state.isAllEventSelected || state.isSelectedEvent.contains(
                                item.eventId
                            )
                        ) painterResource(
                            id = R.drawable.ic_check_selected
                        ) else painterResource(
                            id = R.drawable.ic_check
                        ),
                        contentDescription = stringResource(R.string.check),
                    )
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(3.5f),
                    text = item.title,
                    fontWeight = FontWeight(500),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.view_all),
                        fontWeight = FontWeight(500),
                        fontSize = 12.sp,
                        color = Gray6
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        contentDescription = stringResource(R.string.view_all)
                    )
                }
            }
            Text(
                text = item.content, maxLines = 2, fontSize = 12.sp, color = Gray6
            )
            Text(
                text = eventOpenCloseTime, fontSize = 10.sp, color = Gray6
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventExpandedItem(
    item: ShopEvent,
    eventOpenCloseTime: String,
    pagerState: PagerState,
    onCollapse: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        HorizontalPager(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            state = pagerState,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(0.7f)
                    .background(Gray2),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = if (item.thumbnailImages?.size == 0) painterResource(id = R.drawable.no_event_image) else rememberAsyncImagePainter(
                        model = item.thumbnailImages?.getOrNull(it)
                    ),
                    contentDescription = stringResource(R.string.event_default_image),
                    contentScale = ContentScale.Inside
                )
            }

        }
        Column(
            modifier = Modifier.padding(vertical = 5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(8f), text = item.title, fontWeight = FontWeight(500)
                )
                Row(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.fold),
                        fontWeight = FontWeight(500),
                        fontSize = 12.sp,
                        color = Gray6
                    )
                    Image(
                        modifier = Modifier
                            .rotate(180f)
                            .clickable(onClick = { onCollapse() }),
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        contentDescription = stringResource(R.string.fold),
                    )
                }
            }
            Text(text = item.content, fontSize = 12.sp, color = Gray1)
            Text(
                text = eventOpenCloseTime, fontSize = 10.sp, color = Gray6
            )
        }
    }
}