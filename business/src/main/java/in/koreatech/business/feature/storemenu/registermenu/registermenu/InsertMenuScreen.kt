package `in`.koreatech.business.feature.storemenu.registermenu.registermenu

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.business.ui.theme.ColorAccent
import `in`.koreatech.business.ui.theme.ColorMinor
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.ColorSecondaryText
import `in`.koreatech.business.ui.theme.ColorTextBackgrond
import `in`.koreatech.business.ui.theme.ColorTransparency
import `in`.koreatech.business.ui.theme.Gray6
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.domain.model.owner.StoreMenuCategory
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RegisterMenuScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    viewModel: RegisterMenuViewModel
) {
    val state = viewModel.collectAsState().value

    RegisterMenuScreenImpl(
        goToCheckMenuScreen = {} ,
        onBackPressed = {},
        registerMenuState = state,
        imageIndex = state.imageIndex,
        isModify = state.isModify,
        changeMenuName = {
            viewModel.changeMenuName(it)
        },
        onChangeMenuPrice = {
            viewModel.changeMenuPrice(it)
        },
        onChangeDetailMenuServing = {
            viewModel.changeDetailMenuServing(it.first, it.second)
        },
        onChangeDetailMenuPrice ={
            viewModel.changeDetailMenuPrice(it.first, it.second)
        } ,
        onDeleteMenuPrice ={
            viewModel.deleteMenuPrice(it)
        },
        onChangeMenuDetail = {
            viewModel.changeMenuDetail(it)
        },
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
        onChangeImage = {
            viewModel.changeMenuImageUri(it)
        },
        onDeleteImage = {
            viewModel.deleteMenuImageUri(it)
        },
        onModifyImage = {
            viewModel.modifyMenuImageUri(it)
        },
        setImageModify = {
            viewModel.isImageModify(it)
        },
        setImageIndex = {
            viewModel.setImageIndex(it)
        },
        onNextButtonClicked = {
            viewModel.onNextButtonClick()
        },
    )

    HandleSideEffects(viewModel, goToCheckMenuScreen = {})
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RegisterMenuScreenImpl(
    modifier: Modifier = Modifier,
    goToCheckMenuScreen: () -> Unit,
    onBackPressed: () -> Unit,
    registerMenuState: RegisterMenuState = RegisterMenuState(),
    imageIndex: Int = 0,
    isModify: Boolean = false,
    changeMenuName: (String) -> Unit = {},
    onChangeMenuDetail: (String) -> Unit = {},
    onChangeMenuPrice: (String) -> Unit = {},
    onChangeDetailMenuServing: (Pair<Int, String>) -> Unit = {},
    onChangeDetailMenuPrice: (Pair<Int, String>) -> Unit = {},
    onDeleteMenuPrice:(Int) -> Unit = {},
    addPriceButtonClicked: () -> Unit = {},
    onRecommendMenuButtonClicked: () -> Unit = {},
    onMainMenuButtonClicked: () -> Unit = {},
    onSetMenuButtonClicked: () -> Unit = {},
    onSideMenuButtonClicked: () -> Unit = {},
    onChangeImage: (List<Uri>) -> Unit = {},
    onDeleteImage: (Int) -> Unit ={},
    onModifyImage: (Uri) -> Unit ={},
    setImageModify:(Boolean) -> Unit ={},
    setImageIndex: (Int) -> Unit = {},
    onNextButtonClicked: () -> Unit ={}
) {
    val sheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia((3 - imageIndex).coerceAtLeast(2)),
        onResult = {
            onChangeImage(it)
        }
    )
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                onModifyImage(uri)
            }
        }
    )
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = Color.White,),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(`in`.koreatech.business.R.drawable.ic_x),
                        contentDescription = "",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 16.dp)
                            .clickable {
                                coroutineScope.launch {
                                    sheetState.hide()
                                }
                                setImageModify(false)
                            }
                    )
                }

                Text(
                    text = stringResource(id = `in`.koreatech.business.R.string.menu_image_add),
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(id = `in`.koreatech.business.R.string.menu_image_can_input),
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                )

                Image(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .clickable {
                            if (imageIndex == 2 || isModify) {
                                singlePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )

                                setImageModify(false)
                            } else {
                                multiplePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        }
                    ,
                    painter = painterResource(`in`.koreatech.business.R.drawable.ic_gallery_picture),
                    contentDescription = ""
                )

                Image(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 48.dp)
                        .clickable {
                            Log.e("로그 클릭", "사진 클릭")
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        },
                    painter = painterResource(`in`.koreatech.business.R.drawable.ic_camera_picture),
                    contentDescription = ""
                )
            }
        }
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
                        .padding(start = 16.dp),
                    onClick = { onBackPressed }
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
            ) {
                item {
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
                            .padding(top = 27.dp),
                        thickness = 1.dp,
                        color = Gray6
                    )
                }

                item {
                    Text(
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                        text = stringResource(id = R.string.menu_price),
                        fontSize = 15.sp,
                        color = ColorPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 8.dp)
                    ){
                        if(registerMenuState.menuDetailPrice.isEmpty()){
                            Box(
                                modifier = modifier
                                    .border(width = 1.dp, color = ColorMinor)
                                    .height(37.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                BasicTextField(
                                    value = registerMenuState.menuPrice,
                                    onValueChange = { newValue ->
                                        onChangeMenuPrice(newValue)
                                    },
                                    textStyle = TextStyle(
                                        color = Color.Black,
                                        fontSize = 14.sp
                                    ),
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .fillMaxWidth()
                                )
                            }
                        }
                        else{
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ){
                                registerMenuState.menuDetailPrice.forEachIndexed { index, menuDetailPrice ->
                                    DetailMenuTextField(
                                        modifier = modifier,
                                        index = index,
                                        menuServing = menuDetailPrice.first,
                                        onChangeMenuServing = onChangeDetailMenuServing,
                                        menuPrice= menuDetailPrice.second,
                                        onChangeMenuPrice = onChangeDetailMenuPrice,
                                        onDeleteMenuPrice = onDeleteMenuPrice
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 16.dp)
                            .clickable {
                                addPriceButtonClicked()
                            },
                        verticalAlignment = Alignment.CenterVertically

                    ) {
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
                            .padding(top = 27.dp),
                        thickness = 1.dp,
                        color = Gray6
                    )
                }

                item {
                    Text(
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                        text = stringResource(id = R.string.menu_category),
                        fontSize = 15.sp,
                        color = ColorPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    if (registerMenuState.menuCategory.isNotEmpty()) {
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

                item {
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
                                .fillMaxSize()
                                .padding(horizontal = 8.dp)
                            ,
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
                            .padding(top = 24.dp),
                        thickness = 1.dp,
                        color = Gray6
                    )
                }

                item {
                    Row(
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.menu_image),
                            fontSize = 15.sp,
                            color = ColorPrimary,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            modifier = Modifier.padding(start = 12.dp),
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
                    ) {
                        itemsIndexed(registerMenuState.imageUriList) { index, item ->
                            if (item == Uri.EMPTY) {
                                Image(
                                    modifier = Modifier
                                        .size(137.dp)
                                        .padding(bottom = 16.dp)
                                        .clickable {
                                            coroutineScope.launch {

                                                setImageIndex(index)

                                                if (sheetState.isVisible) {
                                                    sheetState.hide()
                                                } else {
                                                    sheetState.show()
                                                }
                                            }
                                        },
                                    painter = painterResource(id = R.drawable.ic_add_menu_image),
                                    contentDescription = ""
                                )
                            }
                            else{

                                Box(
                                    modifier = Modifier
                                        .size(137.dp)
                                        .padding(bottom = 16.dp)
                                        .padding(end = 16.dp),
                                    contentAlignment = Alignment.TopEnd
                                )
                                {
                                    Image(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable {
                                                setImageModify(true)
                                                coroutineScope.launch {
                                                    setImageIndex(index)
                                                    if (sheetState.isVisible) {
                                                        sheetState.hide()
                                                    } else {
                                                        sheetState.show()
                                                    }
                                                }
                                            },
                                        painter = rememberAsyncImagePainter(
                                            item
                                        ),
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop
                                    )
                                    Image(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .offset(x = 12.dp, y = (-12).dp)
                                            .clickable {
                                                onDeleteImage(index)
                                            }
                                        ,
                                        painter = painterResource(id = R.drawable.ic_delete_button),
                                        contentDescription = ""
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(27.dp)
                                            .align(Alignment.BottomCenter)
                                            .background(Color.Black.copy(alpha = 0.5f))
                                        ,
                                        text = stringResource(id = R.string.menu_change),
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 24.dp, bottom = 52.dp)
                            .fillMaxWidth()
                            .height(43.dp)
                    ) {
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
                            onClick = onNextButtonClicked,
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
}

@Composable
private fun HandleSideEffects(viewModel: RegisterMenuViewModel, goToCheckMenuScreen: () -> Unit) {
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is RegisterMenuSideEffect.ShowMessage -> {
                val message = when (sideEffect.type) {
                    RegisterMenuErrorType.NullMenuName -> context.getString(R.string.menu_null_name)
                    RegisterMenuErrorType.NullMenuPrice -> context.getString(R.string.menu_null_price)
                    RegisterMenuErrorType.NullMenuCategory -> context.getString(R.string.menu_null_category)
                    RegisterMenuErrorType.NullMenuDescription-> context.getString(R.string.menu_null_description)
                    RegisterMenuErrorType.NullMenuImage-> context.getString(R.string.menu_null_image)
                }
                ToastUtil.getInstance().makeShort(message)
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

@Composable
fun DetailMenuTextField(
    modifier: Modifier = Modifier,
    index: Int = 0,
    menuServing: String = "",
    onChangeMenuServing: (Pair<Int, String>) -> Unit = {},
    menuPrice: String = "",
    onChangeMenuPrice: (Pair<Int, String>) -> Unit = {},
    onDeleteMenuPrice: (Int) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Box(
            modifier = modifier
                .border(width = 1.dp, color = ColorMinor)
                .height(37.dp)
                .width(170.dp)
            ,
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                modifier = Modifier.padding(start = 8.dp),
                value = if(menuServing != stringResource(id = R.string.temp_price))menuServing else "",
                onValueChange = { newValue ->
                    onChangeMenuServing(Pair(index, newValue))
                },
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 14.sp
                ),
                decorationBox = { innerTextField ->
                    if (menuServing == stringResource(id = R.string.temp_price)) {
                        Text(
                            text = stringResource(id = R.string.menu_serving_example),
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

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = modifier
                .border(width = 1.dp, color = ColorMinor)
                .height(37.dp)
                .width(170.dp)
            ,
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                modifier = Modifier.padding(start = 8.dp),
                value = if(menuPrice != stringResource(id = R.string.temp_price))menuPrice else "",
                onValueChange = { newValue ->
                    onChangeMenuPrice(Pair(index, newValue))
                },
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 14.sp
                ),
                decorationBox = { innerTextField ->
                    if (menuPrice == stringResource(id = R.string.temp_price)) {
                        Text(
                            text = stringResource(id = R.string.won),
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

        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    onDeleteMenuPrice(index)
                }
                .align(Alignment.CenterVertically)
            ,
            painter = painterResource(id = R.drawable.ic_delete_button),
            contentDescription = ""
        )
    }
}

@Preview
@Composable
fun PreviewDetailMenuTextField() {
    DetailMenuTextField()
}


//@Preview
//@Composable
//fun PreviewRegisterMenuScreen(){
//    RegisterMenuScreenImpl(
//        modifier = Modifier,
//        goToSelectCategoryScreen = {} ,
//        onBackPressed = {}
//    )
//}