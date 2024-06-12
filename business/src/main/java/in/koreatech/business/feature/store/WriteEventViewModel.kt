package `in`.koreatech.business.feature.store

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class WriteEventViewModel(

) : ViewModel(), ContainerHost<WriteEventState, WriteEventSideEffect> {
    override val container = container<WriteEventState, WriteEventSideEffect>(WriteEventState())

    val maxImageLength = 3
    val maxTitleLength = 25
    val maxContentLength = 500
    fun onTitleChanged(title: String) = intent {
        if(title.length > maxTitleLength)
            return@intent
        reduce {
            state.copy(title = title, showTitleInputAlert = false)
        }
    }

    fun onContentChanged(content: String) = intent {
        if(content.length > maxContentLength)
            return@intent
        reduce {
            state.copy(content = content, showContentInputAlert = false)
        }
    }

    fun onStartYearChanged(startYear: String) = intent {
        if(isValidNumberInput(4, startYear).not())
            return@intent
        if(startYear.length == 4)
            postSideEffect(WriteEventSideEffect.FocusStartMonth)
        reduce {
            state.copy(startYear = startYear,
                showDateInputAlert = if (state.showDateInputAlert)
                    !isAllDateInputFilled(state.copy(startYear = startYear))
                else false
            )
        }
    }

    fun onStartMonthChanged(startMonth: String) = intent {
        if(isValidNumberInput(2, startMonth).not())
            return@intent
        if(startMonth.length == 2)
            postSideEffect(WriteEventSideEffect.FocusStartDay)
        reduce {
            state.copy(startMonth = startMonth,
                showDateInputAlert = if (state.showDateInputAlert)
                    !isAllDateInputFilled(state.copy(startMonth = startMonth))
                else false
            )
        }
    }

    fun onStartDayChanged(startDay: String) = intent {
        if(isValidNumberInput(2, startDay).not())
            return@intent
        if(startDay.length == 2)
            postSideEffect(WriteEventSideEffect.FocusEndYear)
        reduce {
            state.copy(startDay = startDay,
                showDateInputAlert = if (state.showDateInputAlert)
                    !isAllDateInputFilled(state.copy(startDay = startDay))
                else false
            )
        }
    }

    fun onEndYearChanged(endYear: String) = intent {
        if(isValidNumberInput(4, endYear).not())
            return@intent
        if(endYear.length == 4)
            postSideEffect(WriteEventSideEffect.FocusEndMonth)
        reduce {
            state.copy(endYear = endYear,
                showDateInputAlert = if (state.showDateInputAlert)
                    !isAllDateInputFilled(state.copy(endYear = endYear))
                else false
            )
        }
    }

    fun onEndMonthChanged(endMonth: String) = intent {
        if(isValidNumberInput(2, endMonth).not())
            return@intent
        if(endMonth.length == 2)
            postSideEffect(WriteEventSideEffect.FocusEndDay)
        reduce {
            state.copy(endMonth = endMonth,
                showDateInputAlert = if (state.showDateInputAlert)
                    !isAllDateInputFilled(state.copy(endMonth = endMonth))
                else false
            )
        }
    }

    fun onEndDayChanged(endDay: String) = intent {
        if(isValidNumberInput(2, endDay).not())
            return@intent
        reduce {
            state.copy(endDay = endDay,
                showDateInputAlert = if (state.showDateInputAlert)
                    !isAllDateInputFilled(state.copy(endDay = endDay))
                else false
            )
        }
    }

    fun registerEvent() = intent {
        reduce {
            state.copy(
                showTitleInputAlert = state.title.isEmpty(),
                showContentInputAlert = state.content.isEmpty(),
                showDateInputAlert = state.startYear.length != 4
                        || state.startMonth.length != 2
                        || state.startDay.length != 2
                        || state.endYear.length != 4
                        || state.endMonth.length != 2
                        || state.endDay.length != 2
            )
        }
    }

    private fun isAllDateInputFilled(state: WriteEventState) : Boolean {
        return state.startYear.length == 4
                && state.startMonth.length == 2
                && state.startDay.length == 2
                && state.endYear.length == 4
                && state.endMonth.length == 2
                && state.endDay.length == 2
    }

    private fun isValidNumberInput(maxLength: Int, input: String): Boolean {
        if(input.length > maxLength)
            return false
        if(input.isNotEmpty() && input.toIntOrNull() == null)
            return false
        return true
    }
}