package `in`.koreatech.koin.di.userAgent

import dagger.Provides
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.di.userAgent.UserAgentProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserAgentModule {
    @Provides
    @Singleton
    fun provideUserAgentProvider(): UserAgentProvider = UserAgentProvider()
}