package `in`.koreatech.business.feature.insertstore.insertdetailinfo

import android.net.Uri
import android.os.Parcelable
import `in`.koreatech.business.feature.insertstore.insertmaininfo.InsertBasicInfoScreenState
import `in`.koreatech.koin.domain.model.owner.insertstore.StoreBasicInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class InsertDetailInfoScreenState (
    val storeCategory: Int = -1,
    val storeName: String = "",
    val storeAddress: String = "",
    val storeImage: String = ""
): Parcelable