package `in`.koreatech.koin.domain.model.bus

sealed class BusType {
    object Shuttle: BusType()
    object Commuting: BusType()
    object Express: BusType()
}
