package `in`.koreatech.business.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.R
import `in`.koreatech.business.ui.theme.Gray3

@Composable
fun NullSettingTime(
    modifier: Modifier = Modifier,
    updateIsSettingScreenState: (Boolean) -> Unit = {},
) {
    Divider(
        modifier = Modifier
            .fillMaxWidth(),
        color = Gray3,
        thickness = 0.5.dp
    )

    Text(
        modifier = Modifier
            .padding(vertical = 135.dp)
            .fillMaxWidth()
        ,
        text = "등록한 운영시간이 없습니다.",
        style = TextStyle(
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    )

    Divider(
        modifier = Modifier
            .fillMaxWidth(),
        color = Gray3,
        thickness = 0.5.dp
    )

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