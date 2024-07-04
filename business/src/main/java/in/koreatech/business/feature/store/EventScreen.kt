package `in`.koreatech.business.feature.store

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.business.ui.theme.Gray1
import `in`.koreatech.business.ui.theme.Gray2
import `in`.koreatech.business.ui.theme.Gray6
import `in`.koreatech.koin.domain.model.store.ShopEvent
import `in`.koreatech.koin.domain.util.StoreUtil.generateOpenCloseTimeString
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventScreen(verticalOffset: Boolean, currentPage: Int) {
    val viewModel: MyStoreDetailViewModel = hiltViewModel()
    val scrollState = rememberScrollState()
    val state = viewModel.collectAsState().value
    val enabledScroll by remember(
        verticalOffset,
        scrollState.value
    ) { derivedStateOf { verticalOffset || scrollState.value != 0 } }

    LaunchedEffect(scrollState.value) {
        if (scrollState.value != 0 && currentPage != 1) {
            scrollState.scrollTo(0)
        }
    }

    LaunchedEffect(state.storeEvent) {
        state.isEventExpanded.forEachIndexed { index, item ->
            viewModel.initEventItem()
        }
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorTextField,
                contentColor = Color.Black
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = stringResource(R.string.edit)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.edit))
        }
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorTextField,
                contentColor = Color.Black
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_add_box),
                contentDescription = stringResource(R.string.add)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.add))
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(enabled = enabledScroll, state = scrollState)
    ) {
        state.storeEvent?.events?.forEachIndexed { index, item ->
            val eventOpenCloseTime = remember(item.startDate, item.endDate) {
                generateOpenCloseTimeString(item.startDate, item.endDate)
            }
            val pagerState =
                rememberPagerState { state.storeEvent.events[index].thumbnailImages?.size ?: 1 }
            if (state.isEventExpanded[index]) {
                EventExpandedItem(
                    state.storeEvent.events[index],
                    eventOpenCloseTime,
                    pagerState,
                    onCollapse = { viewModel.toggleEventItem(index) })
            } else {
                EventItem(
                    state.storeEvent.events[index],
                    eventOpenCloseTime,
                    onClicked = { viewModel.toggleEventItem(index) })
            }
            Divider(
                color = ColorTextField,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .height(1.dp)
            )
        }
    }
}

@Composable
fun EventItem(item: ShopEvent, eventOpenCloseTime: String, onClicked: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .clickable(onClick = { onClicked() })
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        item.thumbnailImages?.getOrNull(0)?.let {
            Image(
                modifier = Modifier
                    .width(72.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(5.dp)),
                painter = if (item.thumbnailImages?.size == 0) painterResource(id = R.drawable.no_image) else
                    rememberAsyncImagePainter(model = item.thumbnailImages?.getOrNull(0)),
                contentDescription = stringResource(R.string.event_default_image),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = item.title, fontWeight = FontWeight(500))
                Row {
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
            Text(text = item.content, fontSize = 12.sp, color = Gray6)
            Text(
                text = eventOpenCloseTime,
                fontSize = 10.sp,
                color = Gray6
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventExpandedItem(item: ShopEvent, eventOpenCloseTime: String, pagerState: PagerState, onCollapse: () -> Unit = {}) {
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
                    painter = if (item.thumbnailImages?.size == 0) painterResource(id = R.drawable.test) else
                        rememberAsyncImagePainter(model = item.thumbnailImages?.getOrNull(it)),
                    contentDescription = stringResource(R.string.event_default_image),
                    contentScale = ContentScale.Inside
                )
            }

        }
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = item.title, fontWeight = FontWeight(500))
                Row {
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
                text = eventOpenCloseTime,
                fontSize = 10.sp,
                color = Gray6
            )
        }
    }
}