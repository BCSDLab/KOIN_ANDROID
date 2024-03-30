package `in`.koreatech.koin.ui.businesssignup.viewmodel

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BusinessSignUpCheckViewModel: BaseViewModel() {
    private val _allCheckButtonState = MutableStateFlow(R.drawable.check)
    val allCheckButtonState = _allCheckButtonState.asStateFlow()

    private val _agreedPrivacyTermsButtonState = MutableStateFlow(R.drawable.check)
    val agreedPrivacyTermsButtonState = _agreedPrivacyTermsButtonState.asStateFlow()

    private val _agreedKoinTermsButtonState = MutableStateFlow(R.drawable.check)
    val agreedKoinTermsButtonState = _agreedKoinTermsButtonState.asStateFlow()

    fun updateButtonState(image: Int, kind: Int) {
        // kind: 1 -> allCheckButtonState, 2 -> agreedPrivacyButtonState, 3 -> agreedKoinTermsButtonState
        when(kind) {
            1 -> _allCheckButtonState.value = image
            2 -> _agreedPrivacyTermsButtonState.value = image
            3 -> _agreedKoinTermsButtonState.value = image
        }
    }
}