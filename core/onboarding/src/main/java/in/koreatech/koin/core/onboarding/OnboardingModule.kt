package `in`.koreatech.koin.core.onboarding

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.domain.repository.OnboardingRepository
import `in`.koreatech.koin.domain.usecase.user.GetUserInfoUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardingModule {
    @Provides
    @Singleton
    fun provideOnboardingManager(
        onboardingRepository: OnboardingRepository,
        getUserInfoUseCase: GetUserInfoUseCase,
        @ApplicationContext context: Context
    ): OnboardingManager {
        return OnboardingManager(onboardingRepository, getUserInfoUseCase, context)
    }
}
