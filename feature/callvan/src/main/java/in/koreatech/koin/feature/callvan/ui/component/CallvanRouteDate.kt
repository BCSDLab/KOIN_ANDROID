package `in`.koreatech.koin.feature.callvan.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun CallvanDateText(
    date: String,
    dayOfWeek: String,
    time: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = "$date ($dayOfWeek) $time",
        style = KoinTheme.typography.regular12,
        color = KoinTheme.colors.neutral600,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun CallvanDateTextPreview() {
    CallvanDateText(
        date = "02.05",
        dayOfWeek = "월",
        time = "14:00"
    )
}