package `in`.koreatech.koin.ui.dining.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.constant.BREAKFAST
import `in`.koreatech.koin.domain.constant.DINNER
import `in`.koreatech.koin.domain.constant.LUNCH
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.usecase.dining.GetDiningUseCase
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DiningViewModel @Inject constructor(
    private val getDiningUseCase: GetDiningUseCase,
) : BaseViewModel() {

    private val _selectedDate = MutableStateFlow(TimeUtil.dateFormatToYYMMDD(DiningUtil.getCurrentDate()))
    val selectedDate: StateFlow<String> get() = _selectedDate

    private val _dining =
        MutableStateFlow<MutableMap<String, List<Dining>>>(mutableMapOf(BREAKFAST to listOf(), LUNCH to listOf(), DINNER to listOf()))
    val dining: StateFlow<Map<String, List<Dining>>> get() = _dining

    init {
        setSelectedDate(DiningUtil.getCurrentDate())
    }

    fun setSelectedDate(date: Date) {
        _selectedDate.value = TimeUtil.dateFormatToYYYYMMDD(date)
    }

    fun getDining(
        date: String = selectedDate.value
    ) {
        if (isLoading.value == false) {
            viewModelScope.launchWithLoading {
                getDiningUseCase(date)
                    .onSuccess {
                        val breakfast = mutableListOf<Dining>()
                        val lunch = mutableListOf<Dining>()
                        val dinner = mutableListOf<Dining>()
                        it.forEach { dining ->
                            when (dining.type) {
                                BREAKFAST -> breakfast.add(dining)
                                LUNCH -> lunch.add(dining)
                                DINNER -> dinner.add(dining)
                            }
                        }
                        _dining.value = mutableMapOf<String, List<Dining>>().apply {
                            this[BREAKFAST] = breakfast
                            this[LUNCH] = lunch
                            this[DINNER] = dinner
                        }
                    }
                    .onFailure {

                    }
            }
        }
    }
}