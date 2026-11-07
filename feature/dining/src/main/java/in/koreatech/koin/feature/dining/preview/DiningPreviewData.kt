package `in`.koreatech.koin.feature.dining.preview

import `in`.koreatech.koin.domain.model.dining.Dining

internal const val PREVIEW_DINING_DATE = "2025.05.17"

internal fun previewDining(
    place: String = "A코너",
    type: String = "BREAKFAST",
    menu: List<String> = listOf("밥", "국", "김치"),
    imageUrl: String = "",
    soldOutAt: String = ""
): Dining = Dining(
    id = 0,
    date = PREVIEW_DINING_DATE,
    type = type,
    place = place,
    priceCard = "1000",
    priceCash = "1000",
    kcal = "786",
    menu = menu,
    imageUrl = imageUrl,
    createdAt = PREVIEW_DINING_DATE,
    updatedAt = PREVIEW_DINING_DATE,
    soldOutAt = soldOutAt,
    changedAt = PREVIEW_DINING_DATE
)
