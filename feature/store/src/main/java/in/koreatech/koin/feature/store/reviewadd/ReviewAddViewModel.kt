package `in`.koreatech.koin.feature.store.reviewadd

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.store.Review
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetMarketPreSignedUrlUseCase
import `in`.koreatech.koin.domain.usecase.store.WriteReviewUseCase
import `in`.koreatech.koin.feature.store.model.StoreNavigationData
import `in`.koreatech.koin.feature.store.model.StoreNavigationDataType
import `in`.koreatech.koin.feature.store.navigation.StoreReviewNavType
import `in`.koreatech.koin.feature.store.reviewadd.constants.MAX_MENU_TAG_COUNT
import javax.inject.Inject
import kotlin.reflect.typeOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ReviewAddViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val writeReviewUseCase: WriteReviewUseCase,
    private val getMarketPreSignedUrlUseCase: GetMarketPreSignedUrlUseCase,
    private val uploadFileUseCase: UploadFileUseCase
) : ViewModel(), ContainerHost<ReviewAddState, ReviewAddSideEffect> {
    override val container = container<ReviewAddState, ReviewAddSideEffect>(
        ReviewAddState()
    ) {
        val route = savedStateHandle.toRoute<StoreReviewNavType.StoreReviewAdd>(
            typeMap = mapOf(typeOf<StoreNavigationData>() to StoreNavigationDataType)
        )
        blockingIntent {
            reduce {
                state.copy(
                    storeNavigationData = route.storeNavigationData,
                    storeId = route.storeNavigationData.shopId,
                    storeName = route.storeName
                )
            }
        }
    }

    fun updateReviewContent(content: String) = blockingIntent {
        reduce {
            state.copy(reviewContent = content)
        }
    }

    fun updateRating(newRating: Int) = blockingIntent {
        reduce {
            state.copy(rating = newRating)
        }
    }

    fun updateMenuTag(menuTag: String) = blockingIntent {
        reduce {
            state.copy(menuTag = menuTag)
        }
    }

    fun addMenuTag() = blockingIntent {
        val newTag = state.menuTag
        if (newTag.isNotBlank() && !state.menuTags.contains(newTag) && state.menuTags.size < MAX_MENU_TAG_COUNT) {
            reduce {
                state.copy(
                    menuTags = (state.menuTags + newTag).toImmutableList(),
                    menuTag = ""
                )
            }
        }
    }

    fun removeMenuTag(index: Int) = blockingIntent {
        reduce {
            state.copy(
                menuTags = state.menuTags.filterIndexed { i, _ -> i != index }.toImmutableList()
            )
        }
    }

    fun requestPresignedUrl(fileSize: Long, fileType: String, fileName: String, imageUri: String) = intent {
        getMarketPreSignedUrlUseCase(fileSize, fileType, fileName).onSuccess { (fileUrl, presignedUrl) ->
            uploadFile(presignedUrl, fileType, fileSize, imageUri, fileUrl)
        }.onFailure {
            postSideEffect(ReviewAddSideEffect.ShowImageUploadFailed)
        }
    }

    private fun uploadFile(presignedUrl: String, mediaType: String, mediaSize: Long, imageUri: String, fileUrl: String) = intent {
        uploadFileUseCase(presignedUrl, mediaType, mediaSize, imageUri).onSuccess {
            reduce {
                state.copy(
                    imageUris = (state.imageUris + fileUrl).toImmutableList()
                )
            }
        }.onFailure {
            postSideEffect(ReviewAddSideEffect.ShowImageUploadFailed)
        }
    }

    fun clearFileInfo() = blockingIntent {
        reduce {
            state.copy(
                fileInfo = persistentListOf(),
                presignedPairs = persistentListOf()
            )
        }
    }

    fun removeImageUri(index: Int) = blockingIntent {
        reduce {
            state.copy(
                imageUris = state.imageUris.filterIndexed { i, _ -> i != index }.toImmutableList()
            )
        }
    }

    fun submitReview() = intent {
        if (state.reviewContent.isBlank()) {
            postSideEffect(ReviewAddSideEffect.ShowReviewWriteIsNotBlank)
            return@intent
        }
        reduce { state.copy(isLoading = true) }
        val review = Review(
            rating = state.rating,
            content = state.reviewContent,
            imageUrls = state.imageUris,
            menuNames = state.menuTags
        )
        writeReviewUseCase(state.storeId, review).onSuccess {
            postSideEffect(ReviewAddSideEffect.ShowReviewWritten)
            postSideEffect(ReviewAddSideEffect.ReviewUpdated)
        }.onFailure {
            postSideEffect(ReviewAddSideEffect.ShowOneReviewPerDay)
        }
        reduce { state.copy(isLoading = false) }
    }
}
