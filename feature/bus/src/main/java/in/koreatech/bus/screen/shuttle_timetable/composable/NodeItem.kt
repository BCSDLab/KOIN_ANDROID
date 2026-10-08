package `in`.koreatech.bus.screen.shuttle_timetable.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
internal fun NodeItem(
    nodeTitle: String,
    modifier: Modifier = Modifier,
    nodeDescription: String = ""
) {
    Column(
        modifier = modifier,
        verticalArrangement = if (nodeDescription.isBlank()) Arrangement.Center else Arrangement.Top
    ) {
        Text(
            text = nodeTitle,
            style = RebrandKoinTheme.typography.medium15
        )
        if (nodeDescription.isNotBlank()) {
            Text(
                text = nodeDescription,
                style = RebrandKoinTheme.typography.regular12,
                color = RebrandKoinTheme.colors.neutral500
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun NodeItemPreview() {
    NodeItem(
        nodeTitle = "천안역",
        nodeDescription = "학화호두과자 앞"
    )
}

@Composable
@Preview(showBackground = true)
private fun NodeItemNoDescriptionPreview() {
    NodeItem(
        nodeTitle = "천안역",
        nodeDescription = ""
    )
}
