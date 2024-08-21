package `in`.koreatech.koin.ui.businesssignup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.owner.OwnerRegisterUrl
import `in`.koreatech.koin.domain.usecase.owner.OwnerEmailRegisterUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusinessSignUpCompleteViewModel @Inject constructor(
    private val ownerEmailRegisterUseCase: OwnerEmailRegisterUseCase,
): BaseViewModel() {
    private val _businessCompleteContinuationState = MutableLiveData<Boolean>()
    val businessCompleteContinuationState: LiveData<Boolean> get() = _businessCompleteContinuationState

    private val _businessCompleteContinuationError = MutableLiveData<Throwable>()
    val businessCompleteContinuationError: LiveData<Throwable> get() = _businessCompleteContinuationError

    fun sendRegisterRequest(
        attachments: List<OwnerRegisterUrl>,
        companyNumber: String,
        email: String,
        name: String,
        password: String,
        phoneNumber: String,
        shopId: Int,
        shopName: String
    ) {
        viewModelScope.launch {
            ownerEmailRegisterUseCase(
                attachments, companyNumber, email, name, password, phoneNumber, shopId, shopName
            ).onSuccess {
                _businessCompleteContinuationState.value = true
            }.onFailure {
                _businessCompleteContinuationError.value = it
            }
        }
    }
}