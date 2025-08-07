package com.example.network.di

import android.content.Context
import android.net.ConnectivityManager
import com.example.network.service.NetworkConnectivityService
import com.example.network.service.NetworkConnectivityServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkMoudule {
    @Singleton
    @Provides
    fun provideConnectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager = context.getSystemService(ConnectivityManager::class.java)

    @Singleton
    @Provides
    fun provideNetworkConnectivityService(
        connectivityManager: ConnectivityManager
    ): NetworkConnectivityService = NetworkConnectivityServiceImpl(connectivityManager)
}
