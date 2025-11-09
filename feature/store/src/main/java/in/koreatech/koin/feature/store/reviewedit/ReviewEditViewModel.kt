package `in`.koreatech.koin.feature.store.reviewedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.store.Review
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetMarketPreSignedUrlUseCase
import `in`.koreatech.koin.domain.usecase.store.ModifyReviewUseCase
import `in`.koreatech.koin.domain.usecase.store.SearchReviewUseCase
import `in`.koreatech.koin.feature.store.model.StoreNavigationData
import `in`.koreatech.koin.feature.store.model.StoreNavigationDataType
import `in`.koreatech.koin.feature.store.navigation.StoreReviewNavType
import `in`.koreatech.koin.feature.store.reviewadd.constants.MAX_IMAGE_COUNT
import `in`.koreatech.koin.feature.store.reviewadd.constants.MAX_MENU_TAG_COUNT
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject
import kotlin.reflect.typeOf
import kotlinx.coroutines.launch
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

        blockingIntent {
            reduce { state.copy(isLoading = true) }
        }

        intent {
            val reviewDetail = searchReviewUseCase(reviewId, shopId)
            reduce {
                state.copy(
                    storeId = shopId,
                    reviewId = reviewDetail.reviewId,
                    storeNavigationData = route.storeNavigationData,
                    storeName = route.storeName,
                    rating = reviewDetail.rating,
                    reviewContent = reviewDetail.content,
                    menuTags = reviewDetail.menuNames.toImmutableList(),
                    imageUris = reviewDetail.imageUrls.toImmutableList(),
                    isLoading = false
                )
            }
        }
    }

    fun updateReviewContent(content: String) = intent {
        reduce {
            state.copy(reviewContent = content)
        }
    }


    fun updateRating(newRating: Int) = intent {
        reduce {
            state.copy(rating = newRating)
        }
    }

    fun updateMenuTag(menuTag: String) = intent {
        reduce {
            state.copy(menuTag = menuTag)
        }
    }

    fun addMenuTag() = intent {
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

    fun removeMenuTag(index: Int) = intent {
        reduce {
            state.copy(
                menuTags = state.menuTags.filterIndexed { i, _ -> i != index }.toImmutableList()
            )
        }
    }

    fun addImageUris(newUris: List<String>) = intent {
        val currentUris = state.imageUris.toMutableList()
        val remain = MAX_IMAGE_COUNT - currentUris.size
        val addUris = newUris.take(remain)
        addUris.forEach { uri ->
            fetchPreSignedUrlWithUrl(uri)
        }
        reduce { state.copy(imageUris = (currentUris + addUris).take(MAX_IMAGE_COUNT).toImmutableList()) }
    }

    fun removeImageUri(index: Int) = intent {
        reduce {
            state.copy(
                imageUris = state.imageUris.filterIndexed { i, _ -> i != index }.toImmutableList(),
                presignedPairs = state.presignedPairs.filterIndexed { i, _ -> i != index }.toImmutableList()
            )
        }
    }

    private fun fetchPreSignedUrlWithUrl(imageUri: String) = intent {
        viewModelScope.launch {
            val fileName = imageUri.substringAfterLast('/')
            val fileType = "image/${fileName.substringAfterLast('.', "jpg")}"
            val fileSize = 1L
            getMarketPreSignedUrlUseCase(fileSize, fileType, fileName)
                .onSuccess { (fileUrl, preSignedUrl) ->
                    intent {
                        reduce {
                            state.copy(
                                presignedPairs = (state.presignedPairs + PresignedPair(imageUri, preSignedUrl, fileUrl)).toImmutableList()
                            )
                        }
                    }
                }
                .onFailure {
                    intent {
                        postSideEffect(ReviewEditSideEffect.ShowImageUploadFailedToast())
                    }
                }
        }
    }

    fun modifyReview() = intent {
        reduce { state.copy(isLoading = true) }
        val uploadResult = mutableListOf<String>()
        for ((localUri, presignedUrl, fileUrl) in state.presignedPairs) {
            val r = uploadFileUseCase(presignedUrl, "image/jpeg", 1L, localUri)
            if (r.isSuccess) {
                uploadResult.add(fileUrl)
            } else {
                postSideEffect(ReviewEditSideEffect.ShowImageUploadFailedToast())
                reduce { state.copy(isLoading = false) }
                return@intent
            }
        }
        val review = Review(
            rating = state.rating,
            content = state.reviewContent,
            imageUrls = uploadResult,
            menuNames = state.menuTags
        )
        modifyReviewUseCase(state.reviewId, state.storeId, review)
            .onSuccess {
                postSideEffect(ReviewEditSideEffect.ShowReviewModifiedToast())
                postSideEffect(ReviewEditSideEffect.NavigateToReview)
            }
            .onFailure {
                postSideEffect(ReviewEditSideEffect.ShowReviewModifyFailedToast())
            }
        reduce { state.copy(isLoading = false) }
    }
}
