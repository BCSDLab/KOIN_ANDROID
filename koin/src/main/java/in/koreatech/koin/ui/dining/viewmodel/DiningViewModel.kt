package `in`.koreatech.koin.ui.dining.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.usecase.dining.GetDiningUseCase
import `in`.koreatech.koin.domain.usecase.user.GetUserStatusUseCase
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DiningViewModel @Inject constructor(
    private val getDiningUseCase: GetDiningUseCase,
    private val getUserStatusUseCase: GetUserStatusUseCase
) : BaseViewModel() {

    val userState: StateFlow<User?>
        get() = getUserStatusUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val _selectedDate =
        MutableStateFlow(TimeUtil.dateFormatToYYMMDD(DiningUtil.getCurrentDate()))
    val selectedDate: StateFlow<String> get() = _selectedDate

    private val _dining =
        MutableStateFlow<List<Dining>>(emptyList())
    val dining: StateFlow<List<Dining>> get() = _dining

    fun setSelectedDate(date: Date) {
        _selectedDate.value = TimeUtil.dateFormatToYYMMDD(date)
        getDining(selectedDate.value)
    }

    fun getDining(
        date: String = selectedDate.value
    ) {
        if (isLoading.value == false) {
            viewModelScope.launchWithLoading {
                getDiningUseCase(date)
                    .onSuccess {
                        _dining.value = it
                    }
                    .onFailure {

                    }
            }
        }
    }
}