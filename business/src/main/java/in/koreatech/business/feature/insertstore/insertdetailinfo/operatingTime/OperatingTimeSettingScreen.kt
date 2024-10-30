package `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.Hours
import com.chargemap.compose.numberpicker.HoursNumberPicker
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.InsertDetailInfoScreenViewModel
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.dialog.OperatingTimeDialog
import `in`.koreatech.business.ui.theme.ColorMinor
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.ColorSecondaryText
import `in`.koreatech.business.ui.theme.ColorTextBackgrond
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.domain.model.owner.SettingTime
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun OperatingTimeSettingScreen(
    onBackPressed: () -> Unit = {},
    viewModel: InsertDetailInfoScreenViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value
    OperatingTimeSettingScreenImpl(
        showDialog = state.showDialog,
        isOpenTimeSetting = state.isOpenTimeSetting,
        dayOfWeekIndex = state.dayOfWeekIndex,
        onShowOpenTimeDialog = {
            viewModel.showOpenTimeDialog(it)
        },
        onShowCloseTimeDialog = {
            viewModel.showCloseTimeDialog(it)
        },
        onCloseDialog = {
            viewModel.closeDialog()
        },
        onCheckBoxClicked = {
            viewModel.isClosedDay(it)
        },
        onBackPressed = onBackPressed,
        onSettingStoreOpenTime = {
            viewModel.settingStoreOpenTime(it.first, it.second)
        },
        onSettingStoreCloseTime = {
            viewModel.settingStoreCloseTime(it.first, it.second)
        },
        nextButtonClicked = onBackPressed,
        operatingTimeList = state.operatingTimeList
    )

}


@Composable
fun OperatingTimeSettingScreenImpl(
    showDialog: Boolean = false,
    isOpenTimeSetting: SettingTime = SettingTime.OPEN,
    dayOfWeekIndex: Int = 0,
    onShowOpenTimeDialog: (Int) -> Unit = {},
    onShowCloseTimeDialog: (Int) -> Unit = {},
    onCloseDialog: () -> Unit = {},
    onCheckBoxClicked: (Int) -> Unit = {},
    onBackPressed: () -> Unit = {},
    onSettingStoreOpenTime: (Pair<Hours, Int>) -> Unit = {},
    onSettingStoreCloseTime: (Pair<Hours, Int>) -> Unit = {},
    nextButtonClicked: () -> Unit = {},
    operatingTimeList: List<OperatingTimeState> = emptyList()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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
                modifier = Modifier
                    .size(40.dp)
            )
        }

        Text(
            modifier = Modifier.padding(top = 35.dp, start = 40.dp),
            text = stringResource(id = R.string.insert_store),
            fontSize = 24.sp
        )

        Text(
            modifier = Modifier.padding(top = 34.dp, start = 40.dp),
            text = stringResource(id = R.string.insert_store_time_setting),
            fontSize = 18.sp
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(35.dp)
                .background(ColorTextBackgrond),
            contentAlignment = Alignment.CenterStart
        ){
            Text(
                modifier = Modifier
                    .padding(start = 16.dp),
                text = stringResource(id = R.string.insert_store_open_time_setting),
                color = ColorSecondaryText
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .padding(horizontal = 24.dp)
        ){
            Text(
                text = stringResource(id = R.string.day_of_week),
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.open_time),
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.day_off),
                fontSize = 18.sp
            )
        }
        Box {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp)
            ) {
                itemsIndexed(operatingTimeList) { index, item ->
                    DayOperatingTimeSetting(item, onShowOpenTimeDialog, onShowCloseTimeDialog, index, onCheckBoxClicked)
                }
            }
            when(isOpenTimeSetting){
                SettingTime.OPEN -> OperatingTimeSettingDialog(
                    title = stringResource(id = R.string.store_open_time),
                    operatingTimeDialog = OperatingTimeDialog(
                        showDialog,
                        onCloseDialog,
                        dayOfWeekIndex,
                        onSettingStoreOpenTime
                    )
                )
                SettingTime.CLOSE -> OperatingTimeSettingDialog(
                    title = stringResource(id = R.string.store_close_time),
                    operatingTimeDialog = OperatingTimeDialog(
                        showDialog,
                        onCloseDialog,
                        dayOfWeekIndex,
                        onSettingStoreCloseTime
                    )
                )
            }
        }

        Button(
            onClick = {
                nextButtonClicked()
            },
            colors = ButtonDefaults.buttonColors(ColorPrimary),
            shape = RectangleShape,
            modifier = Modifier
                .padding(top = 57.dp, start = 240.dp, end = 16.dp)
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

@Composable
fun DayOperatingTimeSetting(
    operatingTime: OperatingTimeState = OperatingTimeState(),
    onShowOpenTimeDialog: (Int) -> Unit = {},
    onShowCloseTimeDialog: (Int) -> Unit = {},
    index: Int = 0,
    onCheckBoxClicked: (Int) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text =  operatingTime.dayOfWeek,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier.clickable {
                if(!operatingTime.closed) onShowOpenTimeDialog(index)
            },
            text = operatingTime.openTime,
            color =if (operatingTime.closed) ColorMinor else Color.Black,
            fontSize = 15.sp
        )

        Text(
            modifier = Modifier.padding(horizontal =  15.dp),
            text = " ~ ",
            fontSize = 15.sp
        )

        Text(
            modifier = Modifier.clickable {
               if(!operatingTime.closed) onShowCloseTimeDialog(index)
            },
            text = operatingTime.closeTime,
            color =if (operatingTime.closed) ColorMinor else Color.Black,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = Modifier.clickable {
                onCheckBoxClicked(index)
            },
            painter = if(operatingTime.closed) painterResource(R.drawable.ic_insert_store_time_setting_checked)
            else painterResource(id = R.drawable.ic_insert_store_time_setting_unchecked),
            contentDescription = "checkBox")
    }
}

@Composable
fun OperatingTimeSettingDialog(
    title: String = "",
    operatingTimeDialog: OperatingTimeDialog = OperatingTimeDialog()
) {
    var timeValue by remember { mutableStateOf<Hours>(FullHours(0, 0)) }
    if (operatingTimeDialog.showDialog) {
        Dialog(onDismissRequest = { operatingTimeDialog.closeDialog() }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = title,
                        style = MaterialTheme.typography.h6,
                        fontSize = 20.sp
                    )

                    HoursNumberPicker(
                        modifier = Modifier
                            .height(120.dp),
                        dividersColor = MaterialTheme.colors.primary,
                        leadingZero = true,
                        value = timeValue,
                        onValueChange = {
                            timeValue = it
                        },
                        minutesRange = (0..59 step 5),
                        hoursDivider = {
                            Text(
                                modifier = Modifier.size(24.dp),
                                textAlign = TextAlign.Center,
                                text = ":"
                            )
                        }
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                    ) {
                        Button(
                            modifier = Modifier.padding(end = 5.dp),
                            onClick = {
                                operatingTimeDialog.closeDialog()
                            }) {
                            Text(stringResource(id = R.string.cancel))
                        }
                        Button(
                            onClick = {
                                operatingTimeDialog.closeDialog()
                                operatingTimeDialog.onSettingStoreTime(Pair(timeValue, operatingTimeDialog.dayOfWeekIndex))
                            }) {
                            Text(stringResource(id = R.string.positive))
                        }
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun PreviewDialog() {
    OperatingTimeSettingDialog()
}

@Preview
@Composable
fun PreviewOperatingTimeSettingScreenImpl() {
    OperatingTimeSettingScreenImpl(
        operatingTimeList = operatingTimeList
    )
}

val operatingTimeList: List<OperatingTimeState> = listOf(
    OperatingTimeState("00:00", false, "월", "00:00"),
    OperatingTimeState("00:00", false, "화", "00:00"),
    OperatingTimeState("00:00", false, "수", "00:00"),
    OperatingTimeState("00:00", false, "목", "00:00"),
    OperatingTimeState("00:00", false, "금", "00:00"),
    OperatingTimeState("00:00", false, "토", "00:00"),
    OperatingTimeState("00:00", false, "일", "00:00"),
)
