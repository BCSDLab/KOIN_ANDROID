package `in`.koreatech.koin.feature.store.review.component

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal const val SAMPLE_STEAMED_EGG = "계란찜"
internal const val SAMPLE_SPICY_CHICKEN_FEET = "1인 매운 닭발"
internal const val SAMPLE_DAKBAL = "닭발"

internal const val REVIEW_PREVIEW_DATE = "2025.01.01"
internal const val REVIEW_PREVIEW_CONTENT =
    "내용을 입력하세요. 내용을 입력하세요. 내용을 입력하세요. 내용을 입력하세요. 내용을 입력하세요. 내용을 입력하세요. 내용을 입력하세요."

internal val previewMenuTags: ImmutableList<String> = persistentListOf(
    SAMPLE_STEAMED_EGG,
    SAMPLE_SPICY_CHICKEN_FEET,
    SAMPLE_STEAMED_EGG,
    SAMPLE_SPICY_CHICKEN_FEET,
    SAMPLE_DAKBAL,
    SAMPLE_STEAMED_EGG,
    SAMPLE_SPICY_CHICKEN_FEET,
    SAMPLE_STEAMED_EGG,
    SAMPLE_SPICY_CHICKEN_FEET,
    SAMPLE_STEAMED_EGG,
    SAMPLE_SPICY_CHICKEN_FEET,
    SAMPLE_STEAMED_EGG,
    SAMPLE_SPICY_CHICKEN_FEET
)

internal val previewRepeatedMenuTags: ImmutableList<String> = persistentListOf(
    SAMPLE_STEAMED_EGG,
    SAMPLE_SPICY_CHICKEN_FEET,
    SAMPLE_STEAMED_EGG,
    SAMPLE_SPICY_CHICKEN_FEET,
    SAMPLE_STEAMED_EGG,
    SAMPLE_SPICY_CHICKEN_FEET,
    SAMPLE_STEAMED_EGG,
    SAMPLE_SPICY_CHICKEN_FEET
)
