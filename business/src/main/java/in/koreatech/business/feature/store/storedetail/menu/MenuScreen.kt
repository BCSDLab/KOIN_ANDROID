package `in`.koreatech.business.feature.store.storedetail.menu

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailState
import `in`.koreatech.business.ui.theme.Gray3
import `in`.koreatech.business.ui.theme.Gray6


@Composable
fun MenuScreen(
    verticalOffset: Boolean,
    currentPage: Int,
    state: MyStoreDetailState,
    onMenuItemClicked: (Int) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val enabledScroll by remember(
        verticalOffset, scrollState.value
    ) { derivedStateOf { verticalOffset || scrollState.value != 0 } }
    val categories = listOf(
        stringResource(R.string.recommend_menu),
        stringResource(R.string.main_menu),
        stringResource(R.string.set_menu),
        stringResource(R.string.side_menu)
    )
    LaunchedEffect(scrollState.value) {
        if (scrollState.value != 0 && currentPage != 0) {
            scrollState.scrollTo(0)
        }
    }
    LazyRow(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 9.dp)
            .fillMaxWidth()
            .height(40.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        items(categories.size) {
            Box(
                modifier = Modifier
                    .fillParentMaxWidth(0.25f)
                    .height(40.dp)
                    .padding(end = 10.dp)
                    .border(
                        width = 1.dp, color = Gray3, shape = RoundedCornerShape(4.dp)
                    ), contentAlignment = Alignment.Center

            ) {
                Text(
                    modifier = Modifier.padding(13.dp),
                    text = categories[it],
                    fontSize = 12.sp,
                    style = TextStyle(color = Gray6, fontSize = 15.sp),
                    fontWeight = FontWeight(500),
                )
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(enabled = enabledScroll, state = scrollState)
    ) {

        state.storeMenu?.forEach {
            MenuCategories(it)
            MenuItem(
                menuList = it,
                onMenuClicked = {menuId ->
                        onMenuItemClicked(menuId)
                }
            )
        }
    }
}
