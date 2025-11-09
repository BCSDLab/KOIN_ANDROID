package `in`.koreatech.koin.feature.store.reviewedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.store.Review
import `in`.koreatech.koin.domain.model.store.ReviewDetail
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetMarketPreSignedUrlUseCase
import `in`.koreatech.koin.domain.usecase.store.ModifyReviewUseCase
import `in`.koreatech.koin.domain.usecase.store.SearchReviewUseCase
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
class ReviewEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchReviewUseCase: SearchReviewUseCase,
    private val modifyReviewUseCase: ModifyReviewUseCase,
    private val getMarketPreSignedUrlUseCase: GetMarketPreSignedUrlUseCase,
    private val uploadFileUseCase: UploadFileUseCase
) : ViewModel(), ContainerHost<ReviewEditState, ReviewEditSideEffect> {
    override val container = container<ReviewEditState, ReviewEditSideEffect>(
        ReviewEditState()
    ) {
        val route = savedStateHandle.toRoute<StoreReviewNavType.StoreReviewEdit>(
            typeMap = mapOf(typeOf<StoreNavigationData>() to StoreNavigationDataType)
        )
        val reviewId = route.reviewId
        val shopId = route.storeNavigationData.shopId

        intent {
            val reviewDetail = fetchReviewDetail(reviewId, shopId)
            reduce {
                state.copy(
                    storeId = shopId,
                    reviewId = reviewDetail.reviewId,
                    storeNavigationData = route.storeNavigationData,
                    storeName = route.storeName,
                    rating = reviewDetail.rating,
                    reviewContent = reviewDetail.content,
                    menuTags = reviewDetail.menuNames.toImmutableList(),
                    imageUris = reviewDetail.imageUrls.toImmutableList()
                )
            }
        }
    }

    private suspend fun fetchReviewDetail(reviewId: Int, shopId: Int): ReviewDetail {
        return searchReviewUseCase(reviewId, shopId)
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

    fun clearFileInfo() = blockingIntent {
        reduce {
            state.copy(
                presignedPairs = persistentListOf()
            )
        }
    }

    fun requestPresignedUrl(fileSize: Long, fileType: String, fileName: String, imageUri: String) = intent {
        getMarketPreSignedUrlUseCase(fileSize, fileType, fileName).onSuccess { (fileUrl, presignedUrl) ->
            uploadFile(presignedUrl, fileType, fileSize, imageUri, fileUrl)
        }.onFailure {
            postSideEffect(ReviewEditSideEffect.ShowImageUploadFailed)
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
            postSideEffect(ReviewEditSideEffect.ShowImageUploadFailed)
        }
    }

    fun removeImageUri(index: Int) = blockingIntent {
        reduce {
            state.copy(
                imageUris = state.imageUris.filterIndexed { i, _ -> i != index }.toImmutableList(),
                presignedPairs = state.presignedPairs.filterIndexed { i, _ -> i != index }.toImmutableList()
            )
        }
    }

    fun modifyReview() = intent {
        reduce { state.copy(isLoading = true) }
        val review = Review(
            rating = state.rating,
            content = state.reviewContent,
            imageUrls = state.imageUris,
            menuNames = state.menuTags
        )
        modifyReviewUseCase(state.reviewId, state.storeId, review)
            .onSuccess {
                postSideEffect(ReviewEditSideEffect.ShowReviewModified)
                postSideEffect(ReviewEditSideEffect.NavigateToReview)
            }
            .onFailure {
                postSideEffect(ReviewEditSideEffect.ShowReviewModifyFailed)
            }
        reduce { state.copy(isLoading = false) }
    }
}
