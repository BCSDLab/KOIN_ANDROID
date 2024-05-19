package `in`.koreatech.business.feature.insertstore.insertmaininfo

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.owner.insertstore.StoreBasicInfo
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class InsertBasicInfoScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel(), ContainerHost<InsertBasicInfoScreenState, InsertBasicInfoScreenSideEffect> {
    override val container: Container<InsertBasicInfoScreenState,InsertBasicInfoScreenSideEffect> =
        container(InsertBasicInfoScreenState(), savedStateHandle = savedStateHandle){
            val categoryId = savedStateHandle.get<Int>("categoryId")
            checkNotNull(categoryId)
            getCategoryId(categoryId)
        }

    fun insertStoreName(storeName: String) = intent {
        reduce {
            state.copy(storeName = storeName)
        }
        isBasicInfoValidate()
    }

    fun insertStoreAddress(storeAddress: String) = intent{
        reduce {
            state.copy(storeAddress = storeAddress)
        }
        isBasicInfoValidate()
    }

    fun insertStoreImage(storeImage: Uri) = intent{
        reduce {
            state.copy(storeImage = storeImage)
        }
        isBasicInfoValidate()
        storeImageIsEmpty()
    }

    fun onNextButtonClick(){
        intent{
            if(state.isBasicInfoValidate){
                val storeBasicInfo: StoreBasicInfo = StoreBasicInfo(
                    storeName = state.storeName,
                    storeAddress = state.storeAddress,
                    storeImage = state.storeImage.toString(),
                    storeCategory = state.storeCategory
                )
                postSideEffect(InsertBasicInfoScreenSideEffect.NavigateToInsertDetailInfoScreen(storeBasicInfo))
            }
            else{
                if(state.storeImageIsEmpty){
                    postSideEffect(InsertBasicInfoScreenSideEffect.ShowMessage(ErrorType.NullStoreImage))
                }
                else if(state.storeName.isEmpty()){
                    postSideEffect(InsertBasicInfoScreenSideEffect.ShowMessage(ErrorType.NullStoreName))
                }
                else if(state.storeAddress.isEmpty()){
                    postSideEffect(InsertBasicInfoScreenSideEffect.ShowMessage(ErrorType.NullStoreAddress))
                }
            }
        }
    }

    private fun storeImageIsEmpty() = intent{
        reduce {
            state.copy(storeImageIsEmpty = state.storeImage == Uri.EMPTY)
        }
        isBasicInfoValidate()
    }

    private fun getCategoryId(id: Int){
        intent{
            reduce {
                state.copy(
                    storeCategory = id
                )
            }
        }
    }

    private fun isBasicInfoValidate() = intent {
        reduce {
            state.copy(
                isBasicInfoValidate = state.storeAddress.isNotBlank() && state.storeName.isNotBlank() && state.storeImage != Uri.EMPTY
            )
        }
    }

}