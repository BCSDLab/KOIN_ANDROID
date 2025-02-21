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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import `in`.koreatech.business.ui.theme.KOIN_ANDROIDTheme
import `in`.koreatech.koin.core.designsystem.component.button.FilledButtonColors
import `in`.koreatech.koin.core.designsystem.component.dialog.ChoiceDialog
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.domain.model.store.ShopMenus
import kotlinx.coroutines.launch
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
        modifier = modifier,
        onBackPressed = onBackPressed,
        state = state,
        onModifyMenuClicked = viewModel::onModifyMenuClicked,
        onDeleteMenuClicked = viewModel::onDeleteMenuClicked,
        onAddMenuClicked = viewModel::onRegisterMenuClicked,
        onCheckBoxClicked = viewModel::onCheckBoxClicked,
        onChangeShowDialog = viewModel::onChangeIsShowDialogState,
        onPositiveButtonClicked = viewModel::onPositiveButtonClicked
    )
    HandleSideEffects(viewModel, navigateToModifyMenuScreen, navigateToRegisterMenuScreen)
}
@Composable
fun ManageMenuScreenImpl(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    state: ManageMenuState = ManageMenuState(),
    onModifyMenuClicked: (Int) -> Unit ={},
    onDeleteMenuClicked:(ShopMenus) -> Unit = {},
    onAddMenuClicked: () -> Unit = {},
    onCheckBoxClicked: (Int) -> Unit = {},
    onChangeShowDialog: () -> Unit = {},
    onPositiveButtonClicked: () -> Unit = {}
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        if(state.isShowDeleteDialog){
            ChoiceDialog(
                title = {},
                description = {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(KoinTheme.typography.regular16.toSpanStyle()) {
                                append("${state.menuName}을(를)\n")
                            }
                            withStyle(KoinTheme.typography.medium16.copy(color = KoinTheme.colors.danger700).toSpanStyle()) {
                                append(stringResource(R.string.common_delete))
                            }
                            withStyle(KoinTheme.typography.regular16.toSpanStyle()) {
                                append("하시겠어요?")
                            }
                        },
                        textAlign = TextAlign.Center
                    )
                },
                onPositive = onPositiveButtonClicked,
                onNegative = onChangeShowDialog,
                positiveButtonColors = FilledButtonColors.Danger
            )
        }

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
                style = KoinTheme.typography.medium20,
                color = Color.White,
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
            text = stringResource(R.string.menu_add_plus),
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
            itemsIndexed(state.storeMenuCategoryList) { index, item->
                Box(
                    modifier = Modifier
                        .background(color = if(item.isChecked) ColorPrimary else Color.White, shape = RoundedCornerShape(8.dp))
                        .fillParentMaxWidth(0.228f)
                        .fillMaxHeight()
                        .clickable {
                            coroutineScope.launch {
                                listState.animateScrollToItem(index)
                            }
                            onCheckBoxClicked(index)
                        }
                        .border(
                            width = 1.dp, color = Gray3, shape = RoundedCornerShape(8.dp)
                        )
                    ,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(1.dp),
                        text = item.categoryName,
                        style = KoinTheme.typography.medium12,
                        color = if(item.isChecked) Color.White else Gray3,
                    )
                }
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp)
        ) {
            state.storeMenuList?.let { menuList ->
                items(menuList){
                    MenuCategories(it)
                    MenuItem(
                        menuList = it,
                        onModifyMenuClicked = {menuId ->
                            onModifyMenuClicked(menuId)
                        },
                        onDeleteMenuClicked = { menuItem ->
                            onDeleteMenuClicked(menuItem)
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
            is ManageMenuSideEffect.SuccessDeleteMenu ->{

            }
            is ManageMenuSideEffect.FailedDeleteMenu ->{

            }
        }
    }
}

@Preview
@Composable
fun PreviewSettingMenuScreen() {
    ManageMenuScreenImpl()
}