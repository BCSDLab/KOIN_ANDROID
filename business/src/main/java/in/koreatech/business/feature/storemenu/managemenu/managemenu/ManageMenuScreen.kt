package `in`.koreatech.business.feature.storemenu.managemenu.managemenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.Gray3
import `in`.koreatech.koin.core.R
import org.orbitmvi.orbit.compose.collectAsState
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import `in`.koreatech.koin.core.toast.ToastUtil
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ManageMenuScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    navigateToModifyMenuScreen: (Int) -> Unit,
    navigateToRegisterMenuScreen: (Int) -> Unit,
    viewModel: ManageMenuViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value
    ManageMenuScreenImpl(
        onBackPressed = onBackPressed,
        state = state,
        onMenuItemClicked = viewModel::onModifyMenuClicked,
        onAddMenuClicked = viewModel::onRegisterMenuClicked
    )
    HandleSideEffects(viewModel, navigateToModifyMenuScreen, navigateToRegisterMenuScreen)
}
@Composable
fun ManageMenuScreenImpl(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    state: ManageMenuState = ManageMenuState(),
    onMenuItemClicked: (Int) -> Unit ={},
    onAddMenuClicked: () -> Unit = {}
) {
    val categories = listOf(
        stringResource(R.string.recommend_menu),
        stringResource(R.string.main_menu),
        stringResource(R.string.set_menu),
        stringResource(R.string.side_menu)
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .background(ColorPrimary),
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp),
                onClick = { onBackPressed() }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_white_arrow_back),
                    contentDescription = stringResource(R.string.back),
                )
            }
            Text(
                text = stringResource(R.string.menu_edit),
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(color = Color.White, fontSize = 20.sp),
            )
        }

        Text(
            modifier = Modifier
                .padding(top = 32.dp)
                .padding(end = 24.dp)
                .align(Alignment.End)
                .clickable {
                    onAddMenuClicked()
                }
            ,
            text = "+ 메뉴 추가",
            style = TextStyle(color = colorResource(R.color.primary_500), fontSize = 16.sp)
        )

        LazyRow(
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(32.dp)
            ,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(categories.size) {
                Box(
                    modifier = Modifier
                        .fillParentMaxWidth(0.228f)
                        .fillMaxHeight()
                        .border(
                            width = 1.dp, color = Gray3, shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(1.dp),
                        text = categories[it],
                        fontSize = 12.sp,
                        style = TextStyle(color = Gray3, fontSize = 12.sp),
                        fontWeight = FontWeight(500),
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp)
        ) {
            state.storeMenuList?.let { menuList ->
                items(menuList){
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
    }
}

@Composable
private fun HandleSideEffects(
    viewModel: ManageMenuViewModel,
    navigateToModifyMenuScreen: (Int) -> Unit,
    navigateToRegisterMenuScreen: (Int) -> Unit

) {
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ManageMenuSideEffect.NavigateToModifyMenuScreen ->{
                navigateToModifyMenuScreen(sideEffect.menuId)
            }
            is ManageMenuSideEffect.NavigateToRegisterMenuScreen ->{
                navigateToRegisterMenuScreen(sideEffect.storeId)
            }
        }
    }
}

@Preview
@Composable
fun PreviewSettingMenuScreen() {
    ManageMenuScreenImpl()
}