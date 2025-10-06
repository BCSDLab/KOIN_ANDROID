package `in`.koreatech.koin.feature.setting.ui.term.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun TermDescriptionItem(
    title: String,
    description: List<String>,
    modifier: Modifier = Modifier,
    backgroundColor: Color = KoinTheme.colors.neutral0
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicText(
                text = title,
                style = KoinTheme.typography.bold15
            )
        }
        HorizontalDivider(color = KoinTheme.colors.neutral100)
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 26.dp)
        ) {
            description.forEach {
                BasicText(
                    text = it,
                    style = KoinTheme.typography.regular12
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun TermDescriptionItemPreview() {
    TermDescriptionItem(
        title = "제 1조 ---",
        description = listOf("① ('koreatech.in'이하 '코인')은(는) 다음의 개인정보 항목을 처리하고 있습니다.\n 어쩌구 저쩌구..")
    )
}