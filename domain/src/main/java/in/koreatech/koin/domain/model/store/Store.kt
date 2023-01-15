package `in`.koreatech.koin.domain.model.store

import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.domain.util.ext.isCurrentOpen
import java.time.LocalDateTime
import java.time.LocalTime

data class Store(
    val id: Int,
    val name: String,
    val chosung: String,
    val category: StoreCategory?,
    val phoneNumber: String?,
    val openTime: LocalTime,
    val closeTime: LocalTime,
    val address: String?,
    val description: String?,
    val isDeliveryOk: Boolean,  //배달 가능 여부
    val deliveryPrice: Int,     //배달비
    val isCardOk: Boolean,      //카드결제 여부
    val isBankOk: Boolean,       //계좌이체 여부
    val images: List<String>,
    val updatedAt: LocalDateTime
) : Comparable<Store> {
    override fun compareTo(other: Store): Int {
        val isThisCurrentOpen = this.isCurrentOpen
        val isOtherCurrentOpen = other.isCurrentOpen

        return when {
            isThisCurrentOpen && isOtherCurrentOpen -> this.name.compareTo(other.name)
            !isThisCurrentOpen && isOtherCurrentOpen -> 1
            isThisCurrentOpen && !isOtherCurrentOpen -> -1
            else -> this.name.compareTo(other.name)
        }
    }
}