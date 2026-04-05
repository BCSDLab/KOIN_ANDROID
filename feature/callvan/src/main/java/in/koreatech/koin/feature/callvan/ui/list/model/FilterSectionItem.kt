package `in`.koreatech.koin.feature.callvan.ui.list.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class FilterSectionItem(
    val title: String,
    val items: ImmutableList<CallvanFilterType>,
    val selectedItems: ImmutableList<CallvanFilterType>,
    val hint: String? = null
)