package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.entity.CoopShopEntity
import `in`.koreatech.koin.data.response.coopshop.CoopShopResponse
import `in`.koreatech.koin.data.response.coopshop.OpenCloseInfoResponse
import `in`.koreatech.koin.domain.model.coopshop.CoopShop
import `in`.koreatech.koin.domain.model.coopshop.OpenCloseInfo
import `in`.koreatech.koin.domain.model.coopshop.OpenCloseTimeInfo
import `in`.koreatech.koin.domain.model.coopshop.checkIfNotOpen
import `in`.koreatech.koin.domain.model.coopshop.toCoopShopDayType

fun CoopShopResponse.toCoopShop(semester: String, updatedAt: String): CoopShop {
    return CoopShop(
        id = id,
        name = name,
        semester = semester,
        opens = opens.groupByDayOfWeek(),
        phone = phone.orEmpty(),
        location = location,
        remarks = remarks.orEmpty(),
        updatedAt = updatedAt
    )
}

fun CoopShop.toCoopShopEntity(coopNameId: Int? = null) = CoopShopEntity(
    id = id,
    coopNameId = coopNameId,
    name = name,
    semester = semester,
    opens = opens,
    phone = phone,
    location = location,
    remarks = remarks,
    updatedAt = updatedAt
)

fun CoopShopEntity.toCoopShop() = CoopShop(
    id = id,
    name = name,
    semester = semester,
    opens = opens,
    phone = phone,
    location = location,
    remarks = remarks,
    updatedAt = updatedAt
)

fun List<OpenCloseInfoResponse>.groupByDayOfWeek(): List<OpenCloseInfo> =
    this.groupBy { it.dayOfWeek }.map { (day, infoList) ->
        OpenCloseInfo(
            dayOfWeek = day.toCoopShopDayType,
            opensByDayType =
            infoList.map { info ->
                OpenCloseTimeInfo(
                    type = info.type.orEmpty(),
                    openTime = info.openTime.orEmpty().checkIfNotOpen,
                    closeTime = info.closeTime.orEmpty().checkIfNotOpen
                )
            }
        )
    }
