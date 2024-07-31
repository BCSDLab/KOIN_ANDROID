package `in`.koreatech.koin.ui.store.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.store.Review
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetMarketPreSignedUrlUseCase
import `in`.koreatech.koin.domain.usecase.store.WriteReviewUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteReviewViewModel @Inject constructor(
    private val writeReviewUseCase: WriteReviewUseCase,
    private val getMarketPreSignedUrlUseCase: GetMarketPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase
) : BaseViewModel() {
    private val _review = MutableStateFlow<Review?>(null)
    val review = _review.asStateFlow()

    private val _menuImageUrls = MutableStateFlow<List<String>>(emptyList())
    val menuImageUrls: StateFlow<List<String>> get() = _menuImageUrls.asStateFlow()

    fun writeReview(storeId: Int, content: Review) {
        viewModelScope.launch {
            writeReviewUseCase(storeId, content).also {
                _review.value = content
            }
        }

    }

    fun getPreSignedUrl(
        fileSize: Long,
        fileType: String,
        fileName: String,
        imageUri: String
    ) {
        viewModelScope.launch {
            getMarketPreSignedUrlUseCase(
                fileSize, fileType, fileName
            ).onSuccess {
                _menuImageUrls.value = emptyList()
                uploadImage(
                    preSignedUrl = it.second,
                    fileUrl = it.first,
                    mediaType = fileType,
                    mediaSize = fileSize,
                    imageUri = imageUri
                )
            }.onFailure {
                _menuImageUrls.value = emptyList()
            }

        }
    }

    private fun uploadImage(
        preSignedUrl: String,
        fileUrl: String,
        mediaType: String,
        mediaSize: Long,
        imageUri: String
    ) {
        viewModelScope.launch {
            uploadFilesUseCase(
                preSignedUrl,
                mediaType,
                mediaSize,
                imageUri
            ).onSuccess {
                _menuImageUrls.value += fileUrl
            }.onFailure {
                _menuImageUrls.value = emptyList()
            }
        }
    }

    fun deleteMenuImage(position: Int) {
        val list = _menuImageUrls.value.toMutableList()
        list.removeAt(position)
        _menuImageUrls.value = list
    }
}
