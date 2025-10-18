package `in`.koreatech.koin.feature.store.review.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun MenuTagChips(
    menuTags: List<String>,
    modifier: Modifier = Modifier,
    onClicked: (Int) -> Unit = { }
) {
    FlowRow(
        modifier = modifier
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        menuTags.forEachIndexed { i, tag ->
            MenuTagChip(
                text = tag,
                onClicked = { onClicked(i) }
            )
        }
    }
}

@Composable
fun MenuTagChip(
    text: String,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit = { }
) {
    val primary300Color = RebrandKoinTheme.colors.primary300
    val chipShape = RebrandKoinTheme.shapes.extraSmall.copy(all = CornerSize(5.dp))

    Box(
        modifier = modifier
            .wrapContentWidth()
            .border(width = 1.dp, color = primary300Color, shape = chipShape)
            .background(color = RebrandKoinTheme.colors.neutral0, shape = chipShape)
            .clickable { onClicked() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = RebrandKoinTheme.typography.regular12,
                color = primary300Color
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_close_thin),
                contentDescription = "",
                tint = primary300Color
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun MenuTagChipsPreview() {
    val tagList = listOf(
        "계란찜", "1인 매운 닭발", "계란찜", "1인 매운 닭발", "닭발", "계란찜", "1인 매운 닭발",
        "계란찜", "1인 매운 닭발", "계란찜", "1인 매운 닭발", "계란찜", "1인 매운 닭발"
    )
    MenuTagChips(
        menuTags = tagList,
        modifier = Modifier,
        onClicked = {}
    )
}
