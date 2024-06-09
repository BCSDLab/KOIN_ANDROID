package `in`.koreatech.business.feature.store

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.business.ui.theme.Gray1
import `in`.koreatech.business.ui.theme.Gray6

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventScreen(verticalOffset: Boolean, currentPage: Int) {
    val list = listOf("Test1", "Test2", "Test3","Test4","Test5","Test6","Test7","Test8","Test9","Test10")
    val scrollState = rememberScrollState()
    val expandedItem = List(list.size) { rememberSaveable { mutableStateOf(false) } }

    LaunchedEffect(scrollState.value) {
        if (scrollState.value != 0 && currentPage != 1) {
            scrollState.scrollTo(0)
        }
    }

    LaunchedEffect(expandedItem) {
        expandedItem.forEachIndexed { index, item ->
            item.value = false
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
            .verticalScroll(enabled = verticalOffset || scrollState.value != 0, state = scrollState)
    ) {
        list.forEachIndexed { index, item ->
            val pagerState = rememberPagerState { 3 }
            if (expandedItem[index].value) {
                EventExpandedItem(
                    item,
                    pagerState,
                    onCollapse = { expandedItem[index].value = false })
            } else {
                EventItem(item, onClicked = { expandedItem[index].value = true })
            }

            Divider(
                color = ColorTextField,
                modifier = Modifier
                    .width(327.dp)
                    .height(1.dp)
            )
        }

    }
}

@Composable
fun EventItem(item: String, onClicked: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .clickable(onClick = { onClicked() })
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .width(68.dp)
                .height(68.dp),
            painter = painterResource(id = R.drawable.ic_koin_logo),
            contentDescription = stringResource(R.string.event_default_image),
        )
        Column() {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = item, fontWeight = FontWeight(500))
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
            Text(text = "", fontSize = 12.sp, color = Gray6)
            Text(text = "", fontSize = 10.sp, color = Gray6)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventExpandedItem(item: String, pagerState: PagerState, onCollapse: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        HorizontalPager(
            modifier = Modifier
                .width(327.dp)
                .height(363.dp),
            verticalAlignment = Alignment.CenterVertically,
            state = pagerState,
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.no_event_image),
                contentDescription = stringResource(R.string.event_default_image),
            )
        }
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = item, fontWeight = FontWeight(500))
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
            Text(text = "", fontSize = 12.sp, color = Gray1)
            Text(text = "", fontSize = 10.sp, color = Gray6)
        }
    }
}