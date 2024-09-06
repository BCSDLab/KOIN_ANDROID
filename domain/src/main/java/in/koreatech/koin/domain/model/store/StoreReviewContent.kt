package `in`.koreatech.koin.domain.model.store

import java.io.Serializable

data class StoreReviewContent(
    val reviewId: Int,
    val rating: Int,
    val nickName: String,
    val content: String,
    val imageUrls: List<String>,
    val menuNames: List<String>,
    val isMine: Boolean,
    val isModified: Boolean,
    val isReported: Boolean,
    val createdAt: String
): Serializable
