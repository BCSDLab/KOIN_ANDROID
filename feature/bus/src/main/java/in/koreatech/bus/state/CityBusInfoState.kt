package `in`.koreatech.bus.state

import `in`.koreatech.koin.domain.model.bus.CityBusInfo

data class CityBusInfoState(
    val number: Int,
    val departNode: String,
    val arriveNode: String
)

fun CityBusInfo.toCityBusInfoState() =
    CityBusInfoState(
        number = number,
        departNode = departNode,
        arriveNode = arriveNode
    )
