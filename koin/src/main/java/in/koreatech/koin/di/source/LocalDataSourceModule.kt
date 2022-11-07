package `in`.koreatech.koin.di.source

import `in`.koreatech.koin.data.source.local.BusLocalDataSource
import `in`.koreatech.koin.data.source.local.SignupTermsLocalDataSource
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.data.source.local.VersionLocalDataSource
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
        @ApplicationContext applicationContext: Context
    ) : TokenLocalDataSource {
        return TokenLocalDataSource(applicationContext)
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
    fun provideBusLocalDataSource() : BusLocalDataSource {
        return BusLocalDataSource()
    }
}