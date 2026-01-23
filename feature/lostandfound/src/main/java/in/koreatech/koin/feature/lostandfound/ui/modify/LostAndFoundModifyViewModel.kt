package `in`.koreatech.koin.feature.lostandfound.ui.modify

import android.net.Uri
import android.webkit.URLUtil
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFound
import `in`.koreatech.koin.domain.model.upload.PreSignedUrlDomain
import `in`.koreatech.koin.domain.usecase.article.lostandfound.FetchLostAndFoundArticleV2UseCase
import `in`.koreatech.koin.domain.usecase.article.lostandfound.ModifyArticleLostAndFoundUseCase
import `in`.koreatech.koin.domain.usecase.presignedurl.UploadImageUseCase
import `in`.koreatech.koin.feature.lostandfound.IMAGE_MAX_COUNT
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory
import `in`.koreatech.koin.feature.lostandfound.enums.LostItemCategory.Companion.getCategoryKoreanWord
import `in`.koreatech.koin.feature.lostandfound.navigation.ARTICLE_ID
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import retrofit2.HttpException

@HiltViewModel
class LostAndFoundModifyViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val fetchLostAndFoundArticleV2UseCase: FetchLostAndFoundArticleV2UseCase,
    private val modifyArticleLostAndFoundUseCase: ModifyArticleLostAndFoundUseCase,
    private val uploadImageUseCase: UploadImageUseCase
) : ViewModel(),
    ContainerHost<LostAndFoundModifyState, LostAndFoundModifySideEffect> {
    override val container =
        container<LostAndFoundModifyState, LostAndFoundModifySideEffect>(
            LostAndFoundModifyState(),
            savedStateHandle
        ) {
            val articleId = savedStateHandle.get<Int>(ARTICLE_ID)
            checkNotNull(articleId)
            fetchLostAndFoundDetail(articleId)
        }
    private var originImages: List<ArticleLostAndFound.ArticleLostAndFoundImage> = emptyList()

    private fun fetchLostAndFoundDetail(articleId: Int) = intent {
        reduce {
            state.copy(
                isLoading = true
            )
        }
        fetchLostAndFoundArticleV2UseCase(articleId).catch {
            if (it is HttpException && it.code() == 404) {
                postSideEffect(LostAndFoundModifySideEffect.DeletedArticle)
            }
        }.collectLatest { article ->
            originImages = article.images?.filter { URLUtil.isValidUrl(it.imageUrl) } ?: emptyList()
            val articleState = article.toLostAndFoundModifyState()
            reduce {
                state.copy(
                    articleId = articleState.articleId,
                    lostOrFoundType = articleState.lostOrFoundType,
                    category = articleState.category,
                    foundPlace = articleState.foundPlace,
                    foundDate = articleState.foundDate,
                    content = articleState.content,
                    images = articleState.images,
                    refreshDatePicker = !state.refreshDatePicker,
                    isLoading = false
                )
            }
        }
    }

    fun updateItemType(
        category: LostItemCategory
    ) {
        intent {
            reduce {
                state.copy(
                    category = category,
                    itemTypeRequired = category == LostItemCategory.NONE
                )
            }
        }
    }

    fun uploadImage(
        mediaName: String,
        mediaType: String,
        mediaSize: Long,
        imageUri: Uri
    ) = intent {
        reduce {
            state.copy(
                isLoading = true
            )
        }
        if (IMAGE_MAX_COUNT < state.images.size + 1) {
            postSideEffect(LostAndFoundModifySideEffect.UploadedMaxImage)
            return@intent
        }
        uploadImageUseCase(
            domain = PreSignedUrlDomain.LOST_AND_FOUND,
            contentLength = mediaSize,
            contentType = mediaType,
            fileName = mediaName,
            imageUri = imageUri.toString()
        ).onSuccess {
            reduce {
                state.copy(
                    images = state.images.toPersistentList().add(it),
                    isLoading = false
                )
            }
        }.onFailure {
            reduce {
                state.copy(
                    isLoading = false
                )
            }
            postSideEffect(LostAndFoundModifySideEffect.FailedToUploadImage)
        }
    }

    fun removeImage(
        imageIndex: Int
    ) = intent {
        reduce {
            state.copy(
                images = state.images.toPersistentList().removeAt(imageIndex)
            )
        }
    }

    fun updateDescription(
        content: String
    ) = intent {
        reduce {
            state.copy(
                content = content
            )
        }
    }

    fun updateLocation(
        foundPlace: String
    ) = intent {
        reduce {
            state.copy(
                foundPlace = foundPlace,
                locationRequired = foundPlace.isEmpty()
            )
        }
    }

    fun updateDate(
        date: LocalDate
    ) = intent {
        reduce {
            state.copy(
                foundDate = date
            )
        }
    }

    fun checkAllFieldValidAndModify() = intent {
        reduce {
            state.copy(
                itemTypeRequired = state.category == LostItemCategory.NONE,
                locationRequired = state.foundPlace.isEmpty()
            )
        }
        modifyArticle()
    }

    private fun modifyArticle() = intent {
        if (state.itemTypeRequired || state.locationRequired) return@intent
        val newImageList = state.images.filterNot {
            it in originImages.map { origin -> origin.imageUrl }
        }
        val deleteImageIds = originImages.filterNot {
            it.imageUrl in state.images
        }.map { it.id }
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        modifyArticleLostAndFoundUseCase(
            articleId = state.articleId,
            category = state.category.getCategoryKoreanWord(),
            foundPlace = state.foundPlace,
            foundDate = state.foundDate.format(dateFormat),
            content = state.content,
            newImage = newImageList,
            deleteImageIds = deleteImageIds
        ).onSuccess {
            postSideEffect(LostAndFoundModifySideEffect.LostAndFoundModifyArticle)
        }.onFailure {
            postSideEffect(LostAndFoundModifySideEffect.LostAndFoundModifyArticleFailed)
        }
    }
}
