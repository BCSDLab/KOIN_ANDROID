package `in`.koreatech.business.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.Hours
import com.chargemap.compose.numberpicker.HoursNumberPicker
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.OperatingTimeState
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.TimeSettingState
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.Gray3
import `in`.koreatech.business.util.ext.makeTimeInfo
import `in`.koreatech.business.util.ext.toTimeString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("RememberReturnType")
@Composable
fun SettingTime(
    modifier: Modifier = Modifier,
    storeOperatingTime: List<OperatingTimeState> = emptyList(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    sheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        ),
    addTimeState: (TimeSettingState) -> Unit = {},
    updateIsSettingScreenState: (Boolean) -> Unit = {},
) {
    val dayOfWeekList = remember { mutableStateListOf<String>() }
    var openTimeValue by remember { mutableStateOf<Hours>(FullHours(6, 0)) }
    var closeTimeValue by remember { mutableStateOf<Hours>(FullHours(0, 0)) }
    var isClosedChecked by remember { mutableStateOf(false) }
    var is24hoursChecked by remember { mutableStateOf(false) }

    Divider(
        modifier = Modifier
            .fillMaxWidth(),
        color = Gray3,
        thickness = 0.5.dp
    )

    LazyRow(
        modifier = Modifier
            .padding(horizontal = 31.dp, vertical = 8.dp)
            .fillMaxWidth()
        ,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        itemsIndexed(storeOperatingTime){ index, item ->
            DayCheckBox(
                modifier = Modifier
                    .size(40.dp)
                    .clickable{
                        if (dayOfWeekList.contains(item.dayOfWeek)) dayOfWeekList.remove(item.dayOfWeek)
                        else dayOfWeekList.add(item.dayOfWeek)
                    }
                ,
                dayName = item.dayOfWeek,
                isChecked = dayOfWeekList.contains(item.dayOfWeek)
            )
        }
    }

    Divider(
        modifier = Modifier
            .fillMaxWidth(),
        color = Gray3,
        thickness = 0.5.dp
    )

    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .height(120.dp),

        ){
        Text(
            modifier = Modifier
                .padding(start = 55.dp)
                .align(Alignment.CenterVertically)
            ,
            text = stringResource(R.string.store_open_time),
            style = TextStyle(
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        )

        HoursNumberPicker(
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .fillMaxHeight(),
            dividersColor = Gray3,
            leadingZero = true,
            value = openTimeValue,
            onValueChange = {
                openTimeValue = it
            },
            minutesRange = (0..59 step 10),
            hoursDivider = {
                Text(
                    modifier = Modifier.size(24.dp),
                    textAlign = TextAlign.Center,
                    text = ":"
                )
            }
        )
    }

    Divider(
        modifier = Modifier
            .fillMaxWidth(),
        color = Gray3,
        thickness = 0.5.dp
    )

    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .height(120.dp),

        ){
        Text(
            modifier = Modifier
                .padding(start = 55.dp)
                .align(Alignment.CenterVertically)
            ,
            text = stringResource(R.string.store_close_time),
            style = TextStyle(
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        )

        HoursNumberPicker(
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .fillMaxHeight(),
            dividersColor = Gray3,
            leadingZero = true,
            value = closeTimeValue,
            onValueChange = {
                closeTimeValue = it
            },
            minutesRange = (0..59 step 10),
            hoursDivider = {
                Text(
                    modifier = Modifier.size(24.dp),
                    textAlign = TextAlign.Center,
                    text = ":"
                )
            }
        )
    }

    Divider(
        modifier = Modifier
            .fillMaxWidth(),
        color = Gray3,
        thickness = 0.5.dp
    )

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        Row(
            modifier = Modifier
                .padding(end = 56.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "휴무"
            )
            Checkbox(
                checked = isClosedChecked,
                onCheckedChange = { isClosedChecked = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = ColorPrimary,
                    uncheckedColor = Gray3
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "24시간"
            )
            Checkbox(
                checked = is24hoursChecked,
                onCheckedChange = { is24hoursChecked = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = ColorPrimary,
                    uncheckedColor = Gray3
                )
            )
        }
    }

    Divider(
        modifier = Modifier
            .fillMaxWidth(),
        color = Gray3,
        thickness = 0.5.dp
    )

    Row(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 36.dp)
            .fillMaxWidth()
        ,
        horizontalArrangement = Arrangement.Center
    ){
        Button(
            onClick = {
                coroutineScope.launch {
                    sheetState.hide()
                }
                updateIsSettingScreenState(false)
            },
            colors = ButtonDefaults.buttonColors(Color.White),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Gray3),
            modifier = Modifier
                .height(44.dp)
                .width(128.dp)

        ) {
            Text(
                text = stringResource(id = R.string.cancel),
                style = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Gray3
                )
            )
        }

        Spacer(modifier = Modifier.width(32.dp))

        Button(
            onClick = {
                addTimeState(
                    TimeSettingState(
                        timeInfoString = makeTimeInfo(
                            dayOfWeekList = dayOfWeekList,
                            openTime = openTimeValue.toTimeString(),
                            closeTime = closeTimeValue.toTimeString(),
                            isClosed = isClosedChecked,
                            is24Hours = is24hoursChecked
                        ),
                        dayOfWeekList = dayOfWeekList,
                        openTime = openTimeValue.toTimeString(),
                        closeTime = closeTimeValue.toTimeString(),
                        isClosed = isClosedChecked,
                        is24Hours = is24hoursChecked
                    )
                )
                updateIsSettingScreenState(false)
                      },
            colors = ButtonDefaults.buttonColors(ColorPrimary),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, ColorPrimary),
            modifier = Modifier
                .height(44.dp)
                .width(128.dp)
        ) {
            Text(
                text = "추가하기",
                style = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            )
        }
    }
}


@Preview
@Composable
fun PreviewSettingTime() {
    SettingTime(
        storeOperatingTime = operatingTime
    )
}