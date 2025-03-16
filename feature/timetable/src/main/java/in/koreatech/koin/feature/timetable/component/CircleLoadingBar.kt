package `in`.koreatech.koin.feature.timetable.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun CircleLoadingBar(
    loading: Boolean,
    modifier: Modifier = Modifier
) {
    if (loading.not()) return

    Box(
        modifier =
        modifier
            .background(Color(0x80FFFFFF))
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = KoinTheme.colors.primary500,
            trackColor = Color.LightGray
        )
    }
}

@Preview
@Composable
private fun CircleLoadingBarPreview() {
    KoinTheme {
        CircleLoadingBar(loading = true)
    }
}
