package `in`.koreatech.business.feature.signup.businessauth

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.business.feature.loading.LoadingState
import `in`.koreatech.business.feature.storemenu.modifymenu.modifymenu.ImageHolder
import `in`.koreatech.business.feature.storemenu.modifymenu.modifymenu.TEMP_IMAGE_URI
import `in`.koreatech.business.feature.storemenu.modifymenu.modifymenu.toStringList
import `in`.koreatech.business.util.getImageInfo
import `in`.koreatech.koin.data.mapper.strToOwnerRegisterUrl
import `in`.koreatech.koin.domain.constant.SIGN_UP_IMAGE_MAX
import `in`.koreatech.koin.domain.error.owner.OwnerError
import `in`.koreatech.koin.domain.model.store.AttachStore
import `in`.koreatech.koin.domain.model.store.StoreUrl
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.business.businessauth.CheckExistsCompanyNumberUseCase
import `in`.koreatech.koin.domain.usecase.owner.OwnerRegisterUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetMarketPreSignedUrlUseCase
import `in`.koreatech.koin.domain.util.ext.formatBusinessNumber
import `in`.koreatech.koin.domain.util.onFailure
import `in`.koreatech.koin.domain.util.onSuccess
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class BusinessAuthViewModel @Inject constructor(
    private val getPresignedUrlUseCase: GetMarketPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase,
    private val ownerRegisterUseCase: OwnerRegisterUseCase,
    private val checkCompanyNumberUseCase: CheckExistsCompanyNumberUseCase
) : ContainerHost<BusinessAuthState, BusinessAuthSideEffect>, ViewModel() {
    override val container =
        container<BusinessAuthState, BusinessAuthSideEffect>(BusinessAuthState())

    fun onNameChanged(name: String) =
        blockingIntent {
            reduce {
                state.copy(name = name)
            }
        }

    fun onShopNameChanged(storeName: String) =
        blockingIntent {
            reduce {
                state.copy(shopName = storeName)
            }
        }

    fun onShopIdChanged(shopId: Int?) =
        blockingIntent {
            reduce {
                state.copy(shopId = shopId)
            }
        }

    fun onShopNumberChanged(shopNumber: String) =
        blockingIntent {
            reduce {
                state.copy(
                    shopNumber = shopNumber
                )
            }
        }

    fun onCompanyNumberChanged(companyNumber: String) =
        blockingIntent {
            reduce {
                state.copy(
                    companyNumber = if (companyNumber.length <= 10) companyNumber else state.companyNumber
                )
            }
            if (companyNumber.length == 10) {
                viewModelScope.launch {
                    checkCompanyNumberUseCase(companyNumber).onSuccess {
                        reduce {
                            state.copy(
                                error = null,
                                signupContinuationState = SignupContinuationState.CheckComplete
                            )
                        }
                    }.onFailure {
                        if (it is OwnerError.CompanyNumberIsDuplicatedException) {
                            reduce {
                                state.copy(
                                    error = it,
                                    signupContinuationState = SignupContinuationState.CompanyNumberIsDuplicated
                                )
                            }
                        } else {
                            reduce {
                                state.copy(
                                    error = it,
                                    signupContinuationState = SignupContinuationState.Failed()
                                )
                            }
                        }
                    }
                }
            }
        }

    fun onImageUrlsChanged(selectedImages: MutableList<AttachStore>) =
        intent {
            reduce {
                state.copy(selectedImages = selectedImages)
            }
        }

    fun onDialogVisibilityChanged(dialogVisibility: Boolean) =
        intent {
            reduce {
                state.copy(dialogVisibility = dialogVisibility)
            }
        }

    fun onNavigateToSearchStore() =
        intent {
            postSideEffect(BusinessAuthSideEffect.NavigateToSearchStore)
        }

    fun onNavigateToBackScreen() =
        intent {
            postSideEffect(BusinessAuthSideEffect.NavigateToBackScreen)
        }

    fun onNavigateToNextScreen() =
        intent {
            postSideEffect(BusinessAuthSideEffect.NavigateToNextScreen)
        }

    fun changeImageUri(
        context: Context,
        uriList: List<Uri>
    ) {
        intent {
            reduce {
                if (uriList.size <= SIGN_UP_IMAGE_MAX) {
                    val newMenuUriList = state.imageUriList.toMutableList()
                    for (imageUri in uriList) {
                        newMenuUriList.add(imageUri.toString())
                        insertStoreFileUrl(
                            imageUri.toString().substringAfterLast("/"),
                            imageUri.toString()
                        )
                    }
                    if (newMenuUriList.size <= SIGN_UP_IMAGE_MAX) newMenuUriList.add(ImageHolder.TempUri.toString())

                    state.copy(
                        imageUriList = newMenuUriList
                    )
                } else {
                    state.copy(
                        imageUriList = uriList.toStringList()
                    )
                }
            }
            uploadImageList(context)
        }
    }

    private fun uploadImageList(context: Context) {
        intent {
            viewModelScope.launch {
                state.imageUriList.forEach { uriString ->
                    if (uriString != TEMP_IMAGE_URI) {
                        if (uriString.contains("content")) {
                            val uri = Uri.parse(uriString)
                            val imageInfo = getImageInfo(context, uri)
                            getPreSignedUrl(
                                fileSize = imageInfo.imageSize,
                                fileType = imageInfo.imageType,
                                fileName = imageInfo.imageName,
                                imageUri = uriString
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getPreSignedUrl(
        fileSize: Long,
        fileType: String,
        fileName: String,
        imageUri: String
    ) {
        viewModelScope.launch {
            LoadingState.show()
            getPresignedUrlUseCase(
                fileSize,
                fileType,
                fileName
            ).onSuccess { (resultUrl, preSignedUrl) ->
                uploadFilesUseCase(
                    preSignedUrl = preSignedUrl,
                    mediaType = fileType,
                    mediaSize = fileSize,
                    imageUri = imageUri
                ).onSuccess {
                    intent {
                        reduce {
                            state.copy(
                                fileInfo =
                                state.fileInfo.toMutableList().apply {
                                    add(
                                        StoreUrl(
                                            imageUri,
                                            resultUrl,
                                            fileName,
                                            fileType,
                                            preSignedUrl,
                                            fileSize
                                        )
                                    )
                                },
                                signupContinuationState = SignupContinuationState.SuccessUploadFiles,
                                error = null
                            )
                        }
                    }
                }.onFailure {
                    intent {
                        reduce { state.copy(error = it) }
                    }
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
        shopNumber: String,
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
                shopNumber,
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

    private fun insertStoreFileUrl(
        title: String,
        url: String
    ) {
        intent {
            reduce {
                state.copy(
                    selectedImages =
                    state.selectedImages.apply {
                        add(AttachStore(url, title))
                    }
                )
            }
        }
    }

    fun initStoreImageUrls() =
        intent {
            reduce {
                state.copy(
                    fileInfo = mutableListOf()
                )
            }
        }
}
