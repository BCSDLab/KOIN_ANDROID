package `in`.koreatech.business.feature.insertstore.insertdetailinfo

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InsertDetailInfoScreenState (
    val storeCategory: Int = -1,
    val storeName: String = "",
    val storeAddress: String = "",
    val storeImage: Uri = Uri.EMPTY
): Parcelable