package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningType
import java.util.*

fun List<Dining>.typeFilter(type: DiningType) = this.filter {
    it.type == type.typeEnglish
}


fun List<Dining>.arrange() = this.let { it ->
    val diningList = mutableListOf<Dining>()
    val diningMap = TreeMap<Int, Dining>()
    it.forEach { dining ->
        var priority = when(dining.place) {
            "한식" -> 0
            "일품식" -> 1
            "양식" -> 2
            "특식" -> 3
            "능수관" -> 4
            "수박여" -> 5
            else -> 6
        }
        diningMap.put(priority, dining)
    }
    diningMap.keys.forEach { key ->
        diningList.add(diningMap[key]!!)
    }
    diningList
}