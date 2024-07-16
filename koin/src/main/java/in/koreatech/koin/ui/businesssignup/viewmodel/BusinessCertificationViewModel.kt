package `in`.koreatech.koin.ui.businesssignup.viewmodel

import android.net.Uri
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.core.viewmodel.SingleLiveEvent
import `in`.koreatech.koin.domain.model.store.AttachStore
import `in`.koreatech.koin.domain.usecase.owner.GetPresignedUrlUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.store.StoreUrl
import `in`.koreatech.koin.domain.usecase.presignedurl.UploadPreSignedUrlUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class BusinessCertificationViewModel @Inject constructor(
    private val getPresignedUrlUseCase: GetPresignedUrlUseCase,
    private val uploadPreSignedUrlUseCase: UploadPreSignedUrlUseCase
): BaseViewModel() {
    private val _saveImageList = MutableLiveData<List<AttachStore>>(emptyList())
    private val _shopImageUrlAndSize = MutableLiveData<StoreUrl>()
    private val _businessCertificationContinuationError = SingleLiveEvent<Throwable>()
    private val _isMinOneInfo = MutableStateFlow(false)
    private val _failUploadPreSignedUrl = SingleLiveEvent<Throwable>()

    val saveImageList: LiveData<List<AttachStore>> get() = _saveImageList
    val shopImageUrlAndSize: LiveData<StoreUrl> get() = _shopImageUrlAndSize
    val businessCertificationContinuationError: LiveData<Throwable> get() = _businessCertificationContinuationError
    val isMinOneInfo = _isMinOneInfo.asStateFlow()
    val failUploadPreSignedUrl: LiveData<Throwable> get() = _failUploadPreSignedUrl

    fun addImageItem(imageUri: String, fileName: String) {
        viewModelScope.launch {
            val imageList = saveImageList.value?.toMutableList()?: mutableListOf()
            imageList.add(AttachStore(imageUri, fileName))

            _saveImageList.value = imageList
            _isMinOneInfo.value = true
        }
    }

    fun uploadPreSignedUrl(
        url: String,
        inputStream: InputStream,
        mediaType: String,
        mediaSize: Long
    ) {
        viewModelScope.launch {
            uploadPreSignedUrlUseCase(url, inputStream, mediaType, mediaSize).onSuccess {
                /* Nothing happens  */
            }.onFailure {
                _failUploadPreSignedUrl.value = it
            }
        }
    }

    fun continueVCertification(
        uri: Uri,
        fileSize: Long,
        fileType: String,
        fileName: String
    ) {
        viewModelScope.launch {
            getPresignedUrlUseCase(
                fileSize, fileType, fileName
            ).onSuccess {
                _shopImageUrlAndSize.value = StoreUrl(uri.toString(), it.first, fileName, fileType, it.second, fileSize)
            }.onFailure {
                _businessCertificationContinuationError.value = it
            }
        }
    }
}