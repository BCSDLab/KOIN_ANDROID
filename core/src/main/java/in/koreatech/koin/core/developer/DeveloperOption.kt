package `in`.koreatech.koin.core.developer

import androidx.annotation.StringRes
import `in`.koreatech.koin.core.R

sealed class DeveloperOption(val key: String, @param:StringRes val title: Int, @param:StringRes val description: Int?) {
    data object DeliverySprint : DeveloperOption("delivery_sprint", R.string.developer_setting_delivery_title, null)
}

val developerOptionList = listOf(
    DeveloperOption.DeliverySprint
)
