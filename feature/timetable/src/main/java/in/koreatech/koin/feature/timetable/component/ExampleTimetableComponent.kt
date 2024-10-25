package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.theme.KoinPreview
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun ExampleTimetableComponent(
    modifier: Modifier = Modifier
) {
        MaterialTheme {
    KoinTheme {
        KoinTheme.colors.danger100

        }
    }
}

@KoinPreview
@Composable
private fun ExampleTimetableComponentPreview() {
    ExampleTimetableComponent()
}