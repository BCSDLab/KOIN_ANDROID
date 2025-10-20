package `in`.koreatech.business.feature.store.modifyinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chargemap.compose.numberpicker.Hours
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.owner.SettingTime
import `in`.koreatech.koin.domain.model.owner.StoreDetailInfo
import `in`.koreatech.koin.domain.model.store.StoreUrl
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.business.store.ModifyShopInfoUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetMarketPreSignedUrlUseCase
import javax.inject.Inject
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ModifyInfoViewModel @Inject constructor(
    private val getPresignedUrlUseCase: GetMarketPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase,
    private val modifyInfoUseCase: ModifyShopInfoUseCase
) : ViewModel(),
    ContainerHost<ModifyInfoState, ModifyInfoSideEffect> {
    override val container = container<ModifyInfoState, ModifyInfoSideEffect>(ModifyInfoState()) {}

    fun initStoreInfo(storeInfo: StoreDetailInfo) =
        intent {
            reduce {
                state.copy(storeInfo = storeInfo)
            }
            initStoreTimeList()
        }

    private fun initStoreTimeList() {
        intent {
            reduce {
                val newList = state.storeInfo.operatingTime?.toMutableList()
                state.copy(
                    operatingTimeList = newList ?: listOf()
                )
            }
        }
    }

    private fun isOpenTimeSetting(openTimeSetting: SettingTime) =
        intent {
            reduce {
                state.copy(isOpenTimeSetting = openTimeSetting)
            }
        }

    private fun dayOfIndex(index: Int) =
        intent {
            reduce {
                state.copy(dayOfWeekIndex = index)
            }
        }

    private fun modifyStoreTime() {
        intent {
            val newList = state.operatingTimeList.toMutableList()
            reduce {
                state.copy(
                    storeInfo =
                    state.storeInfo.copy(
                        operatingTime = newList
                    )
                )
            }
        }
    }

    fun onBackButtonClicked() =
        intent {
            postSideEffect(ModifyInfoSideEffect.NavigateToBackScreen)
        }

    fun onSettingOperatingTimeClicked() =
        intent {
            postSideEffect(ModifyInfoSideEffect.NavigateToSettingOperatingTime)
        }

    fun hideAlertDialog() =
        intent {
            reduce {
                state.copy(
                    showDialog = false
                )
            }
        }

    fun settingStoreOpenTime(
        time: Hours,
        index: Int
    ) {
        intent {
            if (index >= 0 && index < state.operatingTimeList.size) {
                val newList = state.operatingTimeList.toMutableList()
                val currentItem = newList[index]
                newList[index] = currentItem.copy(openTime = time.toTimeString())

                reduce {
                    state.copy(operatingTimeList = newList)
                }
                modifyStoreTime()
                closeDialog()
            }
        }
    }

    fun settingStoreCloseTime(
        time: Hours,
        index: Int
    ) {
        intent {
            if (index >= 0 && index < state.operatingTimeList.size) {
                val newList = state.operatingTimeList.toMutableList()
                val currentItem = newList[index]
                newList[index] = currentItem.copy(closeTime = time.toTimeString())

                reduce {
                    state.copy(operatingTimeList = newList)
                }
                modifyStoreTime()
                closeDialog()
            }
        }
    }

    fun showOpenTimeDialog(index: Int) =
        intent {
            reduce {
                state.copy(showDialog = true)
            }
            isOpenTimeSetting(SettingTime.OPEN)
            dayOfIndex(index)
        }

    fun showCloseTimeDialog(index: Int) =
        intent {
            reduce {
                state.copy(showDialog = true)
            }
            isOpenTimeSetting(SettingTime.CLOSE)
            dayOfIndex(index)
        }

    private fun closeDialog() =
        intent {
            reduce {
                state.copy(showDialog = false)
            }
        }

    fun onCardAvailableChanged() =
        intent {
            reduce {
                state.copy(
                    storeInfo =
                    state.storeInfo.copy(
                        isCardOk = !(state.storeInfo.isCardOk)
                    )
                )
            }
        }

    fun onDeliveryAvailableChanged() =
        intent {
            reduce {
                state.copy(
                    storeInfo =
                    state.storeInfo.copy(
                        isDeliveryOk = !(state.storeInfo.isDeliveryOk)
                    )
                )
            }
        }

    fun onTransferAvailableChanged() =
        intent {
            reduce {
                state.copy(
                    storeInfo =
                    state.storeInfo.copy(
                        isBankOk = !(state.storeInfo.isBankOk)
                    )
                )
            }
        }

    fun onStoreNameChanged(storeName: String) =
        blockingIntent {
            reduce {
                state.copy(storeInfo = state.storeInfo.copy(name = storeName))
            }
        }

    fun onPhoneNumberChanged(phone: String) =
        blockingIntent {
            reduce {
                state.copy(storeInfo = state.storeInfo.copy(phone = phone))
            }
        }

    fun onAddressChanged(address: String) =
        blockingIntent {
            reduce {
                state.copy(storeInfo = state.storeInfo.copy(address = address))
            }
        }

    fun onDeliveryPriceChanged(price: Int) =
        blockingIntent {
            reduce {
                state.copy(storeInfo = state.storeInfo.copy(deliveryPrice = price))
            }
        }

    fun onDescriptionChanged(description: String) =
        blockingIntent {
            reduce {
                state.copy(storeInfo = state.storeInfo.copy(description = description))
            }
        }

    fun isClosedDay(index: Int) {
        intent {
            if (index >= 0 && index <= (state.storeInfo.operatingTime?.size ?: 0)) {
                val newList = state.storeInfo.operatingTime?.toMutableList() ?: mutableListOf()
                val currentItem = newList[index]
                newList[index] = currentItem.copy(closed = !currentItem.closed)
                reduce {
                    state.copy(storeInfo = state.storeInfo.copy(operatingTime = newList))
                }
            }
        }
    }

    fun modifyStoreInfo(
        storeId: Int,
        storeDetailInfo: StoreDetailInfo
    ) {
        intent {
            viewModelScope.launch {
                modifyInfoUseCase.invoke(
                    storeId,
                    storeDetailInfo
                ).apply {
                    this ?: postSideEffect(ModifyInfoSideEffect.NavigateToMyStoreScreen)
                }
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
            getPresignedUrlUseCase(
                fileSize,
                fileType,
                fileName
            ).onSuccess {
                uploadImage(
                    preSignedUrl = it.second,
                    mediaType = fileType,
                    mediaSize = fileSize,
                    imageUri = imageUri,
                    fileUrl = it.first
                )
                intent {
                    reduce {
                        state.copy(
                            fileInfo =
                            state.fileInfo.toMutableList().apply {
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
                            }
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
                    storeInfo =
                    state.storeInfo.copy(
                        imageUrls =
                        state.storeInfo.imageUrls.toMutableList().apply {
                            add(url)
                        }
                    )
                )
            }
        }
    }

    fun initStoreImageUrls() =
        intent {
            reduce {
                state.copy(
                    storeInfo =
                    state.storeInfo.copy(
                        imageUrls = emptyList()
                    )
                )
            }
        }
}

private fun Hours.toTimeString(): String {
    val hoursString: String =
        if (this.hours < 10) "0" + this.hours.toString() else this.hours.toString()

    val minutesString: String =
        if (this.minutes < 10) "0" + this.minutes.toString() else this.minutes.toString()

    return "$hoursString:$minutesString"
}
