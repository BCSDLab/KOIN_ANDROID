package `in`.koreatech.koin.ui.changepassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(

    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val currentStep: StateFlow<ChangePasswordPage> = savedStateHandle.getStateFlow(KEY_CURRENT_PAGE, ChangePasswordPage.Verify)

    fun setCurrentPage(newPage: ChangePasswordPage) {
        savedStateHandle[KEY_CURRENT_PAGE] = newPage
    }

    fun changePrevPage() {
        savedStateHandle[KEY_CURRENT_PAGE] = currentStep.value.prevPage()
    }

    fun changeNextPage() {
        savedStateHandle[KEY_CURRENT_PAGE] = currentStep.value.nextPage()
    }

    private companion object {
        const val KEY_CURRENT_PAGE = "current_page"
    }
}