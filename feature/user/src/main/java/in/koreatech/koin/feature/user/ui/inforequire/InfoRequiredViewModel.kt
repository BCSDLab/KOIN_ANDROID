package `in`.koreatech.koin.feature.user.ui.inforequire

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.repository.ModalRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoRequiredViewModel @Inject constructor(
    private val modalRepository: ModalRepository
) : ViewModel() {

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    fun onStart(isFull: Boolean) {
        if (isFull) {
            modalRepository.setInfoRequiredShown(true)
        }
    }

    fun onPositiveClick() {
        emit(UiEvent.NavigateToUserInfo)
    }

    fun onNegativeClick() {
        emit(UiEvent.Finish)
    }

    private fun emit(event: UiEvent) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    sealed class UiEvent {
        object NavigateToUserInfo : UiEvent()
        object Finish : UiEvent()
    }
}
