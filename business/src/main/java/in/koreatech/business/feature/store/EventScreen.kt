package `in`.koreatech.business.feature.store

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.business.ui.theme.Gray1
import `in`.koreatech.business.ui.theme.Gray2
import `in`.koreatech.business.ui.theme.Gray4
import `in`.koreatech.business.ui.theme.Gray6
import `in`.koreatech.koin.domain.model.store.ShopEvent
import `in`.koreatech.koin.domain.util.StoreUtil.generateOpenCloseTimeString
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun EventScreen(
    verticalOffset: Boolean,
    currentPage: Int,
    viewModel: MyStoreDetailViewModel,
    onDeleteEvent: () -> Unit
) {
    val state = viewModel.collectAsState().value
    val scrollState = rememberScrollState()
    val enabledScroll by remember(
        verticalOffset, scrollState.value
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
    if (state.isEditMode) {
        EventEditToolBar()
    } else {
        EventToolBar()
    }
    EventItem(enabledScroll, scrollState, viewModel)
    OwnerStoreDialog(
        onDismissRequest = { viewModel.changeDialogVisibility() },
        onConfirmation = { onDeleteEvent() },
        dialogTitle = stringResource(R.string.event_delete_title),
        dialogText = stringResource(R.string.event_delete_text),
        positiveButtonText = stringResource(id = R.string.delete),
        visibility = state.dialogVisibility
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventItem(
    enabledScroll: Boolean,
    scrollState: ScrollState,
    viewModel: MyStoreDetailViewModel,
) {
    val state = viewModel.collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(enabled = enabledScroll, state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        state.storeEvent?.forEachIndexed { index, item ->
            val eventOpenCloseTime = remember(item.startDate, item.endDate) {
                generateOpenCloseTimeString(item.startDate, item.endDate)
            }
            val pagerState =
                rememberPagerState { state.storeEvent[index].thumbnailImages?.size ?: 1 }
            if (state.isEventExpanded[index]) {
                EventExpandedItem(state.storeEvent[index],
                    eventOpenCloseTime,
                    pagerState,
                    onCollapse = { viewModel.toggleEventItem(index) })
            } else {
                EventFoldedItem(
                    state.storeEvent[index],
                    eventOpenCloseTime,
                    onClicked = {
                        if (!state.isEditMode) viewModel.toggleEventItem(index) else viewModel.onChangeEventSelected(
                            item.eventId
                        )
                    },
                    viewModel = viewModel,
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
fun EventEditToolBar() {
    val viewModel: MyStoreDetailViewModel = hiltViewModel()
    val state: MyStoreDetailState = viewModel.collectAsState().value
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(Gray4),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy((-6).dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(painter = if (state.isAllEventSelected) painterResource(id = R.drawable.ic_check_selected) else painterResource(
                id = R.drawable.ic_check
            ),
                contentDescription = stringResource(R.string.check),
                modifier = Modifier.clickable { viewModel.onChangeAllEventSelected() })
            Text(text = stringResource(R.string.all), color = Gray6, fontSize = 12.sp)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = { if (state.isSelectedEvent.size > 1) viewModel.modifyEventError() else viewModel.navigateToModifyScreen() },
                modifier = Modifier
                    .width(100.dp)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorTextField, contentColor = Gray6
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = stringResource(R.string.modify)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.modify))
            }
            Button(
                onClick = { viewModel.changeDialogVisibility() },
                modifier = Modifier
                    .width(100.dp)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorTextField, contentColor = Gray6
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = stringResource(R.string.delete)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.delete))
            }
            Button(
                onClick = { viewModel.onChangeEditMode() },
                modifier = Modifier
                    .width(100.dp)
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorTextField, contentColor = Gray6
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_complete),
                    contentDescription = stringResource(R.string.complete)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.complete))
            }
        }
    }
    Divider(
        color = Gray4, modifier = Modifier.height(1.dp)
    )
}

@Composable
fun EventToolBar() {
    val viewModel: MyStoreDetailViewModel = hiltViewModel()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Button(
            onClick = { viewModel.onChangeEditMode() },
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorTextField, contentColor = Color.Black
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
                backgroundColor = ColorTextField, contentColor = Color.Black
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
}


@Composable
fun EventFoldedItem(
    item: ShopEvent,
    eventOpenCloseTime: String,
    onClicked: () -> Unit = {},
    viewModel: MyStoreDetailViewModel
) {
    val state = viewModel.collectAsState().value
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
                    painter = if (item.thumbnailImages?.size != 0) painterResource(id = R.drawable.no_image) else rememberAsyncImagePainter(
                        model = item.thumbnailImages?.getOrNull(0)
                    ),
                    contentDescription = stringResource(R.string.event_default_image),
                )
                if (state.isEditMode) {
                    viewModel.initEventItem()
                    Image(
                        modifier = Modifier
                            .align(TopStart)
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
    item: ShopEvent, eventOpenCloseTime: String, pagerState: PagerState, onCollapse: () -> Unit = {}
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