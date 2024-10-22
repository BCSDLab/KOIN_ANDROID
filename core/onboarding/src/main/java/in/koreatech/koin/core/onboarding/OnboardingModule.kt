package `in`.koreatech.koin.core.onboarding

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.domain.repository.OnboardingRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardingModule {

    @Provides
    @Singleton
    fun provideOnboardingManager(
        onboardingRepository: OnboardingRepository,
        @ApplicationContext context: Context
    ): OnboardingManager {
        return OnboardingManager(onboardingRepository, context, Dispatchers.Main)
    }
}