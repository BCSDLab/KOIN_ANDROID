package `in`.koreatech.koin.ui.store.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
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

    val sortOptions = listOf("기본순", "리뷰순", "별점 높은순")
    val sortValues = listOf(StoreSorter.NONE, StoreSorter.COUNT, StoreSorter.RATING)
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
        ModalBottomSheet(
            onDismissRequest = { setShowSortSheet(false) }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                sortOptions.forEachIndexed { idx, label ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.settingStoreSorter(sortValues[idx])
                                setShowSortSheet(false)
                            }
                            .padding(vertical = 12.dp)
                    ) {
                        RadioButton(
                            selected = currentSortIndex == idx,
                            onClick = {
                                viewModel.settingStoreSorter(sortValues[idx])
                                setShowSortSheet(false)
                            }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(label)
                    }
                }
            }
        }
    }

    if (showMinOrderSheet) {
        ModalBottomSheet(
            onDismissRequest = { setShowMinOrderSheet(false) }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(minOrderButtonText, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(16.dp))
                MinOrderSlider(
                    minOrderOptions = minOrderOptions,
                    minOrderValues = listOf(5000, 10000, 15000, 20000, 0),
                    selectedIndex = minOrderIndex,
                    onSelectedIndexChange = { idx -> setMinOrderIndex(idx) }
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { setShowMinOrderSheet(false) }
                ) { Text("적용하기") }
            }
        }
    }
}
