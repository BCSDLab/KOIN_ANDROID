package `in`.koreatech.koin.feature.dining.component.abTeset

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun DiningAbTestFloatingButton(
    contentText: String,
    buttonText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                spotColor = KoinTheme.colors.neutral800.copy(alpha = 0.04f),
                ambientColor = KoinTheme.colors.neutral800.copy(alpha = 0.04f)
            )
            .background(color = KoinTheme.colors.info100, shape = KoinTheme.shapes.small)
            .padding(start = 20.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = contentText,
            color = KoinTheme.colors.primary500,
            style = KoinTheme.typography.medium14,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Box(
            modifier = Modifier
                .clip(KoinTheme.shapes.small)
                .background(KoinTheme.colors.primary500)
                .clickable(onClick = onClick)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buttonText,
                color = KoinTheme.colors.neutral0,
                style = KoinTheme.typography.bold14
            )
        }
    }
}

@Preview
@Composable
fun DiningAbTestFloatingButtonPreview() {
    DiningAbTestFloatingButton(
        contentText = "오늘 학식 메뉴가 별로라면?",
        buttonText = "주변상점 보기",
        onClick = {}
    )
}
