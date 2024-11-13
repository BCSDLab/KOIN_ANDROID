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
import dagger.hilt.components.SingletonComponent
import `in`.koreatech.koin.data.repository.OnboardingRepositoryImpl
import `in`.koreatech.koin.data.source.local.OnboardingLocalDataSource
import `in`.koreatech.koin.domain.repository.OnboardingRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OnboardingRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(
        onboardingRepositoryImpl: OnboardingRepositoryImpl
    ): OnboardingRepository
}

@Module
@InstallIn(SingletonComponent::class)
object OnboardingLocalDataSourceModule {

    private val Context.onboardingDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "onboarding"
    )

    @Provides
    @Singleton
    fun provideOnboardingManager(
        @ApplicationContext context: Context
    ): OnboardingLocalDataSource {
        return OnboardingLocalDataSource(context.onboardingDataStore)
    }
}