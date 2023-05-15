package `in`.koreatech.koin.ui.businessmain.viewmodel

import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.ui.businessmain.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BusinessMainViewModel() : BaseViewModel() {
    private val _selectedMenuItems = MutableLiveData<List<MenuItem>>()
    val selectedMenuItems: LiveData<List<MenuItem>>
            get() = _selectedMenuItems

    private val _currentDateTime = MutableLiveData<String>()
    val currentDateTime: LiveData<String> get() = _currentDateTime

    var job: Job? = null

    init {
        startUpdatingDateTime()
    }

    fun startUpdatingDateTime() {
        job = viewModelScope.launch {
            while (true) {
                _currentDateTime.value = getCurrentDateTime()
                delay(60_000)
            }
        }
    }

    fun stopUpdatingDateTime() {
        job?.cancel()
    }

    fun updateSelectedItems(selectedItems: List<MenuItem>) {
        _selectedMenuItems.value = selectedItems
    }

    fun getSelectedItems(): List<MenuItem> {
        return selectedMenuItems.value?.filter { it.isSelected } ?: emptyList()
    }

    private fun getCurrentDateTime(): String {
        val currentDate = Calendar.getInstance().time
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd a hh:mm", Locale.getDefault())
        return dateFormatter.format(currentDate)
    }
}