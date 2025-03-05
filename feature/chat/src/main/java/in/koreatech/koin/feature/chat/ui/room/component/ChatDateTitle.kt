package `in`.koreatech.koin.feature.chat.ui.room.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ChatDateTitle(
    date: LocalDate,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier =
                Modifier
                    .background(
                        color = KoinTheme.colors.neutral200.copy(alpha = 0.8f),
                        shape = KoinTheme.shapes.medium,
                    )
                    .padding(vertical = 4.dp, horizontal = 12.dp),
            text = date.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")),
            style = KoinTheme.typography.medium12,
            color = KoinTheme.colors.primary600,
        )
    }
}

@Preview
@Composable
fun ChatDateTitlePreview() {
    ChatDateTitle(date = LocalDate.now())
}
