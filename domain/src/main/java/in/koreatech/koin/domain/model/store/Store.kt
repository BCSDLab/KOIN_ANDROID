package `in`.koreatech.koin.domain.model.store

import `in`.koreatech.koin.domain.util.ext.HHMM
import `in`.koreatech.koin.domain.util.ext.isEqualOrBigger
import `in`.koreatech.koin.domain.util.ext.isEqualOrSmaller
import `in`.koreatech.koin.domain.util.ext.localTimeNow
import java.time.LocalTime

data class Store(
    val uid: Int,
    val name: String,
    val phone: String,
    val isDeliveryOk: Boolean,
    val isCardOk: Boolean,
    val isBankOk: Boolean,
    val isEvent: Boolean,
    val isOpen: Boolean,
    val averageRate : Double,
    val reviewCount : Int,
    val open: OpenData,
    val categoryIds: List<StoreCategory?>
) {
    data class OpenData(
        val dayOfWeek: String,
        val closed: Boolean,
        val openTime: String,
        val closeTime: String,
    ) {
        fun openStore(): Boolean {
            return if(openTime.isNotEmpty() && closeTime.isNotEmpty()) {
                val openTime = LocalTime.parse(if(openTime == "24:00") "00:00" else openTime)
                val closeTime = LocalTime.parse(if(closeTime == "24:00") "00:00" else closeTime)
                val currentTime = LocalTime.parse(localTimeNow.HHMM)

                if (openTime.isBefore(closeTime)) { // 17:00(오픈 시간) < 23:00(종료 시간)
                    currentTime.isEqualOrBigger(openTime) && currentTime.isEqualOrSmaller(closeTime)
                } else if (openTime.isAfter(closeTime)) { // 17:00(오픈 시간) > 02:00(종료 시간)
                    currentTime.isEqualOrBigger(openTime) || currentTime.isEqualOrSmaller(closeTime)
                } else { // 00:00 ~ 00:00 (하루종일 영업)
                    true
                }
            } else {
                false
            }
        }
    }
}