package `in`.koreatech.business.feature.store

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.Blue2
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.business.ui.theme.Shapes
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState


@Composable
fun MyStoreDetailScreen(
    modifier: Modifier,
    viewModel: MyStoreDetailViewModel = MyStoreDetailViewModel()
) {
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
        MyStoreScrollScreen(viewModel)


    }
}

@Composable
private fun CollapsedTopBar(
    modifier: Modifier = Modifier, isCollapsed: Boolean
) {
    val color: Color by animateColorAsState(
        if (isCollapsed) Color.White
        else Color.Transparent
    )
    Box(
        modifier = modifier
            .background(color)
            .fillMaxWidth()
            .height(56.dp)
            .padding(16.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        AnimatedVisibility(visible = isCollapsed) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.shop_name), )
                IconButton(onClick = { /*TODO*/ }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_setting),
                        contentDescription = stringResource(R.string.shop_management)
                    )
                }

            }
        }
    }
    Divider(
        color = ColorTextField,
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    )
}

@Composable
fun TopBar(viewModel: MyStoreDetailViewModel) {
    val state = viewModel.collectAsState().value
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(255.dp),
            painter = state.storeInfo?.imageUrls?.get(0)
                .let { painterResource(id = R.drawable.no_image) },
            contentDescription = stringResource(R.string.shop_image),
            contentScale = ContentScale.Crop,
        )

        Button(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .height(40.dp),
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = ColorPrimary,
            ),
            shape = Shapes.medium,
            border = BorderStroke(1.dp, ColorPrimary)

        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_setting),
                    contentDescription = stringResource(R.string.shop_management),
                )
                Text(text = stringResource(R.string.shop_management))
            }

        }
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = state.storeInfo?.name ?: stringResource(R.string.shop_name),
            style = TextStyle(color = Color.Black, fontSize = 20.sp),
            fontSize = 20.sp,
            fontWeight = FontWeight(600),
        )
    }

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyStoreScrollScreen(viewModel: MyStoreDetailViewModel) {
    val toolBarHeight = 145.dp
    val pagerState = rememberPagerState(0, 0f) { 2 }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val isCollapsed: Boolean by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }
    Box {
        CollapsedTopBar(modifier = Modifier.zIndex(2f), isCollapsed = isCollapsed)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.Top,
        ) {
            item {
                TopBar(viewModel)
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                ) {
                    Row(modifier = Modifier.padding(vertical = 5.dp)) {
                        Text(
                            text = stringResource(R.string.telephone_number),
                            style = TextStyle(color = Color.Black, fontSize = 15.sp),
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = "sd",
                            style = TextStyle(color = Color.Black, fontSize = 15.sp),
                        )
                    }
                    Row(modifier = Modifier.padding(vertical = 5.dp)) {
                        Text(
                            text = stringResource(R.string.operating_time),
                            style = TextStyle(color = Color.Black, fontSize = 15.sp),
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = "",
                            style = TextStyle(color = Color.Black, fontSize = 15.sp),
                        )
                    }
                    Row(modifier = Modifier.padding(vertical = 5.dp)) {
                        Text(
                            text = stringResource(R.string.clodsed_day),
                            style = TextStyle(color = Color.Black, fontSize = 15.sp),
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = "",
                            style = TextStyle(color = Color.Black, fontSize = 15.sp),
                        )
                    }
                    Row(modifier = Modifier.padding(vertical = 5.dp)) {
                        Text(
                            text = stringResource(R.string.address),
                            style = TextStyle(color = Color.Black, fontSize = 15.sp),
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = "",
                            style = TextStyle(color = Color.Black, fontSize = 15.sp),
                        )
                    }
                    Row(modifier = Modifier.padding(vertical = 5.dp)) {
                        Text(
                            text = stringResource(R.string.delivery_amount),
                            style = TextStyle(color = Color.Black, fontSize = 15.sp),
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = "",
                            style = TextStyle(color = Color.Black, fontSize = 15.sp),
                        )
                    }
                    Row(modifier = Modifier.padding(vertical = 5.dp)) {
                        Text(
                            text = stringResource(R.string.other_information),
                            style = TextStyle(color = Color.Black, fontSize = 15.sp),
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = "",
                            style = TextStyle(color = Color.Black, fontSize = 15.sp),
                        )
                    }
                    Row(modifier = Modifier.padding(vertical = 5.dp)) {
                        Box(
                            modifier = Modifier.border(
                                width = 1.dp, color = Blue2, shape = RoundedCornerShape(8.dp)
                            )
                        ) {
                            Text(
                                modifier = Modifier.padding(6.dp),
                                text = stringResource(R.string.deliver_available),
                                fontSize = 12.sp,
                                style = TextStyle(color = Color.Black, fontSize = 15.sp),
                                color = Blue2,
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            modifier = Modifier.border(
                                width = 1.dp, color = Blue2, shape = RoundedCornerShape(8.dp)
                            )
                        ) {
                            Text(
                                modifier = Modifier.padding(6.dp),
                                text = stringResource(R.string.card_available),
                                fontSize = 12.sp,
                                style = TextStyle(color = Color.Black, fontSize = 15.sp),
                                color = Blue2,
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            modifier = Modifier.border(
                                width = 1.dp, color = Blue2, shape = RoundedCornerShape(8.dp)
                            )
                        ) {
                            Text(
                                modifier = Modifier.padding(6.dp),
                                text = stringResource(R.string.account_transfer_available),
                                fontSize = 12.sp,
                                style = TextStyle(color = Color.Black, fontSize = 15.sp),
                                color = Blue2,
                            )
                        }

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
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    }) {
                        Text(stringResource(R.string.menu))
                    }
                    Tab(selected = false, onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
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
                            0 -> MenuScreen(isCollapsed, pagerState.currentPage)
                            1 -> EventScreen(isCollapsed, pagerState.currentPage)
                        }
                    }
                }
            }

        }
    }
}
