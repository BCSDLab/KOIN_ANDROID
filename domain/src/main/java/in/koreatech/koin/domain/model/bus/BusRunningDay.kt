package `in`.koreatech.koin.domain.model.bus

sealed class BusRunningDay{
    object SUN: BusRunningDay()
    object MON: BusRunningDay()
    object TUE: BusRunningDay()
    object WED: BusRunningDay()
    object THU: BusRunningDay()
    object FRI: BusRunningDay()
    object SAT: BusRunningDay()
}
