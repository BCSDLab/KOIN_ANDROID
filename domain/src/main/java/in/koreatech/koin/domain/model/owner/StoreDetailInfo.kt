package `in`.koreatech.koin.domain.model.owner

import `in`.koreatech.koin.domain.model.owner.insertstore.OperatingTime

data class StoreDetailInfo (
    val address: String?,
    val categoryIds: List<Int>,
    val isDeliveryOk: Boolean,
    val deliveryPrice: Int,
    val description: String,
    val imageUrls: List<String>,
    val name: String,
    val operatingTime: List<OperatingTime>,
    val isBankOk: Boolean,
    val isCardOk: Boolean,
    val phone: String,
    val bank: String?,
    val accountNumber: String?,
)