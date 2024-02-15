package `in`.koreatech.koin.data.mapper.business

import `in`.koreatech.koin.data.constant.STORE_CLOSE_TIME_FORMAT
import `in`.koreatech.koin.data.constant.STORE_OPEN_TIME_FORMAT
import `in`.koreatech.koin.data.source.remote.business.TestItems
import `in`.koreatech.koin.domain.model.business.mystore.MyStore
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun TestItems.toMyStore(): MyStore = MyStore(
    id = id,
    name = name,
    phoneNumber = phoneNumber,
    openTime = if (openTime == null){
        LocalTime.MIN
    } else {
        LocalTime.parse(openTime, DateTimeFormatter.ofPattern(STORE_OPEN_TIME_FORMAT))
    },
    closeTime = if (closeTime == null){
        LocalTime.MAX
    } else {
        LocalTime.parse(closeTime, DateTimeFormatter.ofPattern(STORE_CLOSE_TIME_FORMAT))
    },
    dayOfHoliday = dayOfHoliday,
    address = address,
    deliveryPrice = deliveryPrice,
    description = description,
    isDeliveryOk = isDeliveryOk ?: false,
    isCardOk = isCardOk ?: false,
    isBankOk = isBankOk ?: false,
    images = images ?: emptyList(),
    categories = categories ?: emptyList()
)
