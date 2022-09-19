package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.DiningResponse
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningType

fun DiningResponse.toDining() = Dining(
    this.id,
    this.date,
    this.type,
    this.place,
    (this.priceCard ?: 0).toString(),
    (this.priceCash ?: 0).toString(),
    (this.kcal ?: 0).toString(),
    this.menu,
    this.createdAt,
    this.updatedAt,
    this.error ?: ""
)

fun List<String>.toLineChangingString(): String {
    var string = ""
    forEach {
        string += it + "\n"
    }
    return string
}