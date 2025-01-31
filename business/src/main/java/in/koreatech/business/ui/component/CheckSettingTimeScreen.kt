package `in`.koreatech.business.ui.component

import android.util.Log
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
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.feature.insertstore.insertdetailinfo.operatingTime.OperatingTimeState
import `in`.koreatech.business.ui.theme.Black1
import `in`.koreatech.business.ui.theme.ColorPrimary
import `in`.koreatech.business.ui.theme.Gray3
import `in`.koreatech.business.ui.theme.Red2

@Composable
fun CheckSettingTime(
    modifier: Modifier = Modifier,
    settingTimeList: List<String> = emptyList()
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
            if(index != 5 || item.isNotBlank()){
                TimeItem(
                    timeString = item
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = Gray3,
                thickness = 0.5.dp
            )
        }
    }
    if(settingTimeList.last().isBlank()){
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
            ,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = "설정시간 추가",
                style = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(R.drawable.fi_plus),
                contentDescription = "플러스버튼"
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth(),
            color = Gray3,
            thickness = 0.5.dp
        )
    }
}

@Composable
fun TimeItem(
    modifier: Modifier = Modifier,
    timeString: String = "월, 화, 수, 목, 금 : 06:00 ~ 23:00"
) {
    Row(
        modifier = Modifier
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
                style = TextStyle(
                    fontSize = 16.sp
                )
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
        settingTimeList = list1
    )
}

@Preview
@Composable
fun PreviewTimeItem() {
    TimeItem()
}

val list1: List<String> = listOf(
    "토, 일 : 휴무",
    "월, 화, 수, 목, 금 : 06:00 ~ 23:00",
    "",
    "",
    "",
    ""
)

val list2: List<String> = listOf(
    "토, 일 : 휴무",
    "월 : 06:00 ~ 23:00",
    "화 : 06:00 ~ 23:00",
    "수 : 06:00 ~ 23:00",
    "목 : 06:00 ~ 23:00",
    "금 : 06:00 ~ 23:00"
)