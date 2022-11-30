package `in`.koreatech.koin.ui.dining.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.usecase.dining.CheckCorrectDateRangeUseCase
import `in`.koreatech.koin.domain.usecase.dining.DiningUseCase
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DiningViewModel @Inject constructor(
    private val diningUseCase: DiningUseCase,
    private val checkCorrectDateRangeUseCase: CheckCorrectDateRangeUseCase
) : BaseViewModel() {
    private val _isDataLoaded = MutableLiveData<Boolean>()
    val isDataLoaded: LiveData<Boolean> get() = _isDataLoaded
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> get() = _selectedDate
    private val _diningData = MutableLiveData<List<Dining>>(listOf())
    val diningData: LiveData<List<Dining>> get() = _diningData
    private val _isDateError = MutableLiveData(false)
    val isDateError: LiveData<Boolean> get() = _isDateError
    var selectedType: DiningType = DiningUtil.getCurrentType()

    init {
        _selectedDate.value = TimeUtil.dateFormatToYYYYMMDD(DiningUtil.getCurrentDate())
    }

    fun updateDiningData(
        date: Date = TimeUtil.stringToDateYYYYMMDD(
            selectedDate.value ?: TimeUtil.dateFormatToYYYYMMDD(DiningUtil.getCurrentDate())
        )
    ) {
        if (isLoading.value == false) {
            viewModelScope.launchWithLoading {
                diningUseCase(date)
                    .onSuccess {
                        _diningData.value = it
                        _isDataLoaded.value = true
                    }
                    .onFailure {
                        _diningData.value = listOf()
                        _isDataLoaded.value = false
                    }
            }
        }
    }

    fun getNextDayDiningData() {
        val currentDate = TimeUtil.stringToDateYYYYMMDD(
            selectedDate.value ?: TimeUtil.dateFormatToYYYYMMDD(TimeUtil.getCurrentTime())
        )
        val nextDayDate = TimeUtil.getNextDayDate(currentDate)
        if (checkCorrectDateRangeUseCase(nextDayDate)) {
            _selectedDate.value = TimeUtil.dateFormatToYYYYMMDD(nextDayDate)
            updateDiningData(nextDayDate)
        } else _isDateError.value = true
    }

    fun getPreviousDayDiningData() {
        val currentDate = TimeUtil.stringToDateYYYYMMDD(
            selectedDate.value ?: TimeUtil.dateFormatToYYYYMMDD(TimeUtil.getCurrentTime())
        )
        val previousDay = TimeUtil.getPreviousDayDate(currentDate)
        if (checkCorrectDateRangeUseCase(previousDay)) {
            _selectedDate.value = TimeUtil.dateFormatToYYYYMMDD(previousDay)
            updateDiningData(previousDay)
        } else _isDateError.value = true
    }

    fun dateErrorInit() { _isDateError.value = false }
}