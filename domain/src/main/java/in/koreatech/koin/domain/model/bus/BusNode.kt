package `in`.koreatech.koin.domain.model.bus

sealed class BusNode {
    object Koreatech: BusNode()
    object Station: BusNode()
    object Terminal: BusNode()
}
