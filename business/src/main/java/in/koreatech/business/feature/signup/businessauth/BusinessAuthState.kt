package `in`.koreatech.business.feature.signup.businessauth

import android.graphics.Bitmap
import `in`.koreatech.koin.domain.model.store.AttachStore
import `in`.koreatech.koin.domain.model.store.StoreUrl

data class BusinessAuthState(
    val name: String = "",
    val shopName: String = "",
    val shopNumber: String = "",
    val shopId: Int? = null,
    val openAlertDialog: Boolean = false,
    val selectedImages :MutableList<AttachStore> = mutableListOf(),
    val dialogVisibility:Boolean = false,
    val fileInfo: MutableList<StoreUrl> = mutableListOf(),
    val bitmap: MutableList<Bitmap> = mutableListOf(),
    val continuation: Boolean = false,
    val error: Throwable? = null,
){
    val isButtonEnabled: Boolean
        get() = name.isNotEmpty() && shopName.isNotEmpty() && shopNumber.isNotEmpty() && selectedImages.isNotEmpty()
}
