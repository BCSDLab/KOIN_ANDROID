package `in`.koreatech.koin.core.onboarding

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import `in`.koreatech.koin.domain.repository.OnboardingRepository
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ActivityComponent::class)
object OnboardingModule {

    @Provides
    @ActivityScoped
    fun provideOnboardingManager(
        onboardingRepository: OnboardingRepository,
        @ApplicationContext context: Context
    ): OnboardingManager {
        return OnboardingManager(onboardingRepository, context, Dispatchers.Main)
    }
}