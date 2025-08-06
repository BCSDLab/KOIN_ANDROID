package `in`.koreatech.koin.data.service

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import `in`.koreatech.koin.domain.service.NetworkConnectivityService
import `in`.koreatech.koin.domain.state.network.NetworkStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NetworkConnectivityServiceImpl @Inject constructor (
    connectivityManager: ConnectivityManager
): NetworkConnectivityService {

    private val currentNetwork = connectivityManager.activeNetwork
    private val capabilities = connectivityManager.getNetworkCapabilities(currentNetwork)
    private var latestStatus: NetworkStatus = if (capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
        NetworkStatus.Connected
    } else {
        NetworkStatus.Disconnected
    }

    override val networkStatus: Flow<NetworkStatus> = callbackFlow {
        val connectivityCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                latestStatus = NetworkStatus.Connected
                trySend(NetworkStatus.Connected)
            }

            override fun onUnavailable() {
                latestStatus = NetworkStatus.Disconnected
                trySend(NetworkStatus.Disconnected)
            }

            override fun onLost(network: Network) {
                latestStatus = NetworkStatus.Disconnected
                trySend(NetworkStatus.Disconnected)
            }

        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        connectivityManager.registerNetworkCallback(request, connectivityCallback)
        Log.e("MYLOG","${latestStatus} it is")

        awaitClose {
            connectivityManager.unregisterNetworkCallback(connectivityCallback)
        }
    }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)

    override fun getLatestStatus(): NetworkStatus = latestStatus
}