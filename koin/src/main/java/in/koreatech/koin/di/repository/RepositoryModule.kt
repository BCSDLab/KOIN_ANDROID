package `in`.koreatech.koin.di.repository

import `in`.koreatech.koin.data.repository.SignupRepositoryImpl
import `in`.koreatech.koin.data.repository.TokenRepositoryImpl
import `in`.koreatech.koin.data.repository.UserRepositoryImpl
import `in`.koreatech.koin.data.repository.VersionRepositoryImpl
import `in`.koreatech.koin.data.source.local.SignupTermsLocalDataSource
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.data.source.local.VersionLocalDataSource
import `in`.koreatech.koin.data.source.remote.UserRemoteDataSource
import `in`.koreatech.koin.data.source.remote.VersionRemoteDataSource
import `in`.koreatech.koin.domain.repository.SignupRepository
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.repository.VersionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
        userRemoteDataSource: UserRemoteDataSource
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
}