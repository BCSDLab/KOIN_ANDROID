package `in`.koreatech.business.feature.storemenu.registermenu.registermenu

import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.ColorSecondaryText
import `in`.koreatech.business.ui.theme.ColorTextBackgrond
import `in`.koreatech.business.ui.theme.ColorTransparency
import `in`.koreatech.business.ui.theme.Gray6
import `in`.koreatech.business.ui.theme.Gray7
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.core.toast.ToastUtil
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun RegisterMenuCheckScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    viewModel: RegisterMenuViewModel = hiltViewModel(),
    goToStoreMainScreen: () -> Unit = {}
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    RegisterMenuCheckScreenImpl(
        onBackPressed = onBackPressed,
        menuName = state.menuName,
        registerMenuState = state,
        onPositiveButtonClicked = {
            viewModel.onPositiveButtonClicked(context)
        }
    )

    HandleSideEffects(viewModel, goToStoreMainScreen)
}


@Composable
fun RegisterMenuCheckScreenImpl(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    registerMenuState: RegisterMenuState = RegisterMenuState(),
    menuName: String = "",
    onPositiveButtonClicked: () -> Unit = {}
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .height(35.dp)
                        .background(ColorTextBackgrond),
                    contentAlignment = Alignment.CenterStart
                ){
                    Text(
                        modifier = Modifier
                            .padding(start = 16.dp),
                        text = stringResource(id = R.string.menu_info),
                        color = ColorSecondaryText
                    )
                }

                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    text = stringResource(id = R.string.menu_name),
                    fontSize = 15.sp,
                    color = ColorPrimary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 12.dp),
                    text = menuName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Divider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    thickness = 1.dp,
                    color = Gray7
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

                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                ){
                    if(registerMenuState.menuOptionPrice.isEmpty()){
                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = stringResource(id = R.string.menu_price_won, registerMenuState.menuPrice),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    else{
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ){
                            registerMenuState.menuOptionPrice.forEach { menuDetailPrice ->
                                Text(
                                    modifier = Modifier.padding(top = 4.dp),
                                    text = stringResource(id = R.string.menu_price_many_won, menuDetailPrice.option, menuDetailPrice.price),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Divider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    thickness = 1.dp,
                    color = Gray7
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

                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    text = registerMenuState.menuCategoryLabel,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Divider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    thickness = 1.dp,
                    color = Gray7
                )
            }

            item {
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    text = stringResource(id = R.string.menu_composition),
                    fontSize = 15.sp,
                    color = ColorPrimary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                    ,
                    text = registerMenuState.description,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Divider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    thickness = 1.dp,
                    color = Gray7
                )
            }
            
            item{
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    text = stringResource(id = R.string.menu_image),
                    fontSize = 15.sp,
                    color = ColorPrimary,
                    fontWeight = FontWeight.Bold
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                ) {
                    items(registerMenuState.imageUriList) { item ->
                        if (item != Uri.EMPTY){
                            Image(
                                modifier = Modifier
                                    .size(137.dp)
                                    .padding(end = 16.dp)
                                ,
                                painter = rememberAsyncImagePainter(
                                    item
                                ),
                                contentDescription = "",
                                contentScale = ContentScale.Crop
                            )
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
                        .height(43.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onBackPressed,
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(Color.White),
                        modifier = modifier
                            .border(1.dp, ColorSecondary)
                            .fillMaxHeight()
                            .width(113.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorSecondary
                        )
                    }

                    Button(
                        onClick = onPositiveButtonClicked,
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(ColorPrimary),
                        modifier = modifier
                            .fillMaxSize()
                    ) {
                        Text(
                            text = stringResource(id = R.string.positive),
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
private fun HandleSideEffects(viewModel: RegisterMenuViewModel, goToCheckMenuScreen: () -> Unit) {
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is RegisterMenuSideEffect.FinishRegisterMenu -> {

                ToastUtil.getInstance().makeShort(context.getString(R.string.menu_success_register_menu))
                goToCheckMenuScreen()
            }
            is RegisterMenuSideEffect.ShowMessage -> {
                val message = when (sideEffect.type) {
                    RegisterMenuErrorType.FailRegisterMenu ->context.getString(R.string.menu_fail_register_menu)
                    RegisterMenuErrorType.FailUploadImage ->context.getString(R.string.menu_fail_upload_image)
                    else -> ""
                }
                ToastUtil.getInstance().makeShort(message)
            }
            else -> ""
        }
    }
}

@Preview
@Composable
fun PreviewRegisterMenuCheckScreen() {
    Surface {
        RegisterMenuCheckScreenImpl(
            menuName = "불족발 + 막국수 저녁 Set"
        )
    }
}