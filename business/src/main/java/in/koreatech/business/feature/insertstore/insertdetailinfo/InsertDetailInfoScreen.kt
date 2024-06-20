package `in`.koreatech.business.feature.insertstore.insertdetailinfo


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.OperatingTimeState
import `in`.koreatech.business.feature.insertstore.selectcategory.InsertStoreProgressBar
import `in`.koreatech.business.ui.theme.ColorActiveButton
import `in`.koreatech.business.ui.theme.ColorDisabledButton
import `in`.koreatech.business.ui.theme.ColorMinor
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.core.toast.ToastUtil
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun InsertDetailInfoScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    navigateToCheckScreen: (InsertDetailInfoScreenState) -> Unit,
    reviseButtonClicked: (viewModel: InsertDetailInfoScreenViewModel) -> Unit,
    viewModel: InsertDetailInfoScreenViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value
    InsertDetailInfoScreenImpl(
        storePhoneNumber = state.storePhoneNumber,
        storeDeliveryFee = state.storeDeliveryFee,
        storeOtherInfo = state.storeOtherInfo,
        isDeliveryOk = state.isDeliveryOk,
        isCardOk = state.isCardOk,
        isBankOk = state.isBankOk,
        isDetailInfoValid = state.isDetailInfoValid,
        storeOperatingTime = state.operatingTimeList,
        onStorePhoneNumberChange = {
            viewModel.onChangePhoneNumber(it)
        },
        onStoreDeliveryFeeChange = {
            viewModel.onChangeDeliveryFee(it)
        },
        onStoreOtherInfoChange = {
            viewModel.onChangeOtherInfo(it)
        },
        onIsDeliveryOkChange = {
            viewModel.changeIsDeliveryOk()
        },
        onIsCardOkChange = {
            viewModel.changeIsCardOk()
        },
        onIsBankOkChange = {
            viewModel.changeIsBankOk()
        },
        reviseButtonClicked = {
            reviseButtonClicked(viewModel)
        },
        nextButtonClicked = {
            viewModel.onNextButtonClick()
        },
        onBackPressed = onBackPressed
    )

    HandleSideEffects(viewModel, navigateToCheckScreen)
}


