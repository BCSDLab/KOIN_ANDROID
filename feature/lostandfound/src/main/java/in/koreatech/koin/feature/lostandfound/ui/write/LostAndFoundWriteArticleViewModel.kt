package `in`.koreatech.koin.feature.lostandfound.ui.write

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.article.lostandfound.UploadLostAndFoundArticleUseCase
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetLostAndFoundPreSignedUrlUseCase
import `in`.koreatech.koin.feature.lostandfound.IMAGE_MAX_COUNT
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory
import `in`.koreatech.koin.feature.lostandfound.enums.LostOrFoundType
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class LostAndFoundWriteArticleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val uploadLostAndFoundArticleUseCase: UploadLostAndFoundArticleUseCase,
    private val getLostAndFoundPreSignedUrlUseCase: GetLostAndFoundPreSignedUrlUseCase,
    private val uploadFilesUseCase: UploadFileUseCase
) : ViewModel(),
    ContainerHost<LostAndFoundWriteArticleState, LostAndFoundWriteArticleSideEffect> {

    override val container =
        container<LostAndFoundWriteArticleState, LostAndFoundWriteArticleSideEffect>(
            LostAndFoundWriteArticleState(),
            savedStateHandle
        ) {
            val rawLostOrFoundType = savedStateHandle.get<String>(LOST_OR_FOUND_TYPE)
            val lostOrFoundType = LostOrFoundType.entries.find {
                it.name == rawLostOrFoundType
            } ?: LostOrFoundType.FOUND
            setLostOrFoundType(lostOrFoundType)
            addItem(
                LostAndFoundWriteArticleItemState(
                    lostOrFoundType = lostOrFoundType
                )
            )
        }

    private fun setLostOrFoundType(lostOrFoundType: LostOrFoundType) = intent {
        reduce {
            state.copy(lostOrFoundType = lostOrFoundType)
        }
    }

    fun addItem(item: LostAndFoundWriteArticleItemState) = intent {
        //postSideEffect(LostAndFoundWriteArticleSideEffect.AddItem(item))
        reduce {
            state.copy(itemList = state.itemList + item)
        }
    }

    fun removeItem(index: Int) = intent {
        //postSideEffect(LostAndFoundWriteArticleSideEffect.RemoveItem(index))
        reduce {
            state.copy(itemList = state.itemList.filterIndexed { i, _ -> i != index })
        }
    }

    fun updateItemType(index: Int, category: LostItemCategory) {
        intent {
            //postSideEffect(LostAndFoundWriteArticleSideEffect.UpdateItemType(index, category))
            reduce {
                state.copy(
                    itemList = state.itemList.mapIndexed { i, item ->
                        if (i == index) {
                            return@mapIndexed item.copy(
                                category = category,
                                itemTypeRequired = category == LostItemCategory.NONE
                            )
                        } else {
                            item
                        }
                    }
                )
            }
        }
    }

    private fun uploadImage(
        preSignedUrl: String,
        fileUrl: String,
        mediaType: String,
        mediaSize: Long,
        imageUri: Uri,
        itemIndex: Int,
        imageIndex: Int
    ) = viewModelScope.launch {
        uploadFilesUseCase(
            preSignedUrl,
            mediaType,
            mediaSize,
            imageUri.toString()
        ).onSuccess {
            intent {
                reduce {
                    state.copy(
                        itemList = state.itemList.mapIndexed { i, item ->
                            if (i == itemIndex) {
                                return@mapIndexed item.copy(images = item.images.mapIndexed { j, currentValue ->
                                    if (j == imageIndex) {
                                        return@mapIndexed fileUrl // Replace placeholder to real image url
                                    } else {
                                        currentValue
                                    }
                                })
                            } else {
                                item
                            }
                        }
                    )
                }
            }
        }.onFailure {
            intent {
                postSideEffect(LostAndFoundWriteArticleSideEffect.FailedToUploadImage)
            }
        }
    }

    fun getPreSignedUrl(
        fileSize: Long,
        fileType: String,
        fileName: String,
        imageUri: Uri,
        itemIndex: Int,
        imageIndex: Int
    ) = viewModelScope.launch {
        getLostAndFoundPreSignedUrlUseCase(
            fileSize, fileType, fileName
        ).onSuccess {
            uploadImage(
                preSignedUrl = it.second,
                fileUrl = it.first,
                mediaType = fileType,
                mediaSize = fileSize,
                imageUri = imageUri,
                itemIndex = itemIndex,
                imageIndex = imageIndex
            )
        }.onFailure {
            intent {
                postSideEffect(LostAndFoundWriteArticleSideEffect.FailedToUploadImage)
            }
        }
    }

    fun addImage(
        itemIndex: Int,
        imageUri: Uri
    ) = intent {
        if (state.itemList.lastIndex < itemIndex) return@intent
        if (state.itemList[itemIndex].images.size + 1 > IMAGE_MAX_COUNT) {
            postSideEffect(LostAndFoundWriteArticleSideEffect.FailedToUploadImage)
            return@intent
        }

        reduce {
            state.copy(itemList = state.itemList.mapIndexed { i, item ->
                if (i == itemIndex) {
                    return@mapIndexed item.copy(images = item.images + "") // Add empty string as placeholder
                } else {
                    item
                }
            })
        }

        postSideEffect(
            LostAndFoundWriteArticleSideEffect.AddImage(
                itemIndex,
                state.itemList[itemIndex].images.lastIndex,
                imageUri,
                state.itemList[itemIndex].images.size > IMAGE_MAX_COUNT
            )
        )
    }

    fun removeImage(itemIndex: Int, imageIndex: Int) = intent {
        reduce {
            state.copy(
                itemList = state.itemList.mapIndexed { i, item ->
                    if (i == itemIndex) {
                        return@mapIndexed item.copy(images = item.images.filterIndexed { j, _ -> j != imageIndex })
                    } else {
                        item
                    }
                }
            )
        }
    }

    fun updateDescription(itemIndex: Int, content: String) = intent {
        //postSideEffect(LostAndFoundWriteArticleSideEffect.UpdateDescription(itemIndex, content))
        reduce {
            state.copy(
                itemList = state.itemList.mapIndexed { i, item ->
                    if (i == itemIndex) {
                        return@mapIndexed item.copy(content = content)
                    } else {
                        item
                    }
                }
            )
        }
    }

    fun updateLocation(itemIndex: Int, foundPlace: String) = intent {
        //postSideEffect(LostAndFoundWriteArticleSideEffect.UpdateLocation(itemIndex, foundPlace))
        reduce {
            state.copy(
                itemList = state.itemList.mapIndexed { i, item ->
                    if (i == itemIndex) {
                        return@mapIndexed item.copy(
                            foundPlace = foundPlace,
                            locationRequired = foundPlace.isEmpty()
                        )
                    } else {
                        item
                    }
                }
            )
        }
    }

    fun updateDate(itemIndex: Int, date: LocalDate?) = intent {
        //postSideEffect(LostAndFoundWriteArticleSideEffect.UpdateDate(itemIndex, date))
        reduce {
            state.copy(
                itemList = state.itemList.mapIndexed { i, item ->
                    if (i == itemIndex) {
                        return@mapIndexed item.copy(foundDate = date, dateRequired = date == null)
                    } else {
                        item
                    }
                }
            )
        }
    }

    fun checkAllFieldValid() = intent {
        postSideEffect(LostAndFoundWriteArticleSideEffect.CheckAllFieldValid(state.itemList))
    }

    fun writeArticle() = viewModelScope.launch {
        intent {
            uploadLostAndFoundArticleUseCase(state.itemList.map {
                it.toArticleLostAndFoundUpload()
            }).onSuccess {
                postSideEffect(LostAndFoundWriteArticleSideEffect.LostAndFoundWriteArticle(it.id))
            }.onFailure {
                postSideEffect(LostAndFoundWriteArticleSideEffect.LostAndFoundWriteArticleFailed)
            }
        }
    }

    companion object {
        const val LOST_OR_FOUND_TYPE = "lost_or_found_type"
    }
}
