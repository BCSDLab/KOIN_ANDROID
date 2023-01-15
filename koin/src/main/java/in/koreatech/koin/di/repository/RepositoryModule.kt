package `in`.koreatech.koin.di.repository

import `in`.koreatech.koin.data.repository.*
import `in`.koreatech.koin.domain.repository.*
import `in`.koreatech.koin.data.repository.DiningRepositoryImpl
import `in`.koreatech.koin.data.repository.SignupRepositoryImpl
import `in`.koreatech.koin.data.repository.TokenRepositoryImpl
import `in`.koreatech.koin.data.repository.UserRepositoryImpl
import `in`.koreatech.koin.data.repository.VersionRepositoryImpl
import `in`.koreatech.koin.data.source.local.*
import `in`.koreatech.koin.data.source.remote.*
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTokenRepository(
        tokenLocalDataSource: TokenLocalDataSource
    ) : TokenRepository {
        return TokenRepositoryImpl(tokenLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userRemoteDataSource: UserRemoteDataSource,
    ) : UserRepository {
        return UserRepositoryImpl(userRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideSignupRepository(
        userRemoteDataSource: UserRemoteDataSource,
        signupTermsLocalDataSource: SignupTermsLocalDataSource
    ) : SignupRepository {
        return SignupRepositoryImpl(userRemoteDataSource, signupTermsLocalDataSource)
    }

    @Provides
    @Singleton
    fun provideVersionRepository(
        versionLocalDataSource: VersionLocalDataSource,
        versionRemoteDataSource: VersionRemoteDataSource
    ) : VersionRepository {
        return VersionRepositoryImpl(
            versionLocalDataSource,
            versionRemoteDataSource
        )
    }

    @Provides
    @Singleton
    fun provideDeptRepository(
        deptRemoteDataSource: DeptRemoteDataSource,
        deptLocalDataSource: DeptLocalDataSource
    ) : DeptRepository {
        return DeptRepositoryImpl(
            deptRemoteDataSource, deptLocalDataSource
        )
    }

    @Provides
    @Singleton
    fun provideDiningRepository(
        diningRemoteDataSource: DiningRemoteDataSource
    ): DiningRepository {
        return DiningRepositoryImpl(diningRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideBusRepository(
        @ApplicationContext applicationContext: Context,
        busLocalDataSource: BusLocalDataSource,
        busRemoteDataSource: BusRemoteDataSource
    ): BusRepository {
        return BusRepositoryImpl(applicationContext, busLocalDataSource, busRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideStoreRepository(
        storeRemoteDataSource: StoreRemoteDataSource
    ): StoreRepository {
        return StoreRepositoryImpl(storeRemoteDataSource)
    }
}