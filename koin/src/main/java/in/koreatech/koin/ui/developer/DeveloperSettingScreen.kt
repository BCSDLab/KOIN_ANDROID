package `in`.koreatech.koin.ui.developer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import `in`.koreatech.koin.R
import `in`.koreatech.koin.ui.developer.DeveloperSettingViewModel.Companion.DELIVERY_SPRINT
import `in`.koreatech.koin.ui.developer.component.DeveloperSettingItem

@Composable
fun DeveloperSettingScreen(
    modifier: Modifier = Modifier,
    developerSettingViewModel: DeveloperSettingViewModel = hiltViewModel()
) {
    val deliveryDeveloperSetting by developerSettingViewModel.deliveryDeveloperSetting.collectAsState()

    Column(
        modifier = modifier
    ) {
        DeveloperSettingItem(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp),
            title = stringResource(R.string.developer_setting_delivery_title),
            key = DELIVERY_SPRINT,
            value = deliveryDeveloperSetting,
            onValueChange = developerSettingViewModel::setDeveloperSetting
        )
    }
}
