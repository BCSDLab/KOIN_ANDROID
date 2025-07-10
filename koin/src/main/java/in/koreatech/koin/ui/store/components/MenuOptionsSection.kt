package `in`.koreatech.koin.ui.store.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuOptionsSection(options: List<MenuOption>) {
    Column(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        options.forEach { option ->
            OptionView(option)
        }
    }
}

// 추후 변경 예정
data class MenuOption(
    val name: String,
    val description: String,
    val selectionType: SelectionType,
    val required: Boolean,
    val selectCount: Int,
    val items: List<OptionItem>
)

// 추후 변경 예정
data class OptionItem(
    val name: String,
//    val detail: String,
    val price: Int
)

// 추후 변경 예정
enum class SelectionType {
    RADIO,
    CHECKBOX
}
