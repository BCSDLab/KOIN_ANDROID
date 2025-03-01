package `in`.koreatech.business.ui.component

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.core.R
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.Gray3
import `in`.koreatech.business.ui.theme.Red2
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.TimeSettingState
import `in`.koreatech.business.ui.component.button.SettingTimeButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CheckSettingTime(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    sheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        ),
    settingTimeList: List<TimeSettingState> = emptyList(),
    emptySpaceList: List<String> = emptyList(),
    removeTimeSetting: (Int)-> Unit = {},
    onChangeSettingTimeList: () -> Unit = {},
    updateIsSettingScreenState: (Boolean) -> Unit = {},
) {

    Divider(
        modifier = Modifier
            .fillMaxWidth(),
        color = Gray3,
        thickness = 0.5.dp
    )

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(settingTimeList){ index, item ->
            TimeItem(
                modifier = Modifier.clickable {
                    removeTimeSetting(index)
                },
                timeString = item.timeInfoString
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = Gray3,
                thickness = 0.5.dp
            )
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(emptySpaceList){index, item ->
            if(index != emptySpaceList.lastIndex){
                TimeItem(
                    timeString = item
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Gray3,
                    thickness = 0.5.dp
                )
            }
        }
    }

    if(emptySpaceList.isNotEmpty()){
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .clickable {
                    updateIsSettingScreenState(true)
                }
            ,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = stringResource(R.string.add_setting_time),
                style = KoinTheme.typography.medium16,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(R.drawable.fi_plus),
                contentDescription = stringResource(R.string.add_setting_time_plus)
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth(),
            color = Gray3,
            thickness = 0.5.dp
        )
    }
    SettingTimeButton(
        modifier = Modifier,
        onCancelButtonClicked = {
            updateIsSettingScreenState(false)
            coroutineScope.launch {
                sheetState.hide()
            }
        },
        onRegisterButtonClicked = {
            coroutineScope.launch {
                sheetState.hide()
            }
            onChangeSettingTimeList()
        }
    )
}

@Composable
fun TimeItem(
    modifier: Modifier = Modifier,
    timeString: String = "월, 화, 수, 목, 금 : 06:00 ~ 23:00"
) {
    Row(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxWidth()
            .height(25.dp)
        ,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        if(timeString.isNotBlank()){
            val stringList = timeString.split(" : ")
            val coloredString = buildAnnotatedString {
                append(stringList[0] + " : ")
                withStyle(style = SpanStyle(color = if (stringList[1] == "휴무") Red2 else ColorPrimary)){
                    append(stringList[1])
                }
            }

            Text(
                text = coloredString,
                style = KoinTheme.typography.medium16,
            )

            Image(
                painter = painterResource(R.drawable.ic_x),
                contentDescription = "아이템 삭제"
            )
        }
    }
}

@Preview
@Composable
fun PreviewCheckSettingTime() {
    CheckSettingTime(

    )
}

@Preview
@Composable
fun PreviewTimeItem() {
    TimeItem()
}