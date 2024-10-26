package `in`.koreatech.koin.feature.timetable.view

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TimetableScreen(
    modifier: Modifier = Modifier
) {
    Row {
        Text(text = "hi")
    }
}

@Preview
@Composable
private fun TimetableScreenPreview() {
    TimetableScreen()
}