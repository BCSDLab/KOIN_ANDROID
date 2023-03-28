package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningType

fun List<Dining>.typeFilter(type: DiningType) = this.filter {
    it.type == type.typeEnglish
}


fun List<Dining>.arrange() = this.let {
    val diningList = mutableListOf<Dining>()
    val campus2 = this.filter { it.place == "2캠퍼스" }
    val campus1 = this.filter { it.place != "2캠퍼스" }
    campus1.sortedBy { it.place }
    diningList.addAll(campus1)
    diningList.addAll(campus2)
    diningList.toList()
}