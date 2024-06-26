package `in`.koreatech.business.feature.signup.businessauth

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.data.mapper.strToOwnerRegisterUrl
import `in`.koreatech.koin.domain.model.store.AttachStore
import `in`.koreatech.koin.domain.model.store.StoreUrl
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.owner.AttachStoreFileUseCase
import `in`.koreatech.koin.domain.usecase.owner.OwnerRegisterUseCase
import `in`.koreatech.koin.domain.util.ext.formatBusinessNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class BusinessAuthViewModel @Inject constructor(
    private val getPresignedUrlUseCase: AttachStoreFileUseCase,
    private val uploadFilesUseCase: UploadFileUseCase,
    private val ownerRegisterUseCase: OwnerRegisterUseCase
) : ContainerHost<BusinessAuthState, BusinessAuthSideEffect>, ViewModel() {
    override val container =
        container<BusinessAuthState, BusinessAuthSideEffect>(BusinessAuthState())

    fun onNameChanged(name: String) = intent {
        reduce {
            state.copy(name = name)
        }
    }

    fun onShopNameChanged(storeName: String) = intent {
        reduce {
            state.copy(shopName = storeName)
        }
    }

    fun onShopIdChanged(shopId: Int?) = intent {
        reduce {
            state.copy(shopId = shopId)
        }
    }

    fun onStoreNumberChanged(storeNumber: String) = intent {
        reduce {
            state.copy(shopNumber = storeNumber)
        }
    }

    fun onPhoneNumberChanged(phoneNumber: String) = intent {
        reduce {
            state.copy(phoneNumber = phoneNumber)
        }
    }

    fun onImageUrlsChanged(selectedImages: MutableList<AttachStore>) = intent {
        reduce {
            state.copy(selectedImages = selectedImages)
        }
    }

    fun onDialogVisibilityChanged(dialogVisibility: Boolean) = intent {
        reduce {
            state.copy(dialogVisibility = dialogVisibility)
        }
    }

    fun onNavigateToSearchStore() = intent {
        postSideEffect(BusinessAuthSideEffect.NavigateToSearchStore)
    }

    fun onNavigateToBackScreen() = intent {
        postSideEffect(BusinessAuthSideEffect.NavigateToBackScreen)
    }

    fun onNavigateToNextScreen() = intent {
        postSideEffect(BusinessAuthSideEffect.NavigateToNextScreen)
    }

    fun getPreSignedUrl(
        uri: Uri,
        fileSize: Long,
        fileType: String,
        fileName: String,
        bitmap: Bitmap
    ) {
        viewModelScope.launch {
            getPresignedUrlUseCase(
                fileSize, fileType, fileName
            ).onSuccess {
                intent {
                    reduce {
                        state.copy(
                            fileInfo = state.fileInfo.toMutableList().apply {
                                add(
                                    StoreUrl(
                                        uri.toString(),
                                        it.first,
                                        fileName,
                                        fileType,
                                        it.second,
                                        fileSize
                                    )
                                )
                            },
                            bitmap = state.bitmap.toMutableList().apply {
                                add(bitmap)
                            },
                            error = null
                        )
                    }
                }
            }.onFailure {
                intent {
                    reduce { state.copy(error = it) }
                }
            }
        }
    }

    fun uploadImage(
        url: String,
        bitmap: Bitmap,
        mediaType: String,
        mediaSize: Long
    ) {
        val byteArrayOutputStream = ByteArrayOutputStream()

        when(mediaType){
            "image/jpeg" -> bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            "image/jpg" -> bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            "image/png" -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            "image/webp" -> bitmap.compress(Bitmap.CompressFormat.WEBP, 100, byteArrayOutputStream)
            "image/bmp" -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        }

        val bitmapByteArray = byteArrayOutputStream.toByteArray()

        viewModelScope.launch(Dispatchers.IO) {
            uploadFilesUseCase(url, bitmapByteArray, mediaType, mediaSize).onSuccess {
                intent {
                    reduce { state.copy(error = null) }
                }
            }.onFailure {
                intent {
                    reduce { state.copy(error = it) }
                }
            }
        }
    }

    fun sendRegisterRequest(
        fileUrls: List<String>,
        companyNumber: String,
        email: String,
        name: String,
        password: String,
        phoneNumber: String,
        shopId: Int?,
        shopName: String
    ) {
        viewModelScope.launch {
            ownerRegisterUseCase(
                fileUrls.strToOwnerRegisterUrl(),
                companyNumber.formatBusinessNumber(),
                email,
                name,
                password,
                phoneNumber,
                shopId,
                shopName
            ).onSuccess {
                onNavigateToNextScreen()
                intent {
                    reduce { state.copy(continuation = true) }
                }
            }.onFailure {
                intent {
                    reduce { state.copy(error = it) }
                }
            }

        }
    }
}