package `in`.koreatech.koin.ui.bus.state

import `in`.koreatech.koin.domain.util.ext.isAm
import `in`.koreatech.koin.domain.util.ext.isPm
import java.time.LocalTime

data class CityBusTimetableUiItem(
    val am: String,
    val pm: String
)

fun List<String>.toCityBusTimetableUiItemList(): List<CityBusTimetableUiItem> {
    val amList = this.filter { LocalTime.parse(it).isAm() }
    val pmList = this.filter { LocalTime.parse(it).isPm() }

    return amList.zip(pmList) { am, pm -> CityBusTimetableUiItem(am, pm) } +
            amList.drop(pmList.size).map { CityBusTimetableUiItem(it, "") } +
            pmList.drop(amList.size).map { CityBusTimetableUiItem("", it) }
}