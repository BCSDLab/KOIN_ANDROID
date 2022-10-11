package `in`.koreatech.koin.domain.model.bus

sealed class BusRunningDay {
    object SUN : BusRunningDay()
    object MON : BusRunningDay()
    object TUE : BusRunningDay()
    object WED : BusRunningDay()
    object THU : BusRunningDay()
    object FRI : BusRunningDay()
    object SAT : BusRunningDay()
}

fun Iterable<Int>.toBusRunningDays() = this.mapIndexedNotNull { index, i ->
    if (i == 1) when (index) {
        0 -> BusRunningDay.SUN
        1 -> BusRunningDay.MON
        2 -> BusRunningDay.TUE
        3 -> BusRunningDay.WED
        4 -> BusRunningDay.THU
        5 -> BusRunningDay.FRI
        6 -> BusRunningDay.SAT
        else -> null
    } else null
}

fun Iterable<BusRunningDay>.toIntList(): List<Int> {
    val result = MutableList(7) { 0 }
    this.forEach {
        result[when (it) {
            BusRunningDay.SUN -> 0
            BusRunningDay.MON -> 1
            BusRunningDay.TUE -> 2
            BusRunningDay.WED -> 3
            BusRunningDay.THU -> 4
            BusRunningDay.FRI -> 5
            BusRunningDay.SAT -> 6
        }] = 1
    }

    return result
}