package `in`.koreatech.business.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.R
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.TimeSettingState
import `in`.koreatech.koin.core.designsystem.component.dialog.MessageDialog
import kotlinx.coroutines.CoroutineScope

@Composable
fun SettingTimeDialog(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    sheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        ),
    onChangeSettingTimeList: (List<TimeSettingState>) -> Unit = {}
) {
    var isSettingScreen by remember { mutableStateOf(false) }
    val timeInfoList = remember { mutableStateListOf<TimeSettingState>() }
    val emptySpaceList = remember { mutableStateListOf("", "", "", "", "", "") }
    var isDayOfWeekListEmpty by remember { mutableStateOf(false) }

    if (isDayOfWeekListEmpty){
        MessageDialog(
            title = "요일을 선택해 주세요.",
            onPositive = {
                isDayOfWeekListEmpty = false
            }
        )
    }


    Column(
        modifier = modifier
            .fillMaxSize()

    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.insert_store_operating_time_setting),
            style = KoinTheme.typography.bold16,
            textAlign = TextAlign.Center
        )

        if (isSettingScreen) {
            SettingTime(
                storeOperatingTime = dayOfWeekList,
                coroutineScope = coroutineScope,
                sheetState = sheetState,
                addTimeState = {
                    timeInfoList.add(it)
                    emptySpaceList.removeAt(emptySpaceList.lastIndex)
                },
                updateIsSettingScreenState = {
                    isSettingScreen = it
                },
                showMessageDialog = {
                    isDayOfWeekListEmpty = it
                }
            )
        } else {
            if (timeInfoList.isEmpty()) {
                NullSettingTime(
                    coroutineScope = coroutineScope,
                    sheetState = sheetState,
                    updateIsSettingScreenState = {
                        isSettingScreen = it
                    },
                )
            } else {
                CheckSettingTime(
                    settingTimeList = timeInfoList,
                    emptySpaceList = emptySpaceList,
                    coroutineScope = coroutineScope,
                    sheetState = sheetState,
                    removeTimeSetting = {
                        timeInfoList.removeAt(it)
                        emptySpaceList.add("")
                    },
                    onChangeSettingTimeList ={
                        onChangeSettingTimeList(timeInfoList)
                    },
                    updateIsSettingScreenState = {
                        isSettingScreen = it
                    },
                )
            }
        }
    }
}
@Preview
@Composable
fun PreviewSettingTimeDialog() {
    SettingTimeDialog()
}