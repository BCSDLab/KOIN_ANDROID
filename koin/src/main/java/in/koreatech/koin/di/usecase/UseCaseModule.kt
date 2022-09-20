package `in`.koreatech.koin.di.usecase

import `in`.koreatech.koin.domain.repository.DiningRepository
import `in`.koreatech.koin.domain.usecase.dining.CheckCorrectDateRangeUseCase
import `in`.koreatech.koin.domain.usecase.dining.DiningUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideDiningUseCase(
        diningRepository: DiningRepository
    ): DiningUseCase = DiningUseCase(diningRepository)

    @Provides
    @Singleton
    fun provideCheckCorrectDateRangeUseCase(): CheckCorrectDateRangeUseCase {
        return CheckCorrectDateRangeUseCase()
    }
}