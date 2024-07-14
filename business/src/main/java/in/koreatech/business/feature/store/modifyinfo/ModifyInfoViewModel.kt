package `in`.koreatech.business.feature.store.modifyinfo

import android.util.Log
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

    fun dialogTimeSetting() = intent {
        reduce {
            if(state.dayOfWeekIndex<0) return@reduce state
            state.copy(
                dialogTimeState = OperatingTime(state.operatingTimeList[state.dayOfWeekIndex].operatingTime.openTime.let {
                    FullHours(it.hours, it.minutes)
                },
                    state.operatingTimeList[state.dayOfWeekIndex].operatingTime.closeTime.let {
                        FullHours(it.hours, it.minutes)
                    })
            )
        }
    }

    fun onSettingStoreTime(hours: OperatingTime) = intent {
        reduce {
            val newList = state.operatingTimeList.toMutableList()
            val currentItem = newList[state.dayOfWeekIndex]
            newList[state.dayOfWeekIndex] = currentItem.copy(
                operatingTime = hours,
            )
            state.copy(operatingTimeList = newList)
        }
        dialogTimeSetting()
    }

    fun isClosedDay(index: Int) {
        intent {
            if (index >= 0 && index <= state.operatingTimeList.size) {
                val newList = state.operatingTimeList.toMutableList()
                val currentItem = newList[index]
                newList[index] = currentItem.copy(closed = !currentItem.closed)
                reduce {
                    state.copy(operatingTimeList = newList)
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
