package `in`.koreatech.business.feature.insertstore.insertdetailinfo

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.business.feature.insertstore.insertmaininfo.InsertBasicInfoScreenState
import `in`.koreatech.koin.domain.model.owner.insertstore.StoreBasicInfo
import kotlinx.android.parcel.Parcelize
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class InsertDetailInfoScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel(), ContainerHost<InsertDetailInfoScreenState, InsertDetailInfoScreenSideEffect> {
    override val container: Container<InsertDetailInfoScreenState, InsertDetailInfoScreenSideEffect> =
        container(InsertDetailInfoScreenState(), savedStateHandle = savedStateHandle) {

            val storeBasicInfoJson: InsertBasicInfoScreenState? = savedStateHandle.get<InsertBasicInfoScreenState>("storeBasicInfo")
            checkNotNull(storeBasicInfoJson)
            getStoreBasicInfo(storeBasicInfoJson)
        }


    private fun getStoreBasicInfo(storeBasicInfo: InsertBasicInfoScreenState){
        intent{
            reduce {
                state.copy(
                    storeCategory = storeBasicInfo.storeCategory,
                    storeName = storeBasicInfo.storeName,
                    storeAddress = storeBasicInfo.storeAddress,
                    storeImage = storeBasicInfo.storeImage
                )
            }
        }
    }
}