package `in`.koreatech.business.di.userAgent

import dagger.Provides
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserAgentModule {
    @Provides
    @Singleton
    fun provideUserAgentProvider(): UserAgentProvider = UserAgentProvider()
}