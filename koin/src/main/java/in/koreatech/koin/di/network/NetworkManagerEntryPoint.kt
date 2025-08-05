package `in`.koreatech.koin.di.network

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.util.NetworkManager

@EntryPoint
@InstallIn(SingletonComponent::class)
interface NetworkManagerEntryPoint {
    fun networkManager(): NetworkManager
}