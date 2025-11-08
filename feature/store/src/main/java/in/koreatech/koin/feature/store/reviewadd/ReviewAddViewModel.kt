package `in`.koreatech.koin.feature.store.reviewadd

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.store.Review
import `in`.koreatech.koin.domain.usecase.business.UploadFileUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.GetMarketPreSignedUrlUseCase
import `in`.koreatech.koin.domain.usecase.store.WriteReviewUseCase
import `in`.koreatech.koin.feature.store.model.StoreNavigationData
import `in`.koreatech.koin.feature.store.model.StoreNavigationDataType
import `in`.koreatech.koin.feature.store.navigation.StoreReviewNavType
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

    fun updateReviewContent(content: String) {
        intent {
            reduce {
                state.copy(reviewContent = content)
            }
        }
    }

    fun updateRating(newRating: Int) {
        intent {
            reduce {
                state.copy(rating = newRating)
            }
        }
    }

    fun updateMenuTag(menuTag: String) {
        intent {
            reduce {
                state.copy(menuTag = menuTag)
            }
        }
    }

    fun addMenuTag() {
        intent {
            val newTag = state.menuTag
            if (newTag.isNotBlank() && !state.menuTags.contains(newTag) && state.menuTags.size < 5) {
                reduce {
                    state.copy(
                        menuTags = state.menuTags + newTag,
                        menuTag = ""
                    )
                }
            }
        }
    }

    fun removeMenuTag(index: Int) {
        intent {
            reduce {
                state.copy(
                    menuTags = state.menuTags.filterIndexed { i, _ -> i != index }
                )
            }
        }
    }

    fun addImageUris(newUris: List<String>) = intent {
        val currentUris = state.imageUris.toMutableList()
        val remain = 3 - currentUris.size
        val addUris = newUris.take(remain)
        addUris.forEach { uri ->
            fetchPreSignedUrlWithUrl(uri)
        }
        reduce { state.copy(imageUris = (currentUris + addUris).take(3)) }
    }

    fun removeImageUri(index: Int) = intent {
        reduce {
            state.copy(
                imageUris = state.imageUris.filterIndexed { i, _ -> i != index },
                presignedPairs = state.presignedPairs.filterIndexed { i, _ -> i != index }
            )
        }
    }

    private fun fetchPreSignedUrlWithUrl(imageUri: String) {
        intent { reduce { state.copy(isLoading = true) } }
        viewModelScope.launch {
            val fileName = imageUri.substringAfterLast('/')
            val fileType = "image/${fileName.substringAfterLast('.', "jpg")}"
            val fileSize = 1L
            getMarketPreSignedUrlUseCase(fileSize, fileType, fileName)
                .onSuccess { (fileUrl, preSignedUrl) ->
                    intent {
                        reduce {
                            state.copy(
                                presignedPairs = state.presignedPairs + Triple(imageUri, preSignedUrl, fileUrl)
                            )
                        }
                    }
                }
                .onFailure {
                    intent {
                        postSideEffect(ReviewAddSideEffect.ShowToast("이미지 업로드 실패"))
                    }
                }
            intent { reduce { state.copy(isLoading = false) } }
        }
    }

    fun submitReview() = intent {
        if (state.rating < 1) {
            postSideEffect(ReviewAddSideEffect.ShowToast("별점을 1점 이상 선택해주세요."))
            return@intent
        }

        reduce { state.copy(isLoading = true) }
        val uploadResult = mutableListOf<String>()
        for ((localUri, presignedUrl, fileUrl) in state.presignedPairs) {
            val r = uploadFileUseCase(presignedUrl, "image/jpeg", 1L, localUri)
            if (r.isSuccess) {
                uploadResult.add(fileUrl)
            } else {
                postSideEffect(ReviewAddSideEffect.ShowToast("이미지 업로드 실패"))
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
        writeReviewUseCase(state.storeId, review)
            .onSuccess {
                postSideEffect(ReviewAddSideEffect.ShowToast("리뷰가 작성되었어요"))
                postSideEffect(ReviewAddSideEffect.NavigateToReview)
            }
            .onFailure {
                postSideEffect(ReviewAddSideEffect.ShowToast("한 상점에 하루에 한번만 리뷰를 남길 수 있습니다."))
            }
        reduce { state.copy(isLoading = false) }
    }
}
