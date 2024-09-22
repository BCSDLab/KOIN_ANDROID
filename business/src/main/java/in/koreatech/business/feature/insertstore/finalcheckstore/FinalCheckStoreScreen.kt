package `in`.koreatech.business.feature.insertstore.finalcheckstore

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.InsertDetailInfoScreenState
import `in`.koreatech.business.feature.insertstore.insertmaininfo.InsertBasicInfoScreenState
import `in`.koreatech.business.feature.insertstore.selectcategory.InsertStoreProgressBar
import `in`.koreatech.business.ui.theme.ColorActiveButton
import `in`.koreatech.business.ui.theme.ColorMinor
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.core.toast.ToastUtil
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect


@Composable
fun FinalCheckStoreScreen(
    modifier: Modifier = Modifier,
    storeInfo: InsertDetailInfoScreenState,
    onBackPressed: () -> Unit,
    navigateToFinishScreen: () -> Unit,
    viewModel: FinalCheckStoreScreenViewModel = hiltViewModel()
){
    viewModel.getStoreInfo(storeInfo)
    val state = viewModel.collectAsState().value

    FinalCheckStoreScreenImpl(
        state = state,
        navigateToFinishScreen = {
            viewModel.registerStore()
        },
        onBackPressed = onBackPressed
    )

    HandleSideEffects(viewModel, navigateToFinishScreen)
}


@Composable
fun FinalCheckStoreScreenImpl(
    modifier: Modifier = Modifier,
    navigateToFinishScreen: () -> Unit = {},
    onBackPressed: () -> Unit  = {},
    state: FinalCheckStoreScreenState = FinalCheckStoreScreenState()
) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .padding(top = 56.dp, start = 10.dp, bottom = 18.dp)
                        .size(40.dp)
                        .clickable {
                            onBackPressed()
                        }
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_arrow_left),
                        contentDescription = "backArrow",
                        modifier = modifier
                            .size(40.dp)
                    )
                }
            }

            item {
                Text(
                    modifier = Modifier.padding(top = 35.dp, start = 40.dp),
                    text = stringResource(id = R.string.insert_store),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Text(
                    modifier = Modifier.padding(top = 34.dp, start = 40.dp),
                    text = stringResource(id = R.string.insert_store_main_info),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                InsertStoreProgressBar(
                    modifier,
                    1f,
                    R.string.insert_store_check_store_info,
                    R.string.page_four
                )
            }

            item {
                NameTextField(
                    textString = stringResource(id = R.string.category),
                    outputString = state.storeCategoryString,
                    paddingTopValue = 32.dp
                )
            }

            item {
                NameTextField(
                    textString = stringResource(id = R.string.insert_store_store_name),
                    outputString = state.storeName,
                    paddingTopValue = 24.dp
                )
            }

            item {
                NameTextField(
                    textString = stringResource(id = R.string.insert_store_store_address),
                    outputString = state.storeAddress,
                    paddingTopValue = 24.dp
                )
            }

            item {
                NameTextField(
                    textString = stringResource(id = R.string.calling_number),
                    outputString = state.storePhoneNumber,
                    paddingTopValue = 24.dp
                )
            }

            item {
                NameTextField(
                    textString = stringResource(id = R.string.delivery_fee),
                    outputString = state.storeDeliveryFee,
                    paddingTopValue = 24.dp
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.operating_time),
                        fontSize = 14.sp,
                        color = ColorActiveButton,
                        fontWeight = FontWeight.Bold
                    )

                    Column(
                        modifier = Modifier
                            .padding(start = 30.dp)
                    ) {
                        state.operatingTimeList.forEach { item ->
                            Text(
                                text = if (item.closed) stringResource(
                                    id = R.string.insert_store_closed_day,
                                    item.dayOfWeek
                                )
                                else stringResource(
                                    id = R.string.insert_store_operating_time,
                                    item.dayOfWeek,
                                    item.openTime,
                                    item.closeTime
                                )
                            )
                        }
                    }
                }
            }

            item {
                NameTextField(
                    textString = stringResource(id = R.string.other_info),
                    outputString = state.storeOtherInfo,
                    paddingTopValue = 24.dp
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .padding(horizontal = 32.dp)
                ) {
                    CreateOptionCheckBox(
                        stringResource(id = R.string.delivery_available),
                        state.isDeliveryOk
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    CreateOptionCheckBox(
                        stringResource(id = R.string.card_available),
                        state.isCardOk
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    CreateOptionCheckBox(
                        stringResource(id = R.string.account_transfer_avilable),
                        state.isBankOk
                    )
                }
            }

            item {
                Button(
                    onClick = navigateToFinishScreen,
                    colors = ButtonDefaults.buttonColors(ColorPrimary),
                    shape = RectangleShape,
                    modifier = Modifier
                        .padding(top = 57.dp, start = 240.dp, end = 16.dp, bottom = 20.dp)
                        .height(38.dp)
                        .width(105.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.next),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }

@Composable
fun NameTextField(
    modifier: Modifier = Modifier,
    textString: String = "",
    outputString: String = "",
    paddingTopValue: Dp = 10.dp
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .padding(top = paddingTopValue),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = textString,
            fontSize = 14.sp,
            color = ColorActiveButton,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier.padding(start = 26.dp),
            text = outputString,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CreateOptionCheckBox(
    checkString: String = "",
    checkValue: Boolean = true,
){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = if(checkValue) painterResource(R.drawable.ic_insert_store_checked_box)
            else painterResource(id = R.drawable.ic_insert_store_unchecked_box),
            contentDescription = "checkBox")

        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = checkString,
            color = if(checkValue) ColorSecondary else ColorMinor,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun HandleSideEffects(viewModel: FinalCheckStoreScreenViewModel, navigateToFinishScreen: () -> Unit) {

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is FinalCheckStoreScreenSideEffect.GoToFinishScreen -> navigateToFinishScreen()
            is FinalCheckStoreScreenSideEffect.FailRegisterStore -> ToastUtil.getInstance().makeShort(R.string.insert_store_fail_register_store)
        }
    }
}


@Preview
@Composable
fun PreviewStartInsertScreen(){
    FinalCheckStoreScreenImpl(
        modifier = Modifier
    )
}