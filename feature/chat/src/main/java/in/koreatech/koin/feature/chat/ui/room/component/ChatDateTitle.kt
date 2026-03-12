package `in`.koreatech.koin.feature.chat.ui.room.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object ChatDateTitleDefaults {
    @Composable
    fun colors(
        containerColor: Color = KoinTheme.colors.neutral200.copy(alpha = 0.8f),
        contentColor: Color = KoinTheme.colors.primary600
    ): ChatDateTitleColors = ChatDateTitleColors(
        containerColor = containerColor,
        contentColor = contentColor
    )

    @Composable
    fun purpleColors(
        containerColor: Color = RebrandKoinTheme.colors.neutral100,
        contentColor: Color = RebrandKoinTheme.colors.primary600
    ): ChatDateTitleColors = ChatDateTitleColors(
        containerColor = containerColor,
        contentColor = contentColor
    )
}

@Immutable
class ChatDateTitleColors(
    val containerColor: Color,
    val contentColor: Color
)

@Composable
fun ChatDateTitle(
    date: LocalDate,
    modifier: Modifier = Modifier,
    colors: ChatDateTitleColors = ChatDateTitleDefaults.colors()
) {
    ChatDateTitle(
        text = date.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")),
        modifier = modifier,
        colors = colors
    )
}

@Composable
fun ChatDateTitle(
    text: String,
    modifier: Modifier = Modifier,
    colors: ChatDateTitleColors = ChatDateTitleDefaults.colors()
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .background(
                    color = colors.containerColor,
                    shape = RebrandKoinTheme.shapes.medium
                )
                .padding(vertical = 4.dp, horizontal = 12.dp),
            text = text,
            style = RebrandKoinTheme.typography.medium12,
            color = colors.contentColor
        )
    }
}

@Preview
@Composable
private fun ChatDateTitlePreview() {
    ChatDateTitle(date = LocalDate.now())
}
