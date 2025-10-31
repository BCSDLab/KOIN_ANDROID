package `in`.koreatech.koin.feature.store.review.model

import `in`.koreatech.koin.domain.model.store.StoreReviewContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable

@Serializable
data class LocalReviewContent(
    val reviewId: Int,
    val rating: Int,
    val nickName: String,
    val content: String,
    val imageUrls: ImmutableList<String>,
    val menuNames: ImmutableList<String>,
    val isMine: Boolean,
    val isModified: Boolean,
    val isReported: Boolean,
    val createdAt: String
)

fun StoreReviewContent.toLocalReviewContent() = LocalReviewContent(
    reviewId = reviewId,
    rating = rating,
    nickName = nickName,
    content = content,
    imageUrls = imageUrls.toImmutableList(),
    menuNames = menuNames.toImmutableList(),
    isMine = isMine,
    isModified = isModified,
    isReported = isReported,
    createdAt = createdAt
)
