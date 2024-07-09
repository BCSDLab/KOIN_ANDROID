package `in`.koreatech.business.feature.storemenu.registermenu.registermenu

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.OperatingTimeState
import `in`.koreatech.business.feature.insertstore.selectcategory.CategoryItem
import `in`.koreatech.business.ui.theme.Blue1
import `in`.koreatech.business.ui.theme.ColorAccent
import `in`.koreatech.business.ui.theme.ColorDisabledButton
import `in`.koreatech.business.ui.theme.ColorMinor
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.ColorSecondaryText
import `in`.koreatech.business.ui.theme.ColorTextBackgrond
import `in`.koreatech.business.ui.theme.ColorTransparency
import `in`.koreatech.business.ui.theme.Gray6
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.domain.model.owner.StoreMenuCategory
import org.orbitmvi.orbit.compose.collectAsState


@Composable
fun RegisterMenuScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    viewModel: RegisterMenuViewModel
) {
    val state = viewModel.collectAsState().value

    RegisterMenuScreenImpl(
        goToSelectCategoryScreen = {} ,
        onBackPressed = {},
        menuCategory = state.menuCategory,
        registerMenuState = state,
        menuName = state.menuName,
        changeMenuName = {
            viewModel.changeMenuName(it)
        },
        changeMenuPrice = {
            viewModel.changeMenuPrice(it.first, it.second)
        },
        onChangeMenuDetail = {
            viewModel.changeMenuDetail(it)
        },
        menuPrice = state.menuPrice,
        addPriceButtonClicked = {
            viewModel.addPrice()
        },
        onRecommendMenuButtonClicked = {
            viewModel.recommendMenuIsClicked()
        },
        onMainMenuButtonClicked = {
            viewModel.mainMenuIsClicked()
        },
        onSetMenuButtonClicked = {
            viewModel.setMenuIsClicked()
        },
        onSideMenuButtonClicked = {
            viewModel.sideMenuIsClicked()
        },
    )
}

