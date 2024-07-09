package `in`.koreatech.business.feature.store.modifyinfo

import androidx.lifecycle.ViewModel
import com.chargemap.compose.numberpicker.FullHours
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ModifyInfoViewModel @Inject constructor() : ViewModel(),
    ContainerHost<ModifyInfoState, ModifyInfoSideEffect> {
    override val container = container<ModifyInfoState, ModifyInfoSideEffect>(ModifyInfoState()) {}

    fun onBackButtonClicked() = intent {
        postSideEffect(ModifyInfoSideEffect.NavigateToBackScreen)
    }

    fun dialogVisibility(index: Int) = intent {
        reduce {
            state.copy(
                showDialog = !state.showDialog,
                dayOfWeekIndex = index,
            )
        }
    }

    fun dialogTimeSetting() = intent {
        reduce {
            state.copy(
                dialogTimeState = state.operatingTimeList[state.dayOfWeekIndex].openTime.split(":")
                    .let {
                        FullHours(
                            it[0].toInt(), it[1].toInt()
                        )
                    } to state.operatingTimeList[state.dayOfWeekIndex].closeTime.split(":")
                    .let {
                        FullHours(it[0].toInt(), it[1].toInt())
                    }
            )
        }
    }

    fun onSettingStoreTime(hours: Pair<FullHours, FullHours>) = intent {
        reduce {
            val newList = state.operatingTimeList.toMutableList()
            val currentItem = newList[state.dayOfWeekIndex]
            newList[state.dayOfWeekIndex] = currentItem.copy(
                openTime = String.format("%02d:%02d", hours.first.hours, hours.first.minutes),
                closeTime = String.format("%02d:%02d", hours.second.hours, hours.second.minutes),
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
}
