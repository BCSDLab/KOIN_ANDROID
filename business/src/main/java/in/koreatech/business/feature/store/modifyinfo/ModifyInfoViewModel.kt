package `in`.koreatech.business.feature.store.modifyinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chargemap.compose.numberpicker.FullHours
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.owner.StoreDetailInfo
import `in`.koreatech.koin.domain.usecase.business.store.ModifyShopInfoUseCase
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ModifyInfoViewModel @Inject constructor(
    private val modifyInfoUseCase: ModifyShopInfoUseCase,
) : ViewModel(),
    ContainerHost<ModifyInfoState, ModifyInfoSideEffect> {
    override val container = container<ModifyInfoState, ModifyInfoSideEffect>(ModifyInfoState()) {}

    fun initStoreInfo(storeInfo: StoreDetailInfo) = intent {
        reduce {
            state.copy(storeInfo = storeInfo)
        }
    }

    fun onBackButtonClicked() = intent {
        postSideEffect(ModifyInfoSideEffect.NavigateToBackScreen)
    }

    fun onSettingOperatingTimeClicked() = intent {
        postSideEffect(ModifyInfoSideEffect.NavigateToSettingOperatingTime)
    }

    fun showAlertDialog(index: Int) = intent {
        reduce {
            state.copy(
                showDialog = true,
                dayOfWeekIndex = index,
            )
        }
    }

    fun hideAlertDialog() = intent {
        reduce {
            state.copy(
                showDialog = false,
            )
        }
    }

    fun initDialogTimeSetting(openTime: String, closeTime: String) = intent {
        reduce {
            if (state.dayOfWeekIndex < 0) return@reduce state
            val openTimeParts = openTime.split(":").map { it.toInt() }
            val closeTimeParts = closeTime.split(":").map { it.toInt() }
            state.copy(
                dialogTimeState = OperatingTime(
                    FullHours(openTimeParts[0], openTimeParts[1]),
                    FullHours(closeTimeParts[0], closeTimeParts[1])
                )
            )
        }
    }

    fun onSettingStoreTime(openTime: FullHours, closeTime: FullHours) = intent {
        reduce {
            state.copy(dialogTimeState = OperatingTime(openTime, closeTime))
        }
    }

    fun onCardAvailableChanged() = intent {
        reduce {
            state.copy(
                storeInfo = state.storeInfo.copy(
                    isCardOk = !(state.storeInfo.isCardOk)
                )
            )

        }
    }

    fun onDeliveryAvailableChanged() = intent {
        reduce {
            state.copy(
                storeInfo = state.storeInfo.copy(
                    isDeliveryOk = !(state.storeInfo.isDeliveryOk)
                )
            )
        }
    }

    fun onTransferAvailableChanged() = intent {
        reduce {
            state.copy(
                storeInfo = state.storeInfo.copy(
                    isBankOk = !(state.storeInfo.isBankOk)
                )
            )
        }
    }

    fun onStoreNameChanged(storeName: String) = intent {
        reduce {
            state.copy(storeInfo = state.storeInfo.copy(name = storeName))
        }
    }

    fun onPhoneNumberChanged(phone: String) = intent {
        reduce {
            state.copy(storeInfo = state.storeInfo.copy(phone = phone))
        }
    }

    fun onAddressChanged(address: String) = intent {

        reduce {
            state.copy(storeInfo = state.storeInfo.copy(address = address))
        }
    }

    fun onDeliveryPriceChanged(price: Int) = intent {
        reduce {
            state.copy(storeInfo = state.storeInfo.copy(deliveryPrice = price))
        }
    }

    fun onDescriptionChanged(description: String) = intent {
        reduce {
            state.copy(storeInfo = state.storeInfo.copy(description = description))
        }
    }

    fun isClosedDay(index: Int) {
        intent {
            if (index >= 0 && index <= state.storeInfo.operatingTime.size) {
                val newList = state.storeInfo.operatingTime.toMutableList()
                val currentItem = newList[index]
                newList[index] = currentItem.copy(closed = !currentItem.closed)
                reduce {
                    state.copy(storeInfo = state.storeInfo.copy(operatingTime = newList))
                }
            }
        }
    }

    fun modifyStoreInfo(storeId: Int, storeDetailInfo: StoreDetailInfo) {
        viewModelScope.launch {
            modifyInfoUseCase.invoke(
                storeId,
                storeDetailInfo,
            )
        }
    }
}
