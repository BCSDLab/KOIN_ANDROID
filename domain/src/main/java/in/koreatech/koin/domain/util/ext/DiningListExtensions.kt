package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningPlace
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
            DiningPlace.Korean.placeKorean -> 0
            DiningPlace.Onedish.placeKorean -> 1
            DiningPlace.Western.placeKorean -> 2
            DiningPlace.Special.placeKorean -> 3
            DiningPlace.Nungsu.placeKorean -> 4
            DiningPlace.Subakyeo.placeKorean -> 5
            else -> 6
        }
        diningMap[priority] = dining
    }
    diningMap.keys.forEach { key ->
        diningList.add(diningMap[key]!!)
    }
    diningList
}