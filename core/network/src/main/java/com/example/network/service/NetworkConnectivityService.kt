package com.example.network.service

import com.example.network.state.NetworkStatus
import kotlinx.coroutines.flow.Flow

interface NetworkConnectivityService {
    val networkStatus: Flow<NetworkStatus>
    fun getLatestStatus(): NetworkStatus
    fun isConnected(): Boolean
}
