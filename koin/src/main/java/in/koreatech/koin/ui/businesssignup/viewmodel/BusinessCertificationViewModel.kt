package `in`.koreatech.koin.ui.businesssignup.viewmodel

import android.util.Log
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.model.store.AttachStore
import `in`.koreatech.koin.domain.usecase.owner.AttachStoreFileUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusinessCertificationViewModel @Inject constructor(
    private val attachStoreFileUseCase: AttachStoreFileUseCase
): BaseViewModel() {
    private val _saveImageList = MutableLiveData<List<AttachStore>>(emptyList())
    val saveImageList: LiveData<List<AttachStore>>
        get() = _saveImageList

    private val _businessCertificationContinuationState = SingleLiveEvent<Unit>()
    val businessCertificationContinuationState: LiveData<Unit>
        get() = _businessCertificationContinuationState

    private val _businessCertificationContinuationError = SingleLiveEvent<Throwable>()
    val businessCertificationContinuationError: LiveData<Throwable>
        get() = _businessCertificationContinuationError

    private val _isMinOneInfo = MutableStateFlow(false)
    val isMinOneInfo = _isMinOneInfo.asStateFlow()

    fun addImageItem(imageUri: String, fileName: String) {
        viewModelScope.launch {
            val imageList = saveImageList.value?.toMutableList()?: mutableListOf()
            imageList.add(AttachStore(imageUri, fileName))
            _saveImageList.value = imageList
            _isMinOneInfo.value = true
        }
    }

    fun continueVCertification(
        fileSize: Long,
        fileType: String,
        fileName: String
    ) {
        viewModelScope.launch {
            attachStoreFileUseCase(
                fileSize, fileType, fileName
            ).onSuccess {
                _businessCertificationContinuationState.value = it
            }.onFailure {
                _businessCertificationContinuationError.value = it
            }
        }
    }
}