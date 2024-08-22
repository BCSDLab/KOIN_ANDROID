package `in`.koreatech.business.feature.signup.businessauth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.data.mapper.strToOwnerRegisterUrl
import `in`.koreatech.koin.domain.model.store.AttachStore
import `in`.koreatech.koin.domain.model.store.StoreUrl
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.owner.OwnerRegisterUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetMarketPreSignedUrlUseCase
import `in`.koreatech.koin.domain.util.ext.formatBusinessNumber
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class BusinessAuthViewModel @Inject constructor(
    private val getPresignedUrlUseCase: GetMarketPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase,
    private val ownerRegisterUseCase: OwnerRegisterUseCase,
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
            state.copy(
                shopNumber = storeNumber,
                signupContinuationState = if (storeNumber.length != 10) SignupContinuationState.BusinessNumberIsNotValidate
                else SignupContinuationState.RequestedSmsValidation
            )
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
        fileSize: Long,
        fileType: String,
        fileName: String,
        imageUri: String,
    ) {
        viewModelScope.launch {
            getPresignedUrlUseCase(
                fileSize, fileType, fileName
            ).onSuccess {
                uploadImage(
                    title = fileName.substringAfterLast("/"),
                    preSignedUrl = it.second,
                    mediaType = fileType,
                    mediaSize = fileSize,
                    imageUri = imageUri,
                    fileUrl = it.first,
                )
                intent {
                    reduce {
                        state.copy(
                            fileInfo = state.fileInfo.toMutableList().apply {
                                add(
                                    StoreUrl(
                                        imageUri,
                                        it.first,
                                        fileName,
                                        fileType,
                                        it.second,
                                        fileSize
                                    )
                                )
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

    private fun uploadImage(
        title: String,
        fileUrl: String,
        preSignedUrl: String,
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
                insertStoreFileUrl(title, fileUrl)
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
        phoneNumber: String,
        name: String,
        password: String,
        shopId: Int?,
        shopName: String
    ) {
        viewModelScope.launch {
            ownerRegisterUseCase(
                fileUrls.strToOwnerRegisterUrl(),
                companyNumber.formatBusinessNumber(),
                name,
                password,
                phoneNumber,
                shopId,
                shopName
            ).onSuccess {
                onNavigateToNextScreen()
                intent {
                    reduce { state.copy(signupContinuationState = SignupContinuationState.RequestedOwnerRegister) }
                }
            }.onFailure {
                intent {
                    reduce { state.copy(signupContinuationState = SignupContinuationState.Failed(it.message)) }
                }
            }
        }
    }


    private fun insertStoreFileUrl(title: String, url: String) {
        intent {
            reduce {
                state.copy(
                    selectedImages = state.selectedImages.apply {
                        add(AttachStore(url, title))
                    },
                )
            }
        }
    }


    fun initStoreImageUrls() = intent {
        reduce {
            state.copy(
                fileInfo = mutableListOf()
            )
        }
    }
}