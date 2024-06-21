package `in`.koreatech.koin.ui.timetablev2.widget.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import `in`.koreatech.koin.R
import `in`.koreatech.koin.model.timetable.TimeBlock

@Composable
fun TimetableWidgetContent(
    timeWidth: Float,
    timeBlocks: List<List<TimeBlock?>>,
    modifier: GlanceModifier = GlanceModifier
) {
    Row(
        modifier = modifier
    ) {
        if (timeBlocks.isNotEmpty()) {
            repeat(timeBlocks.size + 2) { index ->
                Column(
                    modifier = GlanceModifier
                        .width((timeWidth / 6f).dp)
                        .fillMaxHeight()
                ) {
                    for (i in 9..18) {
                        if (index == 0) {
                            Box(
                                modifier = GlanceModifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                                    .background(imageProvider = ImageProvider(R.drawable.shape_timetable_row)),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                Text(
                                    text = i.toString(),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        color = ColorProvider(Color.Gray)
                                    ),
                                    modifier = GlanceModifier
                                        .padding(top = 2.dp, end = 2.dp)
                                )
                            }
                        } else if (index in 1..5) {
                            val timeBlock = timeBlocks[index - 1][i - 9]
                            Box(
                                modifier = GlanceModifier
                                    .fillMaxWidth()
                                    .height(
                                        if (timeBlock == null) {
                                            60.dp
                                        } else {
                                            (timeBlock.duration * 60).dp
                                        }
                                    )
                                    .padding(
                                        top = ((timeBlock?.startDuration ?: 0f) * 60).dp,
                                        end = 2.dp
                                    )
                                    .background(imageProvider = ImageProvider(R.drawable.shape_timetable_row))
                            ) {
                                Column(
                                    modifier = GlanceModifier
                                        .fillMaxWidth()
                                        .height(
                                            ((timeBlock?.endDuration ?: 0f) * 60).dp
                                        )
                                        .padding(2.dp)
                                        .background(
                                            timeBlock?.color ?: Color.Transparent
                                        )
                                ) {
                                    Box(
                                        modifier = GlanceModifier
                                            .fillMaxWidth()
                                            .height(2.dp)
                                            .background(Color.White)
                                    ) {

                                    }
                                    Text(
                                        text = timeBlock?.title ?: "",
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            color = ColorProvider(Color.Black),
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = if (timeBlock?.start == null || timeBlock?.end == null) "" else "${timeBlock.start} - ${timeBlock.end}",
                                        style = TextStyle(
                                            fontSize = 8.sp,
                                            color = ColorProvider(Color.Black)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TimetableWidgetContentPreview() {
    TimetableWidgetContent(
        timeWidth = 1f,
        timeBlocks = emptyList(),
        modifier = GlanceModifier.fillMaxWidth()
    )
}