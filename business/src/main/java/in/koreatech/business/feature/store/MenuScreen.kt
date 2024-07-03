package `in`.koreatech.business.feature.store

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.ColorCategory
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.business.ui.theme.Gray3
import `in`.koreatech.business.ui.theme.Gray6
import org.orbitmvi.orbit.compose.collectAsState


@Composable
fun MenuScreen(verticalOffset: Boolean, currentPage: Int) {
    val viewModel: MyStoreDetailViewModel = hiltViewModel()
    val state = viewModel.collectAsState().value
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
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(4) {
            Box(
                modifier = Modifier
                    .fillParentMaxWidth(0.25f)
                    .height(40.dp)
                    .padding(end = 10.dp)
                    .border(
                        width = 1.dp, color = Gray3, shape = RoundedCornerShape(4.dp)
                    ),
                contentAlignment = Alignment.Center

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
            Row(
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(
                        id = when (it.id) {
                            1 -> R.drawable.ic_recommend
                            2 -> R.drawable.ic_main
                            3 -> R.drawable.ic_set
                            4 -> R.drawable.ic_side
                            else -> R.drawable.ic_recommend
                        }
                    ), contentDescription = stringResource(R.string.category)
                )
                Text(
                    text = it.name ?: "",
                    modifier = Modifier.padding(10.dp),
                    color = ColorCategory,
                    fontWeight = FontWeight(500),
                    fontSize = 18.sp
                )
            }
            it.menus?.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = item.name, fontWeight = FontWeight(500))
                        Text(text = item.singlePrice.toString() + "원", color = ColorPrimary)
                    }
                    Image(
                        modifier = Modifier
                            .width(68.dp)
                            .height(68.dp),
                        painter = painterResource(id = R.drawable.ic_koin_logo),
                        contentDescription = stringResource(R.string.menu_default_image),
                    )
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
}
