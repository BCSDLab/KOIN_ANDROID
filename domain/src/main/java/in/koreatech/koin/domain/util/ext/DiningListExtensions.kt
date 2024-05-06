package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningPlace
import `in`.koreatech.koin.domain.model.dining.DiningType

fun List<Dining>.typeFilter(type: DiningType) = this.filter {
    it.type == type.typeEnglish
}


fun List<Dining>.arrange() = this.let {
    val campus1 = this.filter { it.place != "2캠퍼스" }
    campus1.sortedBy { it.place }
    val allPlaces = DiningPlace.entries.map { it.place }

    allPlaces.map { place ->
        campus1.find { it.place == place } ?: Dining(
            id = 0,
            date = "",
            type = "",
            place = place,
            priceCard = "",
            priceCash = "",
            kcal = "",
            menu = listOf(),
            imageUrl = "",
            createdAt = "",
            updatedAt = "",
            soldoutAt = "",
            changedAt = "",
            error = ""
        )
    }
}