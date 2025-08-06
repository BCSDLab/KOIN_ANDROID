package `in`.koreatech.koin.domain.service

import `in`.koreatech.koin.domain.state.network.NetworkStatus
import kotlinx.coroutines.flow.Flow

interface NetworkConnectivityService {
    val networkStatus: Flow<NetworkStatus>
    fun getLatestStatus(): NetworkStatus
}