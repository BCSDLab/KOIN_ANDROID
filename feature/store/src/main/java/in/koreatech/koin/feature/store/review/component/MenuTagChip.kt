package `in`.koreatech.koin.feature.store.review.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R
import `in`.koreatech.koin.feature.store.component.KoinStoreChip
import `in`.koreatech.koin.feature.store.component.KoinStoreChipDefaults
import `in`.koreatech.koin.feature.store.component.KoinStoreChipIconStyle

@Composable
fun MenuTagChipGroup(
    menuTags: List<String>,
    modifier: Modifier = Modifier,
    isIconVisibility: Boolean = true,
    enabled: Boolean = true,
    enableRipple: Boolean = true,
    onClick: (Int) -> Unit = { }
) {
    val menuTagChipIcon = painterResource(id = R.drawable.ic_close_thin)
    val menuTagStyle = KoinStoreChipDefaults.koinStoreChipStyle(
        textColor = RebrandKoinTheme.colors.primary300,
        textStyle = RebrandKoinTheme.typography.regular12,
        elevation = 0.dp,
        borderWidth = 1.dp,
        borderColor = RebrandKoinTheme.colors.primary300,
        shape = RebrandKoinTheme.shapes.extraSmall.copy(all = CornerSize(5.dp)),
        paddingValues = PaddingValues(horizontal = 10.dp, vertical = 3.dp),
        ambientColor = Color.Transparent,
        spotColor = Color.Transparent,
        innerPadding = 4.dp
    )

    val menuTagIconStyle = KoinStoreChipIconStyle(
        iconColor = RebrandKoinTheme.colors.primary300,
        iconSize = 14.dp
    )

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        menuTags.forEachIndexed { i, tag ->
            if (isIconVisibility) {
                KoinStoreChip(
                    text = tag,
                    chipStyle = menuTagStyle,
                    trailingIcon = menuTagChipIcon,
                    trailingIconStyle = menuTagIconStyle,
                    enabled = enabled,
                    enableRipple = enableRipple,
                    onClick = { onClick(i) }
                )
            } else {
                KoinStoreChip(
                    text = tag,
                    chipStyle = menuTagStyle,
                    enabled = enabled,
                    enableRipple = enableRipple,
                    onClick = { onClick(i) }
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun MenuTagChipGroupPreview() {
    val tagList = listOf(
        "계란찜", "1인 매운 닭발", "계란찜", "1인 매운 닭발", "닭발", "계란찜", "1인 매운 닭발",
        "계란찜", "1인 매운 닭발", "계란찜", "1인 매운 닭발", "계란찜", "1인 매운 닭발"
    )

    RebrandKoinTheme {
        MenuTagChipGroup(
            menuTags = tagList,
            modifier = Modifier,
            onClick = {}
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun MenuTagChipGroupNoIconPreview() {
    val tagList = listOf(
        "계란찜", "1인 매운 닭발", "계란찜", "1인 매운 닭발", "닭발", "계란찜", "1인 매운 닭발",
        "계란찜", "1인 매운 닭발", "계란찜", "1인 매운 닭발", "계란찜", "1인 매운 닭발"
    )

    RebrandKoinTheme {
        MenuTagChipGroup(
            menuTags = tagList,
            modifier = Modifier,
            isIconVisibility = false,
            onClick = {}
        )
    }
}
