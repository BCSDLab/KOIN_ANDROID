package `in`.koreatech.business.feature.store.modifyinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.business.feature.store.storedetail.MyStoreDetailViewModel
import `in`.koreatech.business.ui.theme.ColorMinor
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondaryText
import `in`.koreatech.business.ui.theme.ColorTextBackgrond
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.domain.model.owner.insertstore.OperatingTime
import `in`.koreatech.koin.domain.util.DateFormatUtil
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ModifyOperatingTimeScreen(
    modifier: Modifier = Modifier,
    myStoreDetailViewModel: MyStoreDetailViewModel = hiltViewModel(),
    viewModel: ModifyInfoViewModel = hiltViewModel(),
    onBackClicked: () -> Unit = {},
) {
    val state = viewModel.collectAsState().value

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ColorPrimary),
        ) {
            IconButton(onClick = viewModel::onBackButtonClicked) {
                Image(
                    painter = painterResource(id = R.drawable.ic_flyer_before_arrow),
                    contentDescription = stringResource(R.string.back),
                    colorFilter = ColorFilter.tint(Color.White),
                )
            }
            Text(
                text = stringResource(id = R.string.operating_time),
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(color = Color.White, fontSize = 18.sp),
            )
        }

        Text(
            modifier = Modifier
                .padding(start = 16.dp, top = 20.dp),
            text = stringResource(id = R.string.insert_store_time_setting),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight(500),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(35.dp)
                .background(ColorTextBackgrond), contentAlignment = Alignment.CenterStart
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = stringResource(id = R.string.insert_store_open_time_setting),
                color = ColorSecondaryText
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = stringResource(id = R.string.day_of_week), fontSize = 18.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.open_time), fontSize = 18.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.day_off), fontSize = 18.sp
            )
        }
        Box {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp)
                    .padding(horizontal = 6.dp)
            ) {
                itemsIndexed(state.storeInfo.operatingTime) { index, item ->
                    OperatingTimeSetting(
                        state = state,
                        onShowOpenTimeDialog = {
                            viewModel.showAlertDialog(index)
                            viewModel.initDialogTimeSetting(item.openTime, item.closeTime)
                        },
                        operatingTime = item,
                        index = index,
                    ) {
                        viewModel.isClosedDay(index)
                    }
                }
            }
            OperatingTimeSettingDialog(
                onDismiss = viewModel::hideAlertDialog,
                onSettingStoreTime = viewModel::onSettingStoreTime,
                visibility = state.showDialog,
                operatingTime = state.dialogTimeState
            )
        }

    }

    viewModel.collectSideEffect {
        when (it) {
            is ModifyInfoSideEffect.NavigateToBackScreen -> {
                onBackClicked()
            }

            else -> {}
        }

    }
}

@Composable
fun OperatingTimeSetting(
    state: ModifyInfoState,
    operatingTime: OperatingTime,
    onShowOpenTimeDialog: (Int) -> Unit = {},
    index: Int = 0,
    onCheckBoxClicked: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val dayOfWeekIndex = DateFormatUtil.dayOfWeekToIndex(operatingTime.dayOfWeek)
    val dayOfWeekKorean =
        if (dayOfWeekIndex != -1) context.resources.getStringArray(R.array.days_one_letter)[dayOfWeekIndex] else operatingTime.dayOfWeek
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = dayOfWeekKorean, fontSize = 15.sp)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.clickable {
                if (!state.storeInfo.operatingTime.get(index).closed)
                    onShowOpenTimeDialog(index)
            },
            text = "${state.storeInfo.operatingTime[index].openTime} ~ ${
                state.storeInfo.operatingTime[index].closeTime
            }",
            color = if (state.storeInfo.operatingTime[index].closed) ColorMinor else Color.Black,
            fontSize = 15.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier.clickable {
                onCheckBoxClicked(index)
            },
            painter = if (state.storeInfo.operatingTime[index].closed) painterResource(
                R.drawable.ic_insert_store_time_setting_checked
            )
            else painterResource(id = R.drawable.ic_insert_store_time_setting_unchecked),
            contentDescription = "checkBox"
        )
    }
}
