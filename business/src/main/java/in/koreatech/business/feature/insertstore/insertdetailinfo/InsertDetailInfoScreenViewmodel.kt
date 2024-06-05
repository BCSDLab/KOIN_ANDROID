package `in`.koreatech.business.feature.insertstore.insertdetailinfo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.chargemap.compose.numberpicker.Hours
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.business.feature.insertstore.insertmaininfo.InsertBasicInfoScreenState
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
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
            if(storeBasicInfoJson != null) getStoreBasicInfo(storeBasicInfoJson)
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

    private fun isDetailInfoValid() = intent{
        reduce {
            state.copy(
                isDetailInfoValid = state.storePhoneNumber.isNotEmpty() && state.storeDeliveryFee.isNotEmpty() && state.storeOtherInfo.isNotEmpty()
            )
        }
    }

    private fun isOpenTimeSetting(isOpenTimeSetting: Boolean) = intent{
        reduce{
            state.copy(isOpenTimeSetting = isOpenTimeSetting)
        }
    }

    private fun dayOfIndex(index: Int) = intent{
        reduce{
            state.copy(dayOfWeekIndex = index)
        }
    }

    fun onChangePhoneNumber(phoneNumber: String) = intent{
        reduce {
            state.copy(storePhoneNumber = phoneNumber)
        }
        isDetailInfoValid()
    }

    fun onChangeDeliveryFee(deliveryFee: String) = intent{
        reduce {
            state.copy(storeDeliveryFee = deliveryFee)
        }
        isDetailInfoValid()
    }

    fun onChangeOtherInfo(otherInfo: String) = intent{
        reduce {
            state.copy(storeOtherInfo = otherInfo)
        }
        isDetailInfoValid()
    }

    fun changeIsDeliveryOk() = intent{
        reduce{
            state.copy(isDeliveryOk = !state.isDeliveryOk)
        }
    }

    fun changeIsCardOk() = intent{
        reduce{
            state.copy(isCardOk = !state.isCardOk)
        }
    }

    fun changeIsBankOk() = intent{
        reduce{
            state.copy(isBankOk = !state.isBankOk)
        }
    }

    fun showDialog(isOpenTimeSetting: Boolean, index: Int) = intent{
        reduce {
            state.copy(showDialog = true)
        }
        isOpenTimeSetting(isOpenTimeSetting)
        dayOfIndex(index)
    }

    fun closeDialog() = intent{
        reduce{
            state.copy(showDialog = false)
        }
    }

    fun settingStoreTime(time: Hours, index: Int, isOpenTimeSetting: Boolean) {
        intent {
            val newList = state.operatingTimeList.toMutableList()
            val currentItem = newList[index]
            when(isOpenTimeSetting){
                true -> newList[index] = currentItem.copy(openTime = time.toTimeString())
                false -> newList[index] = currentItem.copy(closeTime = time.toTimeString())
            }
            reduce{
                state.copy(operatingTimeList = newList)
            }
            closeDialog()
        }
    }

    fun isClosedDay(index: Int) {
        intent {
            val newList = state.operatingTimeList.toMutableList()
            val currentItem = newList[index]
            newList[index] = currentItem.copy(closed = !currentItem.closed)
            reduce{
                state.copy(operatingTimeList = newList)
            }
        }
    }

    fun onNextButtonClick(){
        intent{
            if(state.isDetailInfoValid){
                val storeDetailInfo = state

                postSideEffect(InsertDetailInfoScreenSideEffect.NavigateToCheckScreen(storeDetailInfo))
                return@intent
            }

            when {
                state.storePhoneNumber.isEmpty() -> postSideEffect(InsertDetailInfoScreenSideEffect.ShowMessage(DetailInfoErrorType.NullStorePhoneNumber))
                state.storeDeliveryFee.isEmpty() -> postSideEffect(InsertDetailInfoScreenSideEffect.ShowMessage(DetailInfoErrorType.NullStoreDeliveryFee))
                state.storeOtherInfo.isEmpty() -> postSideEffect(InsertDetailInfoScreenSideEffect.ShowMessage(DetailInfoErrorType.NullStoreOtherInfo))
            }
        }
    }

}

fun Hours.toTimeString(): String {

    val hoursString: String =
        if (this.hours < 10) "0" + this.hours.toString() else this.hours.toString()

    val minutesString: String =
        if (this.minutes < 10) "0" + this.minutes.toString() else this.minutes.toString()

    return "$hoursString:$minutesString"
}