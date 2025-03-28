package `in`.koreatech.business.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.business.ui.theme.ColorCategory
import `in`.koreatech.business.ui.theme.Danger700
import `in`.koreatech.business.ui.theme.Info700

@Composable
fun DayCheckBox(
    modifier: Modifier = Modifier,
    dayName: String = "월",
    isChecked: Boolean
) {
    val textColor =
        when (dayName) {
            "일" -> Danger700
            "토" -> Info700
            else -> Color.Black
        }

    Box(
        modifier =
        modifier
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                clip = false
            )
            .background(
                color = if (isChecked) ColorCategory else Color.White,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = dayName,
            style =
            TextStyle(
                color = if (isChecked) Color.White else textColor,
                fontSize = 14.sp
            )
        )
    }
}

@Preview
@Composable
fun PreviewDayCheckBox(modifier: Modifier = Modifier) {
    Row {
        DayCheckBox(
            isChecked = true
        )
        Spacer(modifier = Modifier.width(20.dp))
        DayCheckBox(
            isChecked = false
        )
    }
}
