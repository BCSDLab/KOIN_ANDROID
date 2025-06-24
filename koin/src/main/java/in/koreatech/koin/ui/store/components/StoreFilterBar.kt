package `in`.koreatech.koin.ui.store.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.domain.model.store.StoreSorter
import `in`.koreatech.koin.ui.store.viewmodel.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreFilterBar(
    viewModel: StoreViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val (showSortSheet, setShowSortSheet) = remember { mutableStateOf(false) }
    val (showMinOrderSheet, setShowMinOrderSheet) = remember { mutableStateOf(false) }

    val sortOptions = listOf("별점 높은순", "리뷰순", "기본순")
    val sortValues = listOf(StoreSorter.RATING, StoreSorter.COUNT, StoreSorter.NONE)
    val currentSorter = viewModel.storeSorter.observeAsState(StoreSorter.NONE).value
    val currentSortIndex = sortValues.indexOf(currentSorter).takeIf { it >= 0 } ?: 0

    val minOrderOptions = listOf("5,000", "10,000", "15,000", "20,000", "전체")
    val (minOrderIndex, setMinOrderIndex) = remember { mutableIntStateOf(4) }

    val isOperating = viewModel.isOperating.observeAsState(false).value
    val isDelivery = viewModel.isDelivery.observeAsState(false).value
    val (isTakeout, setIsTakeout) = remember { mutableStateOf(false) }
    val (isFreeDeliveryTip, setIsFreeDeliveryTip) = remember { mutableStateOf(false) }

    val minOrderButtonText = if (minOrderIndex == 4) {
        "최소주문금액"
    } else {
        "최소주문금액 ${minOrderOptions[minOrderIndex]}원 이하"
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = { setShowSortSheet(true) },
            modifier = Modifier
                .height(34.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = KoinTheme.colors.neutral400,
                    spotColor = KoinTheme.colors.neutral500
                ),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xFFF8F8FA),
                contentColor = RebrandKoinTheme.colors.primary500
            ),
            border = BorderStroke(1.dp, RebrandKoinTheme.colors.primary500),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    sortOptions.getOrElse(currentSortIndex) { "기본순" },
                    fontSize = 14.sp,
                    style = KoinTheme.typography.bold14,
                    color = RebrandKoinTheme.colors.primary500
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down_3),
                    contentDescription = null,
                    tint = RebrandKoinTheme.colors.primary500
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Row(
            modifier = Modifier
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterToggleButton(
                checked = isOperating,
                onCheckedChange = {
                    viewModel.filterStoreIsOpen(!isOperating)
                },
                iconRes = R.drawable.mdi_food,
                text = "영업중"
            )
            FilterToggleButton(
                checked = isDelivery,
                onCheckedChange = { viewModel.filterStoreIsDelivery(!isDelivery) },
                iconRes = R.drawable.motorcycle,
                text = "배달 가능"
            )
            FilterToggleButton(
                checked = isTakeout,
                onCheckedChange = { setIsTakeout(!isTakeout) },
                iconRes = R.drawable.packing,
                text = "포장 가능"
            )
            FilterToggleButton(
                checked = isFreeDeliveryTip,
                onCheckedChange = { setIsFreeDeliveryTip(!isFreeDeliveryTip) },
                iconRes = R.drawable.free_tag,
                text = "배달팁 무료"
            )
            OutlinedButton(
                onClick = { setShowMinOrderSheet(true) },
                modifier = Modifier
                    .height(34.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = KoinTheme.colors.neutral400,
                        spotColor = KoinTheme.colors.neutral500
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFFF8F8FA),
                    contentColor = RebrandKoinTheme.colors.primary500
                ),
                border = BorderStroke(1.dp, RebrandKoinTheme.colors.primary500),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Text(
                    text = minOrderButtonText,
                    fontSize = 14.sp,
                    style = KoinTheme.typography.bold14,
                    color = RebrandKoinTheme.colors.primary500
                )
            }
        }
    }

    if (showSortSheet) {
        SortBottomSheet(
            currentIndex = currentSortIndex,
            options = sortOptions,
            onSelect = { idx ->
                viewModel.settingStoreSorter(sortValues[idx])
                setShowSortSheet(false)
            },
            onClose = { setShowSortSheet(false) }
        )
    }

    if (showMinOrderSheet) {
        MinOrderSliderBottomSheet(
            selectedIndex = minOrderIndex,
            options = minOrderOptions,
            onSelected = { idx -> setMinOrderIndex(idx) },
            onApply = { setShowMinOrderSheet(false) },
            onClose = { setShowMinOrderSheet(false) }
        )
    }
}
