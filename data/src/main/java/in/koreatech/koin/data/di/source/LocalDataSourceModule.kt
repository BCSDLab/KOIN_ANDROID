package `in`.koreatech.koin.data.di.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.core.qualifier.IoDispatcher
import `in`.koreatech.koin.data.source.local.BusLocalDataSource
import `in`.koreatech.koin.data.source.local.DeptLocalDataSource
import `in`.koreatech.koin.data.source.local.SignupTermsLocalDataSource
import `in`.koreatech.koin.data.source.local.TimetableLocalDataSource
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.data.source.local.VersionLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {
    @Provides
    @Singleton
    fun provideSignupLocalDataSource(
        @ApplicationContext applicationContext: Context
    ) : SignupTermsLocalDataSource {
        return SignupTermsLocalDataSource(applicationContext)
    }

    @Provides
    @Singleton
    fun provideTokenLocalDataSource(
        @ApplicationContext applicationContext: Context,
        @IoDispatcher dispatcherIO: CoroutineDispatcher
    ) : TokenLocalDataSource {
        return TokenLocalDataSource(applicationContext, dispatcherIO)
    }

    @Provides
    @Singleton
    fun provideVersionLocalDataSource(
        @ApplicationContext applicationContext: Context
    ) : VersionLocalDataSource {
        return VersionLocalDataSource(applicationContext)
    }

    @Provides
    @Singleton
    fun provideBusLocalDataSource(
        @ApplicationContext applicationContext: Context
    ) : BusLocalDataSource {
        return BusLocalDataSource(applicationContext)
    }

    @Provides
    @Singleton
    fun provideDeptLocalDataSource(
        @ApplicationContext applicationContext: Context
    ) : DeptLocalDataSource {
        return DeptLocalDataSource(applicationContext)
    }

    @Provides
    @Singleton
    fun providesTimetableLocalDataSource(
        @ApplicationContext applicationContext: Context,
        dataStore: DataStore<Preferences>
    ): TimetableLocalDataSource {
        return TimetableLocalDataSource(applicationContext, dataStore)
    }
}