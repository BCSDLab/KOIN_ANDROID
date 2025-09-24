package `in`.koreatech.koin.feature.dining.component.abTeset

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

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
            .height(46.dp)
            .shadow(
                elevation = 4.dp,
                spotColor = RebrandKoinTheme.colors.neutral800.copy(alpha = 0.04f),
                ambientColor = RebrandKoinTheme.colors.neutral800.copy(alpha = 0.04f)
            )
            .background(color = RebrandKoinTheme.colors.info100, shape = RoundedCornerShape(size = 8.dp))
            .padding(start = 20.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = contentText,
            color = KoinTheme.colors.primary500,
            style = RebrandKoinTheme.typography.medium14
        )

        Button(
            onClick = onClick,
            modifier = Modifier.height(30.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = KoinTheme.colors.primary500
            ),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = buttonText,
                color = KoinTheme.colors.neutral0,
                style = RebrandKoinTheme.typography.bold14
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
