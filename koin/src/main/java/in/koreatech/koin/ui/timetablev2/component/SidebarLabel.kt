package `in`.koreatech.koin.ui.timetablev2.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val hourFormatter = DateTimeFormatter.ofPattern("HH")

@Composable
fun SidebarLabel(
    time: LocalTime,
    modifier: Modifier = Modifier,
) {
    Text(
        text = time.format(hourFormatter),
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
        textAlign = TextAlign.End
    )
}

@Preview(showBackground = true)
@Composable
private fun SidebarLabelPreview() {
    SidebarLabel(
        time = LocalTime.now()
    )
}