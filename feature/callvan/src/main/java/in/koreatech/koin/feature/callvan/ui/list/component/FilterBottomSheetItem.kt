package `in`.koreatech.koin.feature.callvan.ui.list.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

@Composable
fun FilterBottomSheetItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) RebrandKoinTheme.colors.primary500 else KoinTheme.colors.neutral300
        ),
        color = KoinTheme.colors.neutral0,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Text(
                text = text,
                color = if (isSelected) RebrandKoinTheme.colors.primary500 else KoinTheme.colors.neutral500,
                style = KoinTheme.typography.bold14
            )
        }
    }
}
