package `in`.koreatech.business.feature.event.writeevent.writeevent

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.business.feature.loading.LoadingState
import `in`.koreatech.business.feature.storemenu.modifymenu.modifymenu.TEMP_IMAGE_URI
import `in`.koreatech.business.util.getImageInfo
import `in`.koreatech.koin.domain.model.owner.EventInfo
import `in`.koreatech.koin.domain.model.store.StoreUrl
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.owner.RegisterEventUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetMarketPreSignedUrlUseCase
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class WriteEventViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPresignedUrlUseCase: GetMarketPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase,
    private val registerEventUseCase: RegisterEventUseCase,
) : ViewModel(), ContainerHost<WriteEventState, WriteEventSideEffect> {
    override val container = container<WriteEventState, WriteEventSideEffect>(WriteEventState())

    private val storeId: Int = checkNotNull(savedStateHandle["storeId"])
    init {
        settingId(storeId)
    }

    fun onCalendarVisibilityChanged(visibility: Boolean) = intent {
        reduce {
            state.copy(showCalendarAlert = visibility)
        }
    }

    fun onDatePickerVisibilityChanged(visibility: Boolean) = intent {
        reduce {
            state.copy(showDatePickerAlert = visibility)
        }
    }

    fun onTitleChanged(title: String) = blockingIntent {
        if(title.length > MAX_TITLE_LENGTH)
            return@blockingIntent
        reduce {
            state.copy(title = title, showTitleInputAlert = false)
        }
    }

    fun onContentChanged(content: String) = blockingIntent {
        if(content.length > MAX_CONTENT_LENGTH)
            return@blockingIntent
        reduce {
            state.copy(content = content, showContentInputAlert = false)
        }
    }

    fun onStartDateChanged(startDate: String) = intent {
        reduce {
            state.copy(startDate = startDate)
        }
    }

    fun onEndDateChanged(endDate: String) = intent {
        reduce {
            state.copy(endDate = endDate)
        }
    }

    fun onStartYearChanged(startYear: String) = intent {
        reduce {
            state.copy(startYear = startYear,
                showDateInputAlert = if (state.showDateInputAlert)
                    !isAllDateInputFilled(state.copy(startYear = startYear))
                else false
            )
        }
    }

    fun onStartMonthChanged(startMonth: String) = intent {
        reduce {
            state.copy(startMonth = startMonth,
                showDateInputAlert = if (state.showDateInputAlert)
                    !isAllDateInputFilled(state.copy(startMonth = startMonth))
                else false
            )
        }
    }

    fun onSelectedYearMonthChanged(yearMonth: YearMonth) = intent {
        reduce {
            state.copy(selectedYearMonth = yearMonth)
        }
    }


    fun registerEventImageUri(context: Context, imageUri: Uri) {
        intent {
            val newMenuUriList = state.images.toMutableList()
            newMenuUriList.add(imageUri.toString())
            reduce {
                state.copy(
                    images = newMenuUriList
                )
            }
            uploadImageList(context)
        }
    }

    fun deleteImage(index: Int) {
        intent {
            val newMenuUriList = state.images.toMutableList()
            newMenuUriList.removeAt(index)
            reduce {
                state.copy(
                    images = newMenuUriList
                )
            }
        }
    }

    //이미지 업로드
    private fun uploadImage(
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
                intent {
                    reduce { state.copy(error = null) }
                }
            }.onFailure {
                intent {
                    reduce { state.copy(error = it) }
                }
            }
            LoadingState.hide()
        }
    }


    private fun uploadImageList(context: Context) {
        intent {
            viewModelScope.launch {
                state.images.forEach { uriString ->
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
        imageUri: String,
    ) {
        viewModelScope.launch {
            LoadingState.show()
            getPresignedUrlUseCase(
                fileSize, fileType, fileName
            ).onSuccess {
                uploadImage(
                    preSignedUrl = it.second,
                    mediaType = fileType,
                    mediaSize = fileSize,
                    imageUri = imageUri,
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

    fun registerEvent() = intent {
        viewModelScope.launch {
            registerEventUseCase(
                shopId = state.storeId,
                eventInfo = EventInfo(
                    title = state.title,
                    content = state.content,
                    thumbnailImages = state.fileInfo.map { it.resultUrl },
                    startDate = state.startDate,
                    endDate = state.endDate
                ),
            )}
        reduce {
            state.copy(
                showTitleInputAlert = state.title.isEmpty(),
                showContentInputAlert = state.content.isEmpty(),
                showDateInputAlert = state.startYear.length != 4
                        || state.startMonth.length != 2
                        || state.startDay.length != 2
                        || state.endYear.length != 4
                        || state.endMonth.length != 2
                        || state.endDay.length != 2
            )
        }
        postSideEffect(WriteEventSideEffect.RegisterEventSuccess)
    }

    private fun isAllDateInputFilled(state: WriteEventState): Boolean {
        return state.startYear.length == 4
                && state.startMonth.length == 2
                && state.startDay.length == 2
                && state.endYear.length == 4
                && state.endMonth.length == 2
                && state.endDay.length == 2
    }

    private fun isValidNumberInput(maxLength: Int, input: String): Boolean {
        if (input.length > maxLength)
            return false
        if (input.isNotEmpty() && input.toIntOrNull() == null)
            return false
        return true
    }

    private fun settingId(menuId: Int){
        intent{
            if(menuId != state.storeId){
                reduce {
                    state.copy(
                        storeId = menuId
                    )
                }
            }
        }
    }


    companion object {
        const val MAX_IMAGE_LENGTH = 3
        const val MAX_TITLE_LENGTH = 25
        const val MAX_CONTENT_LENGTH = 500
    }
}