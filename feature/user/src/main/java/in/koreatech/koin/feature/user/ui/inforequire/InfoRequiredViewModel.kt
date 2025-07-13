package `in`.koreatech.koin.feature.user.ui.inforequire

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class InfoRequiredViewModel @Inject constructor() : ViewModel() {

    private val _event = Channel<UiEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    fun onPositiveClick() {
        emit(UiEvent.NavigateToUserInfo)
    }

    fun onNegativeClick() {
        emit(UiEvent.Finish)
    }

    private fun emit(event: UiEvent) {
        viewModelScope.launch {
            _event.send(event)
        }
    }

    sealed class UiEvent {
        object NavigateToUserInfo : UiEvent()
        object Finish : UiEvent()
    }
}
