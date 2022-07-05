package `in`.koreatech.koin.di.repository

import `in`.koreatech.koin.data.repository.TokenRepositoryImpl
import `in`.koreatech.koin.data.repository.UserRepositoryImpl
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.data.source.remote.UserRemoteDataSource
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.repository.UserRepository
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
}