@Composable
fun RegisterMenuScreenImpl(
    modifier: Modifier = Modifier,
    goToSelectCategoryScreen: () -> Unit,
    onBackPressed: () -> Unit,
    menuCategory: List<StoreMenuCategory> = emptyList(),
    registerMenuState: RegisterMenuState = RegisterMenuState(),
    menuName: String = "",
    changeMenuName: (String) -> Unit = {},
    onChangeMenuDetail: (String) -> Unit = {},
    changeMenuPrice: (Pair<Int, String>) -> Unit = {},
    menuPrice: List<String> = emptyList(),
    addPriceButtonClicked: () -> Unit = {},
    onRecommendMenuButtonClicked: () -> Unit = {},
    onMainMenuButtonClicked: () -> Unit = {},
    onSetMenuButtonClicked: () -> Unit = {},
    onSideMenuButtonClicked: () -> Unit = {}
) {
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
                    .padding(start = 16.dp)
                ,
                onClick = { /*TODO*/ }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_white_arrow_back),
                    contentDescription = stringResource(R.string.back),
                )
            }
            Text(
                text = stringResource(R.string.menu_add),
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(color = Color.White, fontSize = 20.sp),
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ){
            item{
                DivideOption(16.dp, stringResource(id = R.string.menu_info))

                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    text = stringResource(id = R.string.menu_name),
                    fontSize = 15.sp,
                    color = ColorPrimary,
                    fontWeight = FontWeight.Bold
                )

                BorderTextField(
                    height = 37.dp,
                    inputString = registerMenuState.menuName,
                    onStringChange = changeMenuName
                )

                Divider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 27.dp)
                    ,
                    thickness = 1.dp,
                    color = Gray6
                )
            }

            item{
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    text = stringResource(id = R.string.menu_price),
                    fontSize = 15.sp,
                    color = ColorPrimary,
                    fontWeight = FontWeight.Bold
                )

                registerMenuState.menuPrice.forEachIndexed { index, menuName ->

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 8.dp)
                                .border(width = 1.dp, color = ColorMinor)
                                .height(37.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            BasicTextField(
                                value = menuName,
                                onValueChange = { newValue ->
                                    changeMenuPrice(Pair(index, newValue))
                                },
                                textStyle = TextStyle(
                                    color = Color.Black,
                                    fontSize = 14.sp
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp)
                        .clickable {
                            addPriceButtonClicked()
                        }
                    ,
                    verticalAlignment = Alignment.CenterVertically

                ){
                    Image(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null
                    )

                    Text(
                        text = stringResource(id = R.string.add_price),
                        color = ColorAccent,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Divider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 27.dp)
                    ,
                    thickness = 1.dp,
                    color = Gray6
                )
            }

            item{
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    text = stringResource(id = R.string.menu_category),
                    fontSize = 15.sp,
                    color = ColorPrimary,
                    fontWeight = FontWeight.Bold
                )
                if(registerMenuState.menuCategory.isNotEmpty())
                {
                    CategoryRadioButtonScreen(
                        menuCategory = registerMenuState.menuCategory,
                        onRecommendMenuButtonClicked = onRecommendMenuButtonClicked,
                        onMainMenuButtonClicked = onMainMenuButtonClicked,
                        onSetMenuButtonClicked = onSetMenuButtonClicked,
                        onSideMenuButtonClicked = onSideMenuButtonClicked,
                        isRecommendMenu = registerMenuState.isRecommendMenu,
                        isMainMenu = registerMenuState.isMainMenu,
                        isSetMenu = registerMenuState.isSetMenu,
                        isSideMenu = registerMenuState.isSideMenu
                    )
                }
            }

            item{
                DivideOption(22.dp, stringResource(id = R.string.menu_detail))

                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    text = stringResource(id = R.string.menu_composition),
                    fontSize = 15.sp,
                    color = ColorPrimary,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                        .border(width = 1.dp, color = ColorMinor)
                        .height(105.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = registerMenuState.description,
                        onValueChange = onChangeMenuDetail,
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier
                            .fillMaxSize(),
                        decorationBox = { innerTextField ->
                            if (registerMenuState.description.isEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.menu_description_example),
                                    style = TextStyle(
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                )
                            }
                            innerTextField()
                        }
                    )
                }

                Divider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 24.dp)
                    ,
                    thickness = 1.dp,
                    color = Gray6
                )
            }

            item{
                Row(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                ){
                    Text(
                        text = stringResource(id = R.string.menu_image),
                        fontSize = 15.sp,
                        color = ColorPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        modifier = Modifier.padding(start= 12.dp),
                        text = stringResource(id = R.string.menu_image_maximum),
                        fontSize = 15.sp,
                        color = Gray6,
                        fontWeight = FontWeight.Bold
                    )
                }

                LazyRow(
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                ){
                    itemsIndexed(registerMenuState.imageUriList){ index, item ->
                        if(item == ""){
                            Image(
                                modifier = Modifier
                                    .size(137.dp)
                                    .padding(bottom = 16.dp)
                                ,
                                painter = painterResource(id = R.drawable.ic_add_menu_image),
                                contentDescription = "이미지 삽입"
                            )
                        }
                    }
                }
            }

            item{
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .padding(top = 24.dp, bottom = 52.dp)
                        .fillMaxWidth()
                        .height(43.dp)
                ){
                    Button(
                        onClick = {},
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(Color.White),
                        modifier = modifier
                            .fillMaxHeight()
                            .width(93.dp)
                            .border(1.dp, ColorSecondary),
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorSecondary
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {},
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(ColorPrimary),
                        modifier = modifier
                            .height(43.dp)
                            .width(226.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.next),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryRadioButton(
    modifier: Modifier = Modifier,
    startDp: Dp = 0.dp,
    endDp: Dp = 0.dp,
    buttonName: String = "",
    isClicked: Boolean = false,
    onButtonClicked: () ->Unit = {}
) {
    Box(
        modifier = Modifier
            .width(185.dp)
            .height(50.dp)
            .padding(start = startDp, end = endDp)
            .border(width = 0.5.dp, color = if (isClicked) ColorSecondary else Gray6)
            .background(color = if (isClicked) ColorSecondary else ColorTransparency)
            .clickable {
                onButtonClicked()
            }
        ,
        contentAlignment = Alignment.Center
    ){
        Text(
            text = buttonName,
            color = if(isClicked) Color.White else Color.Black,
            fontWeight = FontWeight.Bold
        )

    }
}

@Composable
private fun CategoryRadioButtonScreen(
    menuCategory: List<StoreMenuCategory> = emptyList(),
    onRecommendMenuButtonClicked: () -> Unit = {},
    onMainMenuButtonClicked: () -> Unit = {},
    onSetMenuButtonClicked: () -> Unit = {},
    onSideMenuButtonClicked: () -> Unit = {},
    isRecommendMenu : Boolean = false,
    isMainMenu: Boolean = true,
    isSetMenu: Boolean = true,
    isSideMenu: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
    ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                CategoryRadioButton(
                    buttonName = menuCategory[0].menuCategoryName,
                    isClicked = isRecommendMenu,
                    onButtonClicked = onRecommendMenuButtonClicked
                )

                Spacer(modifier = Modifier.weight(1f))

                CategoryRadioButton(
                    buttonName = menuCategory[1].menuCategoryName,
                    isClicked = isMainMenu,
                    onButtonClicked = onMainMenuButtonClicked
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ){
                CategoryRadioButton(
                    buttonName = menuCategory[2].menuCategoryName,
                    isClicked = isSetMenu,
                    onButtonClicked = onSetMenuButtonClicked
                )

                Spacer(modifier = Modifier.weight(1f))

                CategoryRadioButton(
                    buttonName = menuCategory[3].menuCategoryName,
                    isClicked =  isSideMenu,
                    onButtonClicked = onSideMenuButtonClicked
                )
            }
    }
}

@Composable
fun BorderTextField(
    height: Dp = 20.dp,
    inputString: String = "",
    onStringChange: (String) -> Unit = {},
){
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
            .border(width = 1.dp, color = ColorMinor)
            .height(height),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = inputString,
            onValueChange = onStringChange,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 14.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun DivideOption(
    padding: Dp,
    optionText: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = padding)
            .height(35.dp)
            .background(ColorTextBackgrond),
        contentAlignment = Alignment.CenterStart
    ){
        Text(
            modifier = Modifier
                .padding(start = 16.dp),
            text = optionText,
            color = ColorSecondaryText
        )
    }
}

@Preview
@Composable
fun PreviewRegisterMenuScreen(){
    RegisterMenuScreenImpl(
        modifier = Modifier,
        goToSelectCategoryScreen = {} ,
        onBackPressed = {}
    )
}