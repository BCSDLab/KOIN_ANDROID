package `in`.koreatech.business.feature.store.storedetail.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailState
import `in`.koreatech.business.ui.theme.ColorTextField
import `in`.koreatech.business.ui.theme.Gray1
import `in`.koreatech.business.ui.theme.Gray3
import `in`.koreatech.business.ui.theme.Gray6

@Composable
fun MenuScreen(
    verticalOffset: Boolean,
    currentPage: Int,
    state: MyStoreDetailState,
    onMenuItemClicked: (Int) -> Unit = {},
    onManageMenuClicked: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    val enabledScroll by remember(
        verticalOffset,
        scrollState.value,
    ) { derivedStateOf { verticalOffset || scrollState.value != 0 } }
    val categories =
        listOf(
            stringResource(R.string.recommend_menu),
            stringResource(R.string.main_menu),
            stringResource(R.string.set_menu),
            stringResource(R.string.side_menu),
        )
    LaunchedEffect(scrollState.value) {
        if (scrollState.value != 0 && currentPage != 0) {
            scrollState.scrollTo(0)
        }
    }

    Button(
        onClick = {
            onManageMenuClicked()
        },
        modifier =
            Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxWidth()
                .height(40.dp),
        colors =
            ButtonDefaults.buttonColors(
                backgroundColor = ColorTextField,
                contentColor = Color.Black,
            ),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_edit),
            contentDescription = stringResource(R.string.edit),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(R.string.edit_menu), color = Gray1)
    }

    Divider(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(1.dp),
        color = colorResource(R.color.neutral_300),
    )

    LazyRow(
        modifier =
            Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp)
                .padding(bottom = 9.dp)
                .fillMaxWidth()
                .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(categories.size) {
            Box(
                modifier =
                    Modifier
                        .fillParentMaxWidth(0.25f)
                        .height(40.dp)
                        .padding(end = 10.dp)
                        .border(
                            width = 1.dp,
                            color = Gray3,
                            shape = RoundedCornerShape(4.dp),
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = categories[it],
                    fontSize = 12.sp,
                    style = TextStyle(color = Gray6, fontSize = 13.sp),
                    fontWeight = FontWeight(500),
                )
            }
        }
    }
    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize(),
        userScrollEnabled = enabledScroll,
    ) {
        state.storeMenu?.let {
            items(state.storeMenu) {
                MenuCategories(it)
                MenuItem(
                    menuList = it,
                    onMenuClicked = { menuId ->
                        onMenuItemClicked(menuId)
                    },
                )
            }
        }
    }
}
