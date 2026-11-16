package `in`.koreatech.koin.feature.land.ui.main

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import `in`.koreatech.koin.core.R as CoreR
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme
import `in`.koreatech.koin.feature.land.R
import `in`.koreatech.koin.feature.land.components.LandInformation
import `in`.koreatech.koin.feature.land.components.LandRow
import `in`.koreatech.koin.feature.land.components.LandStatus
import `in`.koreatech.koin.feature.land.model.LandUiModel
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun LandBottomSheet(
    lands: ImmutableList<LandUiModel>,
    filter: LandFilter,
    isLoading: Boolean,
    hasLoadError: Boolean,
    onFilterClick: (LandFilter) -> Unit,
    onLandClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(RebrandKoinTheme.colors.neutral0)
    ) {
        Text(
            text = stringResource(R.string.land_count, lands.size),
            style = RebrandKoinTheme.typography.bold18,
            color = RebrandKoinTheme.colors.neutral800,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
                .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
        ) {
            LandFilter.entries.forEach { option ->
                LandFilterPill(
                    filter = option,
                    isSelected = option == filter,
                    onClick = { onFilterClick(option) }
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            when {
                isLoading -> item {
                    LandStatus(
                        messageRes = R.string.land_loading,
                        isLoading = true,
                        modifier = Modifier.fillParentMaxSize()
                    )
                }
                hasLoadError -> item {
                    LandStatus(
                        messageRes = R.string.land_load_failed,
                        modifier = Modifier.fillParentMaxSize()
                    )
                }
                lands.isEmpty() -> item {
                    LandStatus(
                        messageRes = R.string.land_empty,
                        modifier = Modifier.fillParentMaxSize()
                    )
                }
                else -> items(lands, key = { it.id }) { land ->
                    LandRow(
                        land = land,
                        modifier = Modifier.clickable { onLandClick(land.id) }
                    )
                }
            }
        }
    }
}

@Composable
internal fun LandSelectedBottomSheet(
    land: LandUiModel,
    onDismiss: () -> Unit,
    onDetailClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(RebrandKoinTheme.colors.neutral0)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            LandInformation(
                land = land,
                modifier = Modifier.padding(end = 48.dp)
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(CoreR.drawable.ic_close_round),
                    contentDescription = stringResource(R.string.land_close),
                    tint = RebrandKoinTheme.colors.neutral600,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        FilledButton(
            text = stringResource(R.string.land_view_detail),
            onClick = onDetailClick,
            textStyle = RebrandKoinTheme.typography.medium14,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = RebrandKoinTheme.colors.primary500,
                contentColor = RebrandKoinTheme.colors.neutral0
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun LandFilterPill(
    filter: LandFilter,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(50)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .clip(shape)
            .background(
                if (isSelected) RebrandKoinTheme.colors.primary500 else RebrandKoinTheme.colors.neutral0
            )
            .border(1.dp, RebrandKoinTheme.colors.primary500, shape)
            .selectable(selected = isSelected, role = Role.Tab, onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = stringResource(filter.labelRes()),
            style = RebrandKoinTheme.typography.medium14,
            color = if (isSelected) RebrandKoinTheme.colors.neutral0 else RebrandKoinTheme.colors.primary500
        )
    }
}

@StringRes
private fun LandFilter.labelRes(): Int = when (this) {
    LandFilter.ALL -> R.string.land_filter_all
    LandFilter.MONTHLY -> R.string.land_filter_monthly
    LandFilter.CHARTER -> R.string.land_filter_charter
}
