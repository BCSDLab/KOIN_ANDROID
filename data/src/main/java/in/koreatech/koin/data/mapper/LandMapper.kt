package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.land.LandDetailResponse
import `in`.koreatech.koin.data.response.land.LandResponse
import `in`.koreatech.koin.domain.model.land.Land
import `in`.koreatech.koin.domain.model.land.LandDetail

fun LandResponse.toLand() = Land(
    internalName = internalName ?: "-",
    monthlyFee = monthlyFee ?: "-",
    latitude = latitude,
    charterFee = charterFee ?: "X",
    name = name ?: "-",
    id = id,
    longitude = longitude,
    roomType = roomType?: "-"
)

fun LandDetailResponse.toLandDetail() = LandDetail(
    optElectronicDoorLock = optElectronicDoorLock,
    optTv = optTv,
    monthlyFee = monthlyFee ?: "",
    optElevator = optElevator,
    optWaterPurifier = optWaterPurifier,
    optWasher = optWasher,
    latitude = latitude,
    charterFee = charterFee ?: "",
    optVeranda = optVeranda,
    createdAt = createdAt,
    description = description ?: "",
    imageUrls = imageUrls ?: listOf(),
    optGasRange = optGasRange,
    optInduction = optInduction,
    internalName = internalName ?: "",
    isDeleted = isDeleted,
    updatedAt = updatedAt,
    optBidet = optBidet,
    optShoeCloset = optShoeCloset,
    optRefrigerator = optRefrigerator,
    id = id,
    floor = (floor?.toString()) ?: "",
    managementFee = managementFee ?: "",
    optDesk = optDesk,
    optCloset = optCloset,
    longitude = longitude,
    address = address ?: "",
    optBed = optBed,
    size = size?.toString() ?: "",
    phone = phone ?: "",
    optAirConditioner = optAirConditioner,
    name = name ?: "",
    deposit = deposit ?: "",
    optMicrowave = optMicrowave,
    roomType = roomType ?:""
)