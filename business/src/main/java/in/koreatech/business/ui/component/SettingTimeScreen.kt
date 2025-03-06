package `in`.koreatech.business.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.KorDayOfWeek
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.TimeSettingState
import `in`.koreatech.business.ui.component.button.SettingTimeButton
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
    storeOperatingTime: List<KorDayOfWeek> = emptyList(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    sheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
        ),
    addTimeState: (TimeSettingState, List<KorDayOfWeek>) -> Unit = {t, s ->},
    updateIsSettingScreenState: (Boolean) -> Unit = {},
    showMessageDialog:(Boolean, String) -> Unit = {b,s ->},
    registeredDayOfWeekList: List<KorDayOfWeek> = emptyList()
) {
    val dayOfWeekList = remember { mutableStateListOf<KorDayOfWeek>() }
    var openTimeValue by remember { mutableStateOf<Hours>(FullHours(6, 0)) }
    var closeTimeValue by remember { mutableStateOf<Hours>(FullHours(0, 0)) }
    var isClosedChecked by remember { mutableStateOf(false) }
    var is24hoursChecked by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
    ) {
        Divider(
            modifier =
                Modifier
                    .fillMaxWidth(),
            color = Gray3,
            thickness = 0.5.dp,
        )

        LazyRow(
            modifier =
                Modifier
                    .padding(horizontal = 31.dp, vertical = 8.dp)
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            itemsIndexed(storeOperatingTime) { index, item ->
                DayCheckBox(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable{
                            if(registeredDayOfWeekList.contains(item)){
                                showMessageDialog(true, "이미 설정한 요일입니다.")
                            }
                            else{
                                if (dayOfWeekList.contains(item)) dayOfWeekList.remove(item)
                                else dayOfWeekList.add(item)
                            }
                        }
                    ,
                    dayName = item.kor,
                    isChecked = dayOfWeekList.contains(item),
                )
            }
        }

        Divider(
            modifier =
                Modifier
                    .fillMaxWidth(),
            color = Gray3,
            thickness = 0.5.dp,
        )

        Row(
            modifier =
                Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
                    .height(120.dp),
        ) {
            Text(
                modifier =
                    Modifier
                        .padding(start = 55.dp)
                        .align(Alignment.CenterVertically),
                text = stringResource(R.string.store_open_time),
                style =
                    TextStyle(
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                    ),
            )

            HoursNumberPicker(
                modifier =
                    Modifier
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
                        text = ":",
                    )
                },
            )
        }

        Divider(
            modifier =
                Modifier
                    .fillMaxWidth(),
            color = Gray3,
            thickness = 0.5.dp,
        )

        Row(
            modifier =
                Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth()
                    .height(120.dp),
        ) {
            Text(
                modifier =
                    Modifier
                        .padding(start = 55.dp)
                        .align(Alignment.CenterVertically),
                text = stringResource(R.string.store_close_time),
                style =
                    TextStyle(
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                    ),
            )

            HoursNumberPicker(
                modifier =
                    Modifier
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
                        text = ":",
                    )
                },
            )
        }

        Divider(
            modifier =
                Modifier
                    .fillMaxWidth(),
            color = Gray3,
            thickness = 0.5.dp,
        )

        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier =
                    Modifier
                        .padding(end = 56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    "휴무",
                )
                Checkbox(
                    checked = isClosedChecked,
                    onCheckedChange = { isClosedChecked = it },
                    colors =
                        CheckboxDefaults.colors(
                            checkedColor = ColorPrimary,
                            uncheckedColor = Gray3,
                        ),
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    "24시간",
                )
                Checkbox(
                    checked = is24hoursChecked,
                    onCheckedChange = { is24hoursChecked = it },
                    colors =
                        CheckboxDefaults.colors(
                            checkedColor = ColorPrimary,
                            uncheckedColor = Gray3,
                        ),
                )
            }
        }

        Divider(
            modifier =
                Modifier
                    .fillMaxWidth(),
            color = Gray3,
            thickness = 0.5.dp,
        )

        SettingTimeButton(
            modifier = modifier,
            onCancelButtonClicked = {
                coroutineScope.launch {
                    sheetState.hide()
                }
                updateIsSettingScreenState(false)
            },
            onRegisterButtonClicked = {
                
                if(dayOfWeekList.isEmpty()){
                    showMessageDialog(true, "요일을 선택해 주세요.")
                }
                else {
                    addTimeState(
                        TimeSettingState(
                            timeInfoString = makeTimeInfo(
                                dayOfWeekList = dayOfWeekList.sortedBy { it.priority },
                                openTime = openTimeValue.toTimeString(),
                                closeTime = closeTimeValue.toTimeString(),
                                isClosed = isClosedChecked,
                                is24Hours = is24hoursChecked
                            ),
                            dayOfWeekList = dayOfWeekList.sortedBy { it.priority },
                            openTime = openTimeValue.toTimeString(),
                            closeTime = closeTimeValue.toTimeString(),
                            isClosed = isClosedChecked,
                            is24Hours = is24hoursChecked
                        ),
                        dayOfWeekList
                    )

                    updateIsSettingScreenState(false)
                }
            }
        )
    }
}

@Preview
@Composable
fun PreviewSettingTime() {
    SettingTime(
        storeOperatingTime = dayOfWeekList,
    )
}

val dayOfWeekList: List<KorDayOfWeek> =
    listOf(
        KorDayOfWeek("월", "MONDAY", 1),
        KorDayOfWeek("화", "TUESDAY", 2),
        KorDayOfWeek("수", "WEDNESDAY", 3),
        KorDayOfWeek("목", "THURSDAY", 4),
        KorDayOfWeek("금", "FRIDAY", 5),
        KorDayOfWeek("토", "SATURDAY", 6),
        KorDayOfWeek("일", "SUNDAY", 7),
    )
