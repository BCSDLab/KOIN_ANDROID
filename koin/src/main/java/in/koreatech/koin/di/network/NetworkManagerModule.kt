package `in`.koreatech.koin.di.network

import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.util.NetworkManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkManagerModule {

    @Singleton
    @Provides
    fun provideConnectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager = context.getSystemService(ConnectivityManager::class.java)

    @Singleton
    @Provides
    fun networkCallbackManagerManager(
        connectivityManager: ConnectivityManager
    ): NetworkManager = NetworkManager(connectivityManager)
}