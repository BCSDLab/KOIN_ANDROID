package `in`.koreatech.koin.core.network.service

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import `in`.koreatech.koin.core.network.state.NetworkStatus
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

class NetworkConnectivityServiceImpl @Inject constructor(
    private val connectivityManager: ConnectivityManager
) : NetworkConnectivityService {

    private var latestStatus: NetworkStatus = NetworkStatus.Disconnected

    init {
        checkCurrentNetwork()
    }

    private fun checkCurrentNetwork() {
        val currentNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(currentNetwork)
        latestStatus = if (capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
            NetworkStatus.Connected
        } else {
            NetworkStatus.Disconnected
        }
    }

    override val networkStatus: Flow<NetworkStatus> = callbackFlow {
        val connectivityCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                latestStatus = NetworkStatus.Connected
                trySend(latestStatus)
            }

            override fun onUnavailable() {
                latestStatus = NetworkStatus.Disconnected
                trySend(latestStatus)
            }

            override fun onLost(network: Network) {
                latestStatus = NetworkStatus.Disconnected
                trySend(latestStatus)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, connectivityCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(connectivityCallback)
        }
    }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)

    override fun getLatestStatus(): NetworkStatus {
        checkCurrentNetwork()
        return latestStatus
    }

    override fun isConnected(): Boolean {
        return getLatestStatus() == NetworkStatus.Connected
    }
}
