package `in`.koreatech.business.feature.insertstore.insertdetailinfo

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.business.feature.insertstore.insertmaininfo.InsertBasicInfoScreenState
import `in`.koreatech.koin.domain.model.owner.insertstore.StoreBasicInfo
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

            val storeBasicInfoJson: String? = savedStateHandle.get<String>("storeBasicInfo")
            checkNotNull(storeBasicInfoJson)

            val storeBasicInfo = Gson().fromJson(
                storeBasicInfoJson,
                StoreBasicInfo::class.java
            )
            getStoreBasicInfo(storeBasicInfo)
        }


    private fun getStoreBasicInfo(storeBasicInfo: StoreBasicInfo){
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