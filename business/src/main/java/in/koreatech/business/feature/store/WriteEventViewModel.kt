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

    fun onTitleChanged(title: String) = intent {
        reduce {
            state.copy(title = title)
        }
    }

    fun onContentChanged(content: String) = intent {
        reduce {
            state.copy(content = content)
        }
    }

    fun onStartYearChanged(startYear: String) = intent {
        if(startYear.length == 4)
            postSideEffect(WriteEventSideEffect.FocusStartMonth)
        reduce {
            state.copy(startYear = startYear)
        }
    }

    fun onStartMonthChanged(startMonth: String) = intent {
        if(startMonth.length == 2)
            postSideEffect(WriteEventSideEffect.FocusStartDay)
        reduce {
            state.copy(startMonth = startMonth)
        }
    }

    fun onStartDayChanged(startDay: String) = intent {
        if(startDay.length == 2)
            postSideEffect(WriteEventSideEffect.FocusEndYear)
        reduce {
            state.copy(startDay = startDay)
        }
    }

    fun onEndYearChanged(endYear: String) = intent {
        if(endYear.length == 4)
            postSideEffect(WriteEventSideEffect.FocusEndMonth)
        reduce {
            state.copy(endYear = endYear)
        }
    }

    fun onEndMonthChanged(endMonth: String) = intent {
        if(endMonth.length == 2)
            postSideEffect(WriteEventSideEffect.FocusEndDay)
        reduce {
            state.copy(endMonth = endMonth)
        }
    }

    fun onEndDayChanged(endDay: String) = intent {
        reduce {
            state.copy(endDay = endDay)
        }
    }

    fun registerEvent() {

    }
}