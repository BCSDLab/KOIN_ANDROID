package `in`.koreatech.business.feature.insertstore.insertdetailinfo

import android.app.AlertDialog
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.chargemap.compose.numberpicker.AMPMHours
import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.Hours
import com.chargemap.compose.numberpicker.HoursNumberPicker
import `in`.koreatech.business.ui.theme.ColorSecondaryText
import `in`.koreatech.business.ui.theme.ColorTextBackgrond
import `in`.koreatech.koin.core.R
import org.orbitmvi.orbit.compose.collectAsState
import com.chargemap.compose.numberpicker.NumberPicker
import `in`.koreatech.business.ui.theme.ColorDisabledButton
import `in`.koreatech.business.ui.theme.ColorPrimary
import java.time.DayOfWeek

@Composable
fun OperatingTimeSettingScreen(
    onBackPressed: () -> Unit = {},
    viewModel:InsertDetailInfoScreenViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value
    OperatingTimeSettingScreenImpl(
        showDialog = state.showDialog,
        isOpenTimeSetting = state.isOpenTimeSetting,
        dayOfWeekIndex = state.dayOfWeekIndex,
        onShowDialog = {
            viewModel.showDialog(it.first, it.second)
        },
        onCloseDialog = {
            viewModel.closeDialog()
        },
        onCheckBoxClicked = {
            viewModel.isClosedDay(it)
        },
        onBackPressed = onBackPressed,
        onSettingStoreTime = {
            viewModel.settingStoreTime(it.first, it.second, it.third)
        },
        nextButtonClicked = onBackPressed,
        operatingTimeList = state.operatingTimeList
    )

}


@Composable
fun OperatingTimeSettingScreenImpl(
    showDialog: Boolean = false,
    isOpenTimeSetting: Boolean = false,
    dayOfWeekIndex: Int = 0,
    onShowDialog: (Pair<Boolean, Int>) -> Unit = {},
    onCloseDialog: () -> Unit = {},
    onCheckBoxClicked: (Int) -> Unit = {},
    onBackPressed: () -> Unit = {},
    onSettingStoreTime: (Triple<Hours, Int, Boolean>) -> Unit = {},
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
        Box(

        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp)
            ) {
                itemsIndexed(operatingTimeList) { index, item ->
                    DayOperatingTImeSetting(item, onShowDialog, index, onCheckBoxClicked)

                }
            }
            ShowAlertDialog(
                showDialog,
                onCloseDialog,
                isOpenTimeSetting,
                dayOfWeekIndex,
                onSettingStoreTime
            )
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
fun DayOperatingTImeSetting(
    operatingTime: OperatingTimeState = OperatingTimeState("00:00", false, "월", "00:00"),
    onShowDialog: (Pair<Boolean, Int>) -> Unit = {},
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
                if(!operatingTime.closed) onShowDialog(true to index)
            },
            text = operatingTime.openTime,
            fontSize = 15.sp
        )

        Text(
            modifier = Modifier.padding(horizontal =  15.dp),
            text = " ~ ",
            fontSize = 15.sp
        )

        Text(
            modifier = Modifier.clickable {
               if(!operatingTime.closed) onShowDialog(false to index)
            },
            text = operatingTime.closeTime,
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
fun ShowAlertDialog(
    showDialog: Boolean = true,
    closeDialog: () -> Unit = {},
    isOpenTimeSetting: Boolean = false,
    dayOfWeekIndex: Int = 0,
    onSettingStoreTime: (Triple<Hours, Int, Boolean>) -> Unit = {}
) {
    var pickerValue by remember { mutableStateOf<Hours>(FullHours(0, 0)) }
    if (showDialog) {
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
            ,
            onDismissRequest = { closeDialog() },
            title = {
                Text(
                    modifier = Modifier.padding(bottom = 30.dp),
                    text = if (isOpenTimeSetting) stringResource(id = R.string.store_open_time)
                    else stringResource(id = R.string.store_close_time)
                )
            },
            text = {
                HoursNumberPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    dividersColor = MaterialTheme.colors.primary,
                    leadingZero = false,
                    value = pickerValue,
                    onValueChange = {
                        pickerValue = it
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
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSettingStoreTime(Triple(pickerValue, dayOfWeekIndex, isOpenTimeSetting))
                    }) {
                    Text(stringResource(id = R.string.positive))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        closeDialog()
                    }) {
                    Text(stringResource(id = R.string.neutral))
                }
            }
        )
    }
}

@Preview
@Composable
fun PreviewDayOperatingTImeSetting() {
    DayOperatingTImeSetting()
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
