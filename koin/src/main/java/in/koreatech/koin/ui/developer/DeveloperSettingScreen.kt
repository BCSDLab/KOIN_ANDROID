package `in`.koreatech.koin.ui.developer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.core.developer.DeveloperOptionUtil
import `in`.koreatech.koin.core.developer.developerOptionList
import `in`.koreatech.koin.ui.developer.component.DeveloperSettingItem

@Composable
fun DeveloperSettingScreen(
    modifier: Modifier = Modifier,
    developerSettingViewModel: DeveloperSettingViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier
    ) {
        developerOptionList.forEach {
            key(it.key) {
                DeveloperSettingItem(
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp),
                    title = stringResource(it.title),
                    description = it.description?.let { res -> stringResource(res) },
                    key = it,
                    value = DeveloperOptionUtil.getDeveloperOptionFlow(it).collectAsState(false).value,
                    onValueChange = developerSettingViewModel::setDeveloperSetting
                )
            }
        }
    }
}
