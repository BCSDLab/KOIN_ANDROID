package `in`.koreatech.koin.data.di.onboarding

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import `in`.koreatech.koin.data.repository.OnboardingRepositoryImpl
import `in`.koreatech.koin.data.source.local.OnboardingLocalDataSource
import `in`.koreatech.koin.domain.repository.OnboardingRepository

@Module
@InstallIn(ActivityComponent::class)
abstract class OnboardingRepositoryModule {

    @Binds
    @ActivityScoped
    abstract fun bindOnboardingRepository(
        onboardingRepositoryImpl: OnboardingRepositoryImpl
    ): OnboardingRepository
}

@Module
@InstallIn(ActivityComponent::class)
object OnboardingLocalDataSourceModule {

    private val Context.onboardingDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "onboarding"
    )

    @Provides
    @ActivityScoped
    fun provideOnboardingManager(
        @ApplicationContext context: Context
    ): OnboardingLocalDataSource {
        return OnboardingLocalDataSource(context.onboardingDataStore)
    }
}