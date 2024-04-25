package `in`.koreatech.business.feature.signup.businessauth

import `in`.koreatech.koin.domain.model.store.AttachStore
import `in`.koreatech.koin.domain.model.store.StoreUrl
import java.io.InputStream

data class BusinessAuthState(
    val name: String = "",
    val storeName: String = "",
    val shopNumber: String = "",
    val shopId: Int = 0,
    val phoneNumber: String = "",
    val openAlertDialog: Boolean = false,
    val selectedImages :MutableList<AttachStore> = mutableListOf(),
    val dialogVisibility:Boolean = false,
    val fileInfo: MutableList<StoreUrl> = mutableListOf(),
    val inputStream: MutableList<InputStream> = mutableListOf(),
    val continuation: Boolean = false,
    val error: Throwable? = null,
)
