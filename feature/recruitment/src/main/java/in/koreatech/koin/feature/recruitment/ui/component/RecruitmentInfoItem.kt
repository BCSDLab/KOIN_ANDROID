package `in`.koreatech.koin.feature.recruitment.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun RecruitmentInfoItem(
    icon: @Composable () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = RebrandKoinTheme.colors.neutral500
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        icon()
        Text(
            text = text,
            style = RebrandKoinTheme.typography.regular10,
            color = textColor
        )
    }
}
