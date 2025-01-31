package `in`.koreatech.business.ui.component

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.OperatingTimeState
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.Gray3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SettingTimeDialog(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    sheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        )
)
{
    var isSettingScreen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ){
        Text(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
            ,
            text = "운영시간 설정",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )

        if(isSettingScreen) {
            NullSettingTime{isSettingScreen = it}
        }
        else {
//            NullSettingTime{isSettingScreen = it}
            CheckSettingTime(
                settingTimeList = list2
            )
        }
        Row(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 36.dp)
                .fillMaxWidth()
            ,
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {
                    isSettingScreen = false
                    coroutineScope.launch {
                        sheetState.hide()
                    }
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
                onClick = {isSettingScreen = !isSettingScreen},
                colors = ButtonDefaults.buttonColors(ColorPrimary),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, ColorPrimary),
                modifier = Modifier
                    .height(44.dp)
                    .width(128.dp)
            ) {
                Text(
                    text = if(isSettingScreen)"등록하기" else "추가하기",
                    style = TextStyle(
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
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

val operatingTime: List<OperatingTimeState> = listOf(
    OperatingTimeState("00:00", false, "일", "00:00"),
    OperatingTimeState("00:00", false, "월", "00:00"),
    OperatingTimeState("00:00", false, "화", "00:00"),
    OperatingTimeState("00:00", false, "수", "00:00"),
    OperatingTimeState("00:00", true, "목", "00:00"),
    OperatingTimeState("00:00", true, "금", "00:00"),
    OperatingTimeState("00:00", false, "토", "00:00"),
)