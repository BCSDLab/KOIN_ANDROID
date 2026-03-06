package `in`.koreatech.koin.feature.callvan.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import `in`.koreatech.koin.core.designsystem.noRippleClickable
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme

@Composable
fun CallvanDropdownMenu(
    expanded: Boolean,
    items: List<CallvanDropdownMenuItem>,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    topPadding: Dp = 0.dp
) {
    if (!expanded) return

    val density = LocalDensity.current

    Popup(
        alignment = Alignment.TopEnd,
        offset = IntOffset(0, with(density) { topPadding.roundToPx() }),
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(focusable = true)
    ) {
        Box(
            modifier = modifier
                .shadow(elevation = 4.dp, shape = KoinTheme.shapes.medium)
                .clip(KoinTheme.shapes.medium)
                .background(KoinTheme.colors.neutral50)
        ) {
            Column(modifier = Modifier.width(IntrinsicSize.Max)) {
                items.forEachIndexed { index, item ->
                    if (index > 0) {
                        HorizontalDivider(
                            color = KoinTheme.colors.neutral200,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }
                    Text(
                        text = item.text(),
                        style = KoinTheme.typography.regular14,
                        color = item.color(),
                        modifier = Modifier
                            .noRippleClickable {
                                item.onClick()
                                onDismissRequest()
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }
        }
    }
}

class CallvanDropdownMenuItem(
    val text: @Composable () -> String,
    val color: @Composable () -> Color = { KoinTheme.colors.neutral800 },
    val onClick: () -> Unit
) {
    constructor(
        text: String,
        color: Color? = null,
        onClick: () -> Unit
    ) : this({ text }, { color ?: KoinTheme.colors.neutral800 }, onClick)
}

@Preview
@Composable
private fun CallvanDropdownMenuPreview() {
    val items = listOf(
        CallvanDropdownMenuItem(
            text = "모두 읽음으로 표시",
            onClick = {}
        ),
        CallvanDropdownMenuItem(
            text = "알림 전체 삭제",
            onClick = {}
        )
    )
    CallvanDropdownMenu(
        expanded = true,
        items = items,
        onDismissRequest = {}
    )
}
