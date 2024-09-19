package `in`.koreatech.business.feature.store.modifyinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chargemap.compose.numberpicker.FullHours
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.owner.StoreDetailInfo
import `in`.koreatech.koin.domain.model.store.StoreUrl
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.business.store.ModifyShopInfoUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetMarketPreSignedUrlUseCase
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ModifyInfoViewModel @Inject constructor(
    private val getPresignedUrlUseCase: GetMarketPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase,
    private val modifyInfoUseCase: ModifyShopInfoUseCase,
) : ViewModel(),
    ContainerHost<ModifyInfoState, ModifyInfoSideEffect> {
    override val container = container<ModifyInfoState, ModifyInfoSideEffect>(ModifyInfoState()) {}

    fun initStoreInfo(storeInfo: StoreDetailInfo) = intent {
        reduce {
            state.copy(storeInfo = storeInfo)
        }
    }

    fun onBackButtonClicked() = intent {
        postSideEffect(ModifyInfoSideEffect.NavigateToBackScreen)
    }

    fun onSettingOperatingTimeClicked() = intent {
        postSideEffect(ModifyInfoSideEffect.NavigateToSettingOperatingTime)
    }

    fun showAlertDialog(index: Int) = intent {
        reduce {
            state.copy(
                showDialog = true,
                dayOfWeekIndex = index,
            )
        }
    }

    fun hideAlertDialog() = intent {
        reduce {
            state.copy(
                showDialog = false,
            )
        }
    }

    fun initDialogTimeSetting(openTime: String, closeTime: String) = intent {
        reduce {
            if (state.dayOfWeekIndex < 0) return@reduce state
            val openTimeParts = openTime.split(":").map { it.toInt() }
            val closeTimeParts = closeTime.split(":").map { it.toInt() }
            state.copy(
                dialogTimeState = OperatingTime(
                    FullHours(openTimeParts[0], openTimeParts[1]),
                    FullHours(closeTimeParts[0], closeTimeParts[1])
                )
            )
        }
    }

    fun onSettingStoreTime(openTime: FullHours, closeTime: FullHours) = intent {
        reduce {
            state.copy(dialogTimeState = OperatingTime(openTime, closeTime),
                storeInfo = state.storeInfo.copy(
                    operatingTime = state.storeInfo.operatingTime.mapIndexed { index, operatingTime ->
                        if (index == state.dayOfWeekIndex) {
                            operatingTime.copy(
                                openTime = String.format(
                                    "%02d:%02d",
                                    openTime.hours,
                                    openTime.minutes
                                ),
                                closeTime = String.format(
                                    "%02d:%02d",
                                    closeTime.hours,
                                    closeTime.minutes
                                )
                            )
                        } else {
                            operatingTime
                        }
                    }
                )
            )
        }
    }

    fun onCardAvailableChanged() = intent {
        reduce {
            state.copy(
                storeInfo = state.storeInfo.copy(
                    isCardOk = !(state.storeInfo.isCardOk)
                )
            )

        }
    }

    fun onDeliveryAvailableChanged() = intent {
        reduce {
            state.copy(
                storeInfo = state.storeInfo.copy(
                    isDeliveryOk = !(state.storeInfo.isDeliveryOk)
                )
            )
        }
    }

    fun onTransferAvailableChanged() = intent {
        reduce {
            state.copy(
                storeInfo = state.storeInfo.copy(
                    isBankOk = !(state.storeInfo.isBankOk)
                )
            )
        }
    }

    fun onStoreNameChanged(storeName: String) = intent {
        reduce {
            state.copy(storeInfo = state.storeInfo.copy(name = storeName))
        }
    }

    fun onPhoneNumberChanged(phone: String) = intent {
        reduce {
            state.copy(storeInfo = state.storeInfo.copy(phone = phone))
        }
    }

    fun onAddressChanged(address: String) = intent {
        reduce {
            state.copy(storeInfo = state.storeInfo.copy(address = address))
        }
    }

    fun onDeliveryPriceChanged(price: Int) = intent {
        reduce {
            state.copy(storeInfo = state.storeInfo.copy(deliveryPrice = price))
        }
    }

    fun onDescriptionChanged(description: String) = intent {
        reduce {
            state.copy(storeInfo = state.storeInfo.copy(description = description))
        }
    }

    fun isClosedDay(index: Int) {
        intent {
            if (index >= 0 && index <= state.storeInfo.operatingTime.size) {
                val newList = state.storeInfo.operatingTime.toMutableList()
                val currentItem = newList[index]
                newList[index] = currentItem.copy(closed = !currentItem.closed)
                reduce {
                    state.copy(storeInfo = state.storeInfo.copy(operatingTime = newList))
                }
            }
        }
    }

    fun modifyStoreInfo(storeId: Int, storeDetailInfo: StoreDetailInfo) {
        viewModelScope.launch {
            modifyInfoUseCase.invoke(
                storeId,
                storeDetailInfo,
            )
        }
    }


    fun getPreSignedUrl(
        fileSize: Long,
        fileType: String,
        fileName: String,
        imageUri: String
    ) {
        viewModelScope.launch {
            getPresignedUrlUseCase(
                fileSize, fileType, fileName
            ).onSuccess {
                uploadImage(
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
                        )
                    }
                }
            }.onFailure {
                intent {
                    postSideEffect(ModifyInfoSideEffect.ShowToastMessage)
                }
            }
        }
    }

    private fun uploadImage(
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
                insertStoreFileUrl(fileUrl)
            }.onFailure {
                intent {
                    postSideEffect(ModifyInfoSideEffect.ShowToastMessage)
                }
            }
        }
    }

    private fun insertStoreFileUrl(url: String) {
        intent {
            reduce {
                state.copy(
                    storeInfo = state.storeInfo.copy(
                        imageUrls = state.storeInfo.imageUrls.toMutableList().apply {
                            add(url)
                        }
                    ),
                )
            }
        }
    }

    fun initStoreImageUrls() = intent {
        reduce {
            state.copy(
                storeInfo = state.storeInfo.copy(
                    imageUrls = emptyList()
                )
            )
        }
    }
}
