package `in`.koreatech.koin.feature.club.ui.clubdetail.component.snackbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun DetailSnackBar(
    message: String,
    label: String?,
    modifier: Modifier = Modifier,
    onLabelClick: () -> Unit = {},
    messageStyle: TextStyle = KoinTheme.typography.regular14,
    labelStyle: TextStyle = KoinTheme.typography.regular14,
    messageColor: Color = KoinTheme.colors.neutral0,
    labelColor: Color = KoinTheme.colors.info700,
    containerColor: Color = KoinTheme.colors.primary900.copy(alpha = 0.8f),
    shape: Shape = KoinTheme.shapes.medium
) {
    Snackbar(
        modifier = modifier
            .padding(horizontal = 24.dp),
        content = {
            Text(
                text = message,
                style = messageStyle,
                color = messageColor
            )
        },
        action = {
            label?.let {
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clickable { onLabelClick() }
                ) {
                    Text(
                        text = label,
                        style = labelStyle,
                        color = labelColor
                    )
                }
            }
        },
        containerColor = containerColor,
        shape = shape
    )
}