@Composable
fun InsertDetailInfoScreenImpl(
    modifier: Modifier = Modifier,
    storePhoneNumber: String = "",
    storeDeliveryFee: String = "",
    storeOperatingTime: List<OperatingTimeState> = emptyList(),
    storeOtherInfo: String = "",
    isDeliveryOk: Boolean = false,
    isCardOk: Boolean = false,
    isBankOk: Boolean = false,
    isDetailInfoValid: Boolean = false,
    onStorePhoneNumberChange: (String) -> Unit = {},
    onStoreDeliveryFeeChange: (String) -> Unit = {},
    onStoreOtherInfoChange: (String) -> Unit = {},
    onIsDeliveryOkChange: () -> Unit = {},
    onIsCardOkChange: () -> Unit = {},
    onIsBankOkChange: () -> Unit = {},
    reviseButtonClicked: () -> Unit = {},
    nextButtonClicked: () -> Unit = {},
    onBackPressed: () -> Unit = {},
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
            InsertStoreProgressBar(modifier, 0.75f, R.string.insert_store_detail_info, R.string.page_three)
        }

        item {
            NameTextField(stringResource(id = R.string.phone_number), storePhoneNumber, onStorePhoneNumberChange, 32.dp)
        }

        item {
            NameTextField(stringResource(id = R.string.delivery_fee), storeDeliveryFee, onStoreDeliveryFeeChange, 24.dp)
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
                    storeOperatingTime.forEach { item ->
                        Text(
                            text = if (item.closed) stringResource(id = R.string.insert_store_closed_day, item.dayOfWeek)
                            else stringResource(id = R.string.insert_store_operating_time, item.dayOfWeek, item.openTime, item.closeTime)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = reviseButtonClicked,
                    colors = ButtonDefaults.buttonColors(ColorPrimary),
                    shape = RectangleShape,
                    modifier = Modifier
                        .height(29.dp)
                        .width(58.dp)
                    ,
                    contentPadding = PaddingValues(vertical = 4.dp, horizontal = 13.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.Center),
                        text = stringResource(id = R.string.revise),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        item {
            NameTextField(stringResource(id = R.string.other_info), storeOtherInfo, onStoreOtherInfoChange, 24.dp)
        }

        item {
            Row(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .padding(horizontal = 32.dp)
            ){
                CreateOptionCheckBox(
                    stringResource(id = R.string.delivery_available),
                    isDeliveryOk,
                    onIsDeliveryOkChange
                )

                Spacer(modifier = Modifier.weight(1f))

                CreateOptionCheckBox(
                    stringResource(id = R.string.card_available),
                    isCardOk,
                    onIsCardOkChange
                )

                Spacer(modifier = Modifier.weight(1f))

                CreateOptionCheckBox(
                    stringResource(id = R.string.account_transfer_avilable),
                    isBankOk,
                    onIsBankOkChange
                )
            }
        }

        item {
            Button(
                onClick = nextButtonClicked,
                colors = if(isDetailInfoValid)ButtonDefaults.buttonColors(ColorPrimary) else ButtonDefaults.buttonColors(ColorDisabledButton),
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
    textString: String = "",
    inputString: String = "",
    onStringChange: (String) -> Unit = {},
    paddingTopValue: Dp = 10.dp
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .padding(top = paddingTopValue),
        verticalAlignment = CenterVertically
    ){
        Text(
            text = textString,
            fontSize = 14.sp,
            color = ColorActiveButton,
            fontWeight = FontWeight.Bold
        )

        BorderTextField(inputString, onStringChange)
    }
}

@Composable
private fun HandleSideEffects(viewModel: InsertDetailInfoScreenViewModel, navigateToCheckScreen: (InsertDetailInfoScreenState) -> Unit) {
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is InsertDetailInfoScreenSideEffect.NavigateToCheckScreen -> navigateToCheckScreen(sideEffect.storeDetailInfo)
            is InsertDetailInfoScreenSideEffect.ShowMessage -> {
                val message = when (sideEffect.type) {
                    DetailInfoErrorType.NullStorePhoneNumber -> context.getString(R.string.insert_store_null_store_phone_number)
                    DetailInfoErrorType.NullStoreDeliveryFee -> context.getString(R.string.insert_store_null_store_delivery_fee)
                    DetailInfoErrorType.NullStoreOtherInfo -> context.getString(R.string.insert_store_null_store_other_info)
                }
                ToastUtil.getInstance().makeShort(message)
            }
        }
    }
}

@Composable
fun BorderTextField(
    inputString: String = "",
    onStringChange: (String) -> Unit = {},
){
    Box(
        modifier = Modifier
            .padding(start = 30.dp)
            .border(width = 1.dp, color = ColorMinor)
            .height(37.dp),
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
                .padding(start = 12.dp)
        )
    }
}

@Composable
fun CreateOptionCheckBox(
    checkString: String = "",
    checkValue: Boolean = true,
    onCheckValueChange: () -> Unit = {}
){
    Row(
        modifier = Modifier.clickable {
            onCheckValueChange()
        },
        verticalAlignment = CenterVertically
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

@Preview
@Composable
fun PreviewCreateCheckBox(){
    CreateOptionCheckBox()
}

@Preview
@Composable
fun PreviewInsertDetailInfoScreen(){
    InsertDetailInfoScreenImpl(
        storeOperatingTime = operatingTime
    )
}

val operatingTime: List<OperatingTimeState> = listOf(
    OperatingTimeState("00:00", false, "월", "00:00"),
    OperatingTimeState("00:00", false, "화", "00:00"),
    OperatingTimeState("00:00", false, "수", "00:00"),
    OperatingTimeState("00:00", true, "목", "00:00"),
    OperatingTimeState("00:00", true, "금", "00:00"),
    OperatingTimeState("00:00", false, "토", "00:00"),
    OperatingTimeState("00:00", false, "일", "00:00"),
)