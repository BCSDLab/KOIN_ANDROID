package `in`.koreatech.bus.state

import `in`.koreatech.koin.domain.model.bus.CityBusInfoV2

data class CityBusInfoState(
    val number: Int,
    val departNode: String,
    val arriveNode: String,
)

fun CityBusInfoV2.toCityBusInfoState() = CityBusInfoState(
    number = number,
    departNode = departNode,
    arriveNode = arriveNode,
)
