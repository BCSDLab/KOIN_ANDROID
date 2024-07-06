package `in`.koreatech.business.feature.store

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.Blue2
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.business.ui.theme.Gray3
import `in`.koreatech.koin.core.toast.ToastUtil
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyStoreDetailScreen(
    modifier: Modifier,
    navigateToLoginScreen: () -> Unit = {},
) {
    val viewModel: MyStoreDetailViewModel = hiltViewModel()
    val state = viewModel.collectAsState().value
    val pagerState = rememberPagerState(0, 0f) { 2 }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorPrimary),
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_setting),
                    contentDescription = stringResource(R.string.back),
                )
            }
            Text(
                text = stringResource(R.string.my_shop),
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(color = Color.White, fontSize = 18.sp),
            )
        }
        MyStoreScrollScreen(state, listState, pagerState, viewModel, onTabSelected = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(it)
            }
        }) {

            state.isSelectedEvent.forEach {
                viewModel.deleteEventItem(state.storeId, it)
            }

        }
    }
    viewModel.collectSideEffect {
        when (it) {
            is MyStoreDetailSideEffect.ShowErrorMessage -> {
                ToastUtil.getInstance().makeShort(it.errorMessage)
                navigateToLoginScreen()}
            MyStoreDetailSideEffect.NavigateToUploadEventScreen -> TODO()
            MyStoreDetailSideEffect.ShowDialog -> TODO()
        }
    }
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyStoreScrollScreen(
    state: MyStoreDetailState,
    listState: LazyListState = rememberLazyListState(),
    pagerState: PagerState = rememberPagerState(0, 0f) { 2 },
    viewModel: MyStoreDetailViewModel,
    onTabSelected: (Int) -> Unit = {},
    onDeleteEvent: () -> Unit = {},
) {
    val toolBarHeight = 145.dp
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val isCollapsedTopBar: Boolean by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }
    val isCollapsed: Boolean by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 5 }
    }
    val infoDataList = getInfoDataList(state)

    val available = mutableMapOf(
        stringResource(
            R.string.delivery_available
        ) to state.storeInfo?.isDeliveryOk,
        stringResource(
            R.string.card_payment_available
        ) to state.storeInfo?.isCardOk,
        stringResource(
            R.string.bank_transfer_available
        ) to state.storeInfo?.isBankOk
    )
    Box {
        CollapsedTopBar(modifier = Modifier.zIndex(2f), isCollapsed = isCollapsedTopBar)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.Top,
        ) {
            item {
                TopBar()
            }
            storeDetailInfo(infoDataList)
            item {
                LazyRow(modifier = Modifier.padding(vertical = 5.dp, horizontal = 20.dp)) {
                    items(available.size) {
                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = if (available[available.keys.elementAt(it)] == true) Blue2 else Gray3,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Text(
                                modifier = Modifier.padding(6.dp),
                                text = available.keys.elementAt(it),
                                fontSize = 12.sp,
                                style = TextStyle(fontSize = 15.sp),
                                color = if (available[available.keys.elementAt(it)] == true) Blue2 else Gray3,
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
            }
            item {
                Divider(
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .height(12.dp),
                    color = ColorTextField,
                )
            }
            stickyHeader {
                TabRow(modifier = Modifier.height(45.dp),
                    selectedTabIndex = pagerState.currentPage,
                    backgroundColor = Color.White,
                    contentColor = Color.Black,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(
                                tabPositions[pagerState.currentPage],
                            ), color = ColorPrimary
                        )
                    }) {
                    Tab(selected = true, onClick = {
                        onTabSelected(0)
                    }) {
                        Text(stringResource(R.string.menu))
                    }
                    Tab(selected = false, onClick = {
                        onTabSelected(1)
                    }) {
                        Text(stringResource(R.string.event_notification))
                    }
                }
            }
            item {
                HorizontalPager(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(screenHeight - toolBarHeight),
                    state = pagerState,
                ) { page ->
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        when (page) {
                            0 -> MenuScreen(isCollapsed, pagerState.currentPage, state)
                            1 -> EventScreen(
                                isCollapsed,
                                pagerState.currentPage,
                                viewModel,
                            ) {
                                onDeleteEvent()
                            }
                        }
                    }
                }
            }
        }
    }
}
