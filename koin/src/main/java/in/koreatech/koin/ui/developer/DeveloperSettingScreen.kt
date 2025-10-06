package `in`.koreatech.koin.ui.developer

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DeveloperSettingScreen(
    modifier: Modifier = Modifier,
    developerSettingViewModel: DeveloperSettingViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier
    ) {
    }
}
