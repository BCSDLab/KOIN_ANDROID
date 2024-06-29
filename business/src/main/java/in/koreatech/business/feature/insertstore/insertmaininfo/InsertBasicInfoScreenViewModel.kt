package `in`.koreatech.business.feature.insertstore.insertmaininfo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.upload.toCompressJPEG
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.owner.AttachStoreFileUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetMarketPreSignedUrlUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.UploadPreSignedUrlUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class InsertBasicInfoScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMarketPreSignedUrlUseCase: GetMarketPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase
): ViewModel(), ContainerHost<InsertBasicInfoScreenState, InsertBasicInfoScreenSideEffect> {
    override val container: Container<InsertBasicInfoScreenState,InsertBasicInfoScreenSideEffect> =
        container(InsertBasicInfoScreenState(), savedStateHandle = savedStateHandle){
            val categoryId = savedStateHandle.get<Int>("categoryId")
            checkNotNull(categoryId)
            getCategoryId(categoryId)
        }

    fun insertStoreName(storeName: String) = intent {
        reduce {
            state.copy(storeName = storeName)
        }
        isBasicInfoValidate()
    }

    fun insertStoreAddress(storeAddress: String) = intent{
        reduce {
            state.copy(storeAddress = storeAddress)
        }
        isBasicInfoValidate()
    }

    fun insertStoreImage(storeImage: Uri) = intent{
        reduce {
            state.copy(storeImage = storeImage)
        }
        isBasicInfoValidate()
        storeImageIsEmpty()
    }

    fun getPreSignedUrl(
        fileSize: Long,
        fileType: String,
        fileName: String,
        imageUri: String
    ) {
        intent {
            if(state.storeImagePreSignedUrl == ""){
                viewModelScope.launch{
                    getMarketPreSignedUrlUseCase(
                        fileSize, fileType, fileName
                    ).onSuccess {
                        uploadImage(
                            preSignedUrl = it.second,
                            fileUrl = it.first,
                            mediaType = fileType,
                            mediaSize = fileSize,
                            imageUri = imageUri
                        )
                    }.onFailure {
                        failUploadImage()
                    }
                }
            }
            else{
                uploadImage(
                    preSignedUrl = state.storeImagePreSignedUrl,
                    fileUrl = state.storeImageFileUrl,
                    mediaType = fileType,
                    mediaSize = fileSize,
                    imageUri = imageUri
                )
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
        viewModelScope.launch{
            uploadFilesUseCase(
                preSignedUrl,
                mediaType,
                mediaSize,
                imageUri
            ).onSuccess {
                insertStorePreSignedUrl(preSignedUrl)
                insertStoreFileUrl(fileUrl)
            }.onFailure {
                failUploadImage()
            }
        }
    }

    fun onNextButtonClick(){
        intent{
            if(state.isBasicInfoValid){
                val storeBasicInfo = InsertBasicInfoScreenState(
                    storeName = state.storeName,
                    storeAddress = state.storeAddress,
                    storeImageFileUrl = state.storeImageFileUrl,
                    storeCategory = state.storeCategory
                )
                postSideEffect(InsertBasicInfoScreenSideEffect.NavigateToInsertDetailInfoScreen(storeBasicInfo))
                return@intent
            }

                when {
                    state.storeImageIsEmpty -> postSideEffect(InsertBasicInfoScreenSideEffect.ShowMessage(BasicInfoErrorType.NullStoreImage))
                    state.storeName.isEmpty() -> postSideEffect(InsertBasicInfoScreenSideEffect.ShowMessage(BasicInfoErrorType.NullStoreName))
                    state.storeAddress.isEmpty() -> postSideEffect(InsertBasicInfoScreenSideEffect.ShowMessage(BasicInfoErrorType.NullStoreAddress))
                }
        }
    }

    private fun failUploadImage() = intent{
        postSideEffect(InsertBasicInfoScreenSideEffect.ShowMessage(BasicInfoErrorType.FailUploadImage))
    }

    private fun storeImageIsEmpty() = intent{
        reduce {
            state.copy(storeImageIsEmpty = state.storeImage == Uri.EMPTY)
        }
        isBasicInfoValidate()
    }

    private fun getCategoryId(id: Int){
        intent{
            reduce {
                state.copy(
                    storeCategory = id
                )
            }
        }
    }

    private fun insertStorePreSignedUrl(url: String){
        intent{
            reduce {
                state.copy(
                    storeImagePreSignedUrl = url
                )
            }
        }
    }

    private fun insertStoreFileUrl(url: String){
        intent{
            reduce {
                state.copy(
                    storeImageFileUrl = url
                )
            }
        }
    }

    private fun isBasicInfoValidate() = intent {
        reduce {
            state.copy(
                isBasicInfoValid = state.storeAddress.isNotBlank() && state.storeName.isNotBlank() && state.storeImage != Uri.EMPTY
            )
        }
    }

}