package `in`.koreatech.koin.ui.timetablev2.widget.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import `in`.koreatech.koin.R
import `in`.koreatech.koin.ui.timetablev2.widget.RefreshAction

@Composable
fun TimetableWidgetHeader(
    modifier: GlanceModifier = GlanceModifier
) {
    val headerTitle = listOf("", "월", "화", "수", "목", "금")
    Row(
        modifier = modifier
    ) {
        headerTitle.forEachIndexed { index, title ->
            Box(
                modifier = GlanceModifier
                    .defaultWeight()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                if (title.isEmpty()) {
                    Image(
                        provider = ImageProvider(R.drawable.icon_refresh),
                        contentDescription = null,
                        modifier = GlanceModifier.clickable(onClick = actionRunCallback<RefreshAction>())
                    )
                } else {
                    Text(
                        text = title,
                        style = TextStyle(
                            color = ColorProvider(Color.Black),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun TimetableWidgetHeaderPreview() {
    TimetableWidgetHeader(
        modifier = GlanceModifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.Transparent)
    )
}