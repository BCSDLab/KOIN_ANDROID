package `in`.koreatech.business.di.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.data.repository.TokenRepositoryImpl
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.domain.repository.TokenRepository
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
}