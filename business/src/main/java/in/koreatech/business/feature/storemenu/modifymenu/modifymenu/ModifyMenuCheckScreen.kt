package `in`.koreatech.business.feature.storemenu.modifymenu.modifymenu

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import `in`.koreatech.business.feature.storemenu.TitleAndContent
import `in`.koreatech.business.feature.storemenu.TitleAndImageString
import `in`.koreatech.business.feature.storemenu.TitleAndImageUri
import `in`.koreatech.business.feature.storemenu.TitleAndOptionPrice
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.ColorSecondaryText
import `in`.koreatech.business.ui.theme.ColorTextBackgrond
import `in`.koreatech.business.ui.theme.Gray7
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.core.toast.ToastUtil
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ModifyMenuCheckScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    viewModel: ModifyMenuViewModel = hiltViewModel(),
    goToStoreMainScreen: () -> Unit = {}
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    ModifyMenuCheckScreenImpl(
        onBackPressed = onBackPressed,
        menuName = state.menuName,
        modifyMenuState = state,
        onPositiveButtonClicked = {
            viewModel.onPositiveButtonClicked(context)
        }
    )

    HandleSideEffects(viewModel, goToStoreMainScreen)
}


@Composable
fun ModifyMenuCheckScreenImpl(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    modifyMenuState: ModifyMenuState = ModifyMenuState(),
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
                onClick = { onBackPressed() }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_white_arrow_back),
                    contentDescription = stringResource(R.string.back),
                )
            }
            Text(
                text = stringResource(R.string.menu_modify),
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(color = Color.White, fontSize = 20.sp),
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item{
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
            }

            item {
                TitleAndContent(
                    stringId = R.string.menu_name,
                    content = menuName
                )

                TitleAndOptionPrice(
                    optionPriceList = modifyMenuState.menuOptionPrice,
                    menuPrice = modifyMenuState.menuPrice
                )

                TitleAndContent(
                    stringId = R.string.menu_category,
                    content = modifyMenuState.menuCategoryLabel
                )

                TitleAndContent(
                    stringId = R.string.menu_composition,
                    content = modifyMenuState.description
                )

                TitleAndImageString(
                    imageStringList = modifyMenuState.imageUriList
                )
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
private fun HandleSideEffects(viewModel: ModifyMenuViewModel, goToCheckMenuScreen: () -> Unit) {
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ModifyMenuSideEffect.FinishModifyMenu -> {

                ToastUtil.getInstance().makeShort(context.getString(R.string.menu_success_modify_menu))
                goToCheckMenuScreen()
            }
            is ModifyMenuSideEffect.ShowMessage -> {
                val message = when (sideEffect.type) {
                    ModifyMenuErrorType.FailModifyMenu ->context.getString(R.string.menu_fail_modify_menu)
                    ModifyMenuErrorType.FailUploadImage ->context.getString(R.string.menu_fail_upload_image)
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
        ModifyMenuCheckScreenImpl(
            menuName = "불족발 + 막국수 저녁 Set"
        )
    }
}