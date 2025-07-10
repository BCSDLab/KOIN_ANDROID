package `in`.koreatech.koin.ui.store.defaults

import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import `in`.koreatech.koin.core.designsystem.theme.RebrandKoinTheme

object StoreSearchBarDefaults {
    @Composable
    fun colors(): TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = RebrandKoinTheme.colors.neutral0,
        unfocusedContainerColor = RebrandKoinTheme.colors.neutral0,
        disabledContainerColor = RebrandKoinTheme.colors.neutral0,
        errorContainerColor = RebrandKoinTheme.colors.neutral0
    )
}