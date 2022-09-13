package `in`.koreatech.koin.ui.dining.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningType
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
    private val diningUseCase: DiningUseCase
) : BaseViewModel() {
    private val _isDataLoaded = MutableLiveData<Boolean>()
    val isDataLoaded: LiveData<Boolean> get() = _isDataLoaded
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> get() = _selectedDate
    private val _diningData = MutableLiveData<List<Dining>>(listOf())
    val diningData: LiveData<List<Dining>> get() = _diningData
    var selectedType: DiningType = DiningUtil.getCurrentType()

    init {
        if (!DiningUtil.isNextDay()) _selectedDate.value =
            TimeUtil.dateFormatToYYYYMMDD(TimeUtil.getCurrentTime())
        else _selectedDate.value =
            TimeUtil.dateFormatToYYYYMMDD(TimeUtil.getNextDayDate(TimeUtil.getCurrentTime()))
    }

    fun updateDiningData(date: Date = TimeUtil.stringToDateYYYYMMDD(selectedDate.value!!)) {
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

    fun getNextDayDiningData(): Boolean {
        val dateDifference = TimeUtil.getDateDifferenceWithToday(TimeUtil.stringToDateYYYYMMDD(selectedDate.value!!))
        return if (dateDifference < 7) {
            val nextDayDate =
                TimeUtil.getNextDayDate(TimeUtil.stringToDateYYYYMMDD(selectedDate.value!!))
            _selectedDate.value = TimeUtil.dateFormatToYYYYMMDD(nextDayDate)
            updateDiningData(nextDayDate)
            true
        } else false
    }

    fun getPreviousDayDiningData(): Boolean {
        val dateDifference = TimeUtil.getDateDifferenceWithToday(TimeUtil.stringToDateYYYYMMDD(selectedDate.value!!))
        return if (dateDifference > -7) {
            val previousDay =
                TimeUtil.getPreviousDayDate(TimeUtil.stringToDateYYYYMMDD(selectedDate.value!!))
            _selectedDate.value = TimeUtil.dateFormatToYYYYMMDD(previousDay)
            updateDiningData(previousDay)
            true
        } else false
    }
}