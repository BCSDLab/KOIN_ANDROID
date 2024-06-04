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
            state.copy(title = title)
        }
    }

    fun onContentChanged(content: String) = intent {
        if(content.length > maxContentLength)
            return@intent
        reduce {
            state.copy(content = content)
        }
    }

    fun onStartYearChanged(startYear: String) = intent {
        if(isValidNumberInput(4, startYear).not())
            return@intent
        if(startYear.length == 4)
            postSideEffect(WriteEventSideEffect.FocusStartMonth)
        reduce {
            state.copy(startYear = startYear)
        }
    }

    fun onStartMonthChanged(startMonth: String) = intent {
        if(isValidNumberInput(2, startMonth).not())
            return@intent
        if(startMonth.length == 2)
            postSideEffect(WriteEventSideEffect.FocusStartDay)
        reduce {
            state.copy(startMonth = startMonth)
        }
    }

    fun onStartDayChanged(startDay: String) = intent {
        if(isValidNumberInput(2, startDay).not())
            return@intent
        if(startDay.length == 2)
            postSideEffect(WriteEventSideEffect.FocusEndYear)
        reduce {
            state.copy(startDay = startDay)
        }
    }

    fun onEndYearChanged(endYear: String) = intent {
        if(isValidNumberInput(4, endYear).not())
            return@intent
        if(endYear.length == 4)
            postSideEffect(WriteEventSideEffect.FocusEndMonth)
        reduce {
            state.copy(endYear = endYear)
        }
    }

    fun onEndMonthChanged(endMonth: String) = intent {
        if(isValidNumberInput(2, endMonth).not())
            return@intent
        if(endMonth.length == 2)
            postSideEffect(WriteEventSideEffect.FocusEndDay)
        reduce {
            state.copy(endMonth = endMonth)
        }
    }

    fun onEndDayChanged(endDay: String) = intent {
        if(isValidNumberInput(2, endDay).not())
            return@intent
        reduce {
            state.copy(endDay = endDay)
        }
    }

    fun registerEvent() {

    }

    private fun isValidNumberInput(maxLength: Int, input: String): Boolean {
        return !(input.length > maxLength || input.toIntOrNull() == null)
    }
}