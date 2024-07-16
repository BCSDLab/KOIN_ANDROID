package `in`.koreatech.business.feature.store.modifyinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailViewModel
import `in`.koreatech.business.ui.theme.ColorMinor
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondary
import `in`.koreatech.business.ui.theme.Gray2
import `in`.koreatech.business.ui.theme.Gray6
import `in`.koreatech.business.ui.theme.Gray9
import `in`.koreatech.koin.domain.util.DateFormatUtil.dayOfWeekToIndex
import `in`.koreatech.koin.domain.util.StoreUtil
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ModifyInfoScreen(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    viewModel: ModifyInfoViewModel = hiltViewModel(),
    storeInfoViewModel: MyStoreDetailViewModel = hiltViewModel(),
    onSettingOperatingClicked: () -> Unit = {},
) {
    val state = viewModel.collectAsState().value
    val storeInfoState = storeInfoViewModel.collectAsState().value
    val listState = rememberLazyListState()
    val context = LocalContext.current

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorPrimary),
        ) {
            IconButton(onClick = { viewModel.onBackButtonClicked() }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_flyer_before_arrow),
                    contentDescription = stringResource(R.string.back),
                    colorFilter = ColorFilter.tint(Color.White),
                )
            }
            Text(
                text = stringResource(id = R.string.my_shop),
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(color = Color.White, fontSize = 18.sp),
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.Top,
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Gray2),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        modifier = Modifier.height(255.dp),
                        painter = state.storeInfo?.imageUrls?.getOrNull(0)
                            .let { painterResource(id = R.drawable.no_image) },
                        contentDescription = stringResource(R.string.shop_image),
                        contentScale = ContentScale.Crop,
                    )
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .width(100.dp)
                            .height(50.dp)
                            .padding(bottom = 15.dp, end = 15.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.Black.copy(alpha = 0.5f),
                        ),
                        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxSize(),
                            text = stringResource(R.string.change_image),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            item {
                InputStoreInfo(
                    info = stringResource(R.string.shop_name),
                    data = state.storeInfo?.name ?: "",
                    onValueChange = { viewModel.onStoreNameChanged(it) }
                )
            }
            item {
                InputStoreInfo(
                    info = stringResource(R.string.telephone_number),
                    data = state.storeInfo?.phone ?: "",
                    onValueChange = { viewModel.onPhoneNumberChanged(it) }
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.operating_time),
                        style = TextStyle(
                            color = ColorPrimary,
                            fontWeight = FontWeight(500),
                            fontSize = 15.sp
                        ),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column {
                            state.storeInfo?.operatingTime?.forEach { item ->
                                val dayOfWeekIndex = dayOfWeekToIndex(item.dayOfWeek)
                                val dayOfWeekKorean =
                                    if (dayOfWeekIndex != -1) context.resources.getStringArray(R.array.days_one_letter)[dayOfWeekIndex] else item.dayOfWeek
                                Text(
                                    text = if (item.closed) stringResource(
                                        id = R.string.insert_store_closed_day,
                                        dayOfWeekKorean
                                    )
                                    else "$dayOfWeekKorean " +
                                            StoreUtil.generateOpenCloseTimeString(
                                                item.openTime,
                                                item.closeTime
                                            ),
                                    color = ColorMinor,
                                )
                            }
                        }
                        Button(
                            modifier = Modifier
                                .width(70.dp)
                                .height(40.dp),
                            onClick = viewModel::onSettingOperatingTimeClicked
                        ) {
                            Text(
                                modifier = Modifier.fillMaxSize(),
                                text = stringResource(R.string.modify),
                                color = Color.White,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
            item {
                InputStoreInfo(
                    info = stringResource(R.string.address),
                    data = state.storeInfo?.address ?: "",
                    onValueChange = { viewModel.onAddressChanged(it) }
                )
            }
            item {
                InputStoreInfo(
                    info = stringResource(R.string.delivery_fee),
                    data = state.storeInfo?.deliveryPrice?.toString() ?: "",
                    onValueChange = { viewModel.onDeliveryPriceChanged(it.toInt()) }
                )
            }

            item {
                InputStoreInfo(
                    info = stringResource(R.string.other_info),
                    data = state.storeInfo?.description ?: "",
                    onValueChange = { viewModel.onDescriptionChanged(it) }
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    AvailableRadioButton(
                        text = stringResource(id = R.string.delivery_available),
                        selected = state.storeInfo?.isDeliveryOk ?: false,
                        onClick = viewModel::onDeliveryAvailableChanged
                    )
                    AvailableRadioButton(
                        text = stringResource(id = R.string.card_payment_available),
                        selected = state.storeInfo?.isCardOk ?: false,
                        onClick = viewModel::onCardAvailableChanged
                    )
                    AvailableRadioButton(
                        text = stringResource(id = R.string.bank_transfer_available),
                        selected = state.storeInfo?.isBankOk ?: false,
                        onClick = viewModel::onTransferAvailableChanged
                    )
                }
            }
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            viewModel.modifyStoreInfo(
                                storeInfoState.storeId,
                                state.storeInfo ?: return@Button
                            )
                            viewModel.onBackButtonClicked()
                        },
                        modifier = Modifier
                            .width(130.dp)
                            .height(40.dp)
                            .align(Alignment.CenterEnd)
                            .padding(horizontal = 20.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = ColorPrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.save),
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 15.sp
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    viewModel.collectSideEffect {
        when (it) {
            ModifyInfoSideEffect.NavigateToBackScreen -> onBackClicked()
            ModifyInfoSideEffect.NavigateToSettingOperatingTime -> onSettingOperatingClicked()
            else -> {}
        }
    }
}

@Composable
fun InputStoreInfo(
    info: String, data: String, onValueChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = info,
            style = TextStyle(
                color = ColorPrimary,
                fontWeight = FontWeight(500),
                fontSize = 15.sp
            ),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(start = 20.dp)
                .border(0.5.dp, Gray9, RectangleShape),
            contentAlignment = Alignment.CenterEnd,
        ) {
            BasicTextField(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                value = data,
                onValueChange = onValueChange,
                textStyle = TextStyle(color = Color.Black, fontSize = 15.sp)
            )
        }
    }
}

@Composable
fun AvailableRadioButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = { onClick() },
            colors = RadioButtonDefaults.colors(
                ColorSecondary, Gray6
            )
        )
        Text(
            text = text,
            style = TextStyle(
                color = if (selected) ColorSecondary else Gray6,
                fontSize = 15.sp
            ),
        )
    }

}