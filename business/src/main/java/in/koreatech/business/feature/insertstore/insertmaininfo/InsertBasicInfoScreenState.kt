package `in`.koreatech.business.feature.insertstore.insertmaininfo

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InsertBasicInfoScreenState(
    val storeName: String = "",
    val storeAddress: String = "",
    val storeImage: Uri = Uri.EMPTY,
    val storeImageIsEmpty: Boolean = true,
    val storeCategory: Int = 0,
    val isBasicInfoValid: Boolean = false
): Parcelable