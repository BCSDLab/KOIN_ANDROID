package `in`.koreatech.business.feature.signup.businessauth

import android.net.Uri
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.data.mapper.strToOwnerRegisterUrl
import `in`.koreatech.koin.domain.model.store.AttachStore
import `in`.koreatech.koin.domain.model.store.StoreUrl
import `in`.koreatech.koin.domain.usecase.owner.AttachStoreFileUseCase
import `in`.koreatech.koin.domain.usecase.owner.OwnerRegisterUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.UploadPreSignedUrlUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class BusinessAuthViewModel @Inject constructor(
    private val attachStoreFileUseCase: AttachStoreFileUseCase,
    private val uploadPreSignedUrlUseCase: UploadPreSignedUrlUseCase,
    private val ownerRegisterUseCase: OwnerRegisterUseCase
) : ContainerHost<BusinessAuthState, BusinessAuthSideEffect>, BaseViewModel() {
    override val container =
        container<BusinessAuthState, BusinessAuthSideEffect>(BusinessAuthState())

    fun onNameChanged(name: String) = intent {
        reduce {
            state.copy(name = name)
        }
    }

    fun onShopNameChanged(storeName: String) = intent {
        reduce {
            state.copy(storeName = storeName)
        }
    }

    fun onShopIdChanged(shopId: Int) = intent {
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

    fun presignedUrl(//url 생성
        uri: Uri,
        fileSize: Long,
        fileType: String,
        fileName: String,
        fileStream: InputStream
    ) {
        viewModelScope.launch {
            attachStoreFileUseCase(
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
                            }
                        )
                    }
                    reduce {
                        state.copy(inputStream = state.inputStream.toMutableList().apply {
                            add(fileStream)
                        })
                    }
                }
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

    fun uploadImage(//파일 업로드
        url: String,
        inputStream: InputStream,
        mediaType: String,
        mediaSize: Long
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            uploadPreSignedUrlUseCase(url, inputStream, mediaType, mediaSize).onSuccess {
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

    fun sendRegisterRequest(//등록 요청
        fileUrls: List<String>,
        companyNumber: String,
        email: String,
        name: String,
        password: String,
        phoneNumber: String,
        shopId: Int,
        shopName: String
    ) {
        viewModelScope.launch {
            ownerRegisterUseCase(
                fileUrls.strToOwnerRegisterUrl(), companyNumber, email, name, password, phoneNumber, shopId, shopName
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