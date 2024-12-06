package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.bus.v2.BusNotice

interface BusV2Repository {
    suspend fun fetchBusNotice(): Result<BusNotice>
}