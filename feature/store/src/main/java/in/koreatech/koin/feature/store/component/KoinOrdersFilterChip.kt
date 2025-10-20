package `in`.koreatech.koin.feature.store.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.store.R

@Composable
fun KoinOrdersFilterChip(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    KoinStoreChip(
        modifier = modifier,
        text = text,
        chipStyle = KoinStoreChipDefaults.koinStoreChipStyle(
            elevation = if (isSelected) 0.dp else 4.dp,
            containerColor = if (isSelected) RebrandKoinTheme.colors.primary500 else RebrandKoinTheme.colors.neutral0,
            textColor = if (isSelected) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.neutral500
        ),
        trailingIcon = painterResource(R.drawable.ic_store_arrow_down),
        trailingIconStyle = KoinStoreChipDefaults.koinStoreIconStyle(
            iconColor = if (isSelected) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.neutral500
        ),
        onClick = onClick
    )
}

@Preview(showBackground = true)
@Composable
private fun DownClipTruePreview() {
    KoinOrdersFilterChip(
        text = "주소",
        isSelected = true
    )
}
