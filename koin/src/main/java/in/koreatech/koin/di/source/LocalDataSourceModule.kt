package `in`.koreatech.koin.di.source

import `in`.koreatech.koin.data.source.local.*
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
}