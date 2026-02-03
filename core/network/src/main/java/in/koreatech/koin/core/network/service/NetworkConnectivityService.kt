package `in`.koreatech.koin.core.network.service

import `in`.koreatech.koin.core.network.state.NetworkStatus
import kotlinx.coroutines.flow.Flow

interface NetworkConnectivityService {
    val networkStatus: Flow<NetworkStatus>
    fun getLatestStatus(): NetworkStatus
    fun isConnected(): Boolean
}
