package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.entity.DiningEntity
import `in`.koreatech.koin.data.response.DiningResponse
import `in`.koreatech.koin.domain.model.dining.Dining

fun DiningResponse.toDining() =
    Dining(
        this.id,
        this.date,
        this.type,
        this.place,
        (this.priceCard ?: 0).toString(),
        (this.priceCash ?: 0).toString(),
        (this.kcal ?: 0).toString(),
        this.menu,
        this.imageUrl ?: "",
        this.createdAt,
        this.updatedAt,
        this.soldoutAt ?: "",
        this.changedAt ?: ""
    )

fun List<String>.toLineChangingString(): String {
    var string = ""
    forEach {
        string += it + "\n"
    }
    return string
}

fun Dining.toDiningEntity(cacheDate: String) = DiningEntity(
    id = id,
    cacheDate = cacheDate,
    date = date,
    type = type,
    place = place,
    priceCard = priceCard,
    priceCash = priceCash,
    kcal = kcal,
    menu = menu,
    imageUrl = imageUrl,
    createdAt = createdAt,
    updatedAt = updatedAt,
    soldOutAt = soldOutAt,
    changedAt = changedAt
)

fun DiningEntity.toDining() = Dining(
    id = id,
    date = date,
    type = type,
    place = place,
    priceCard = priceCard,
    priceCash = priceCash,
    kcal = kcal,
    menu = menu,
    imageUrl = imageUrl,
    createdAt = createdAt,
    updatedAt = updatedAt,
    soldOutAt = soldOutAt,
    changedAt = changedAt
)